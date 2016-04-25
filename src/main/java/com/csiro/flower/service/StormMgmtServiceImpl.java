/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

//import analytics.flow.resource.control.StormClusterManagement;
import backtype.storm.generated.Nimbus;
import backtype.storm.generated.RebalanceOptions;
import backtype.storm.generated.SupervisorSummary;
import backtype.storm.generated.TopologySummary;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.csiro.flower.util.CloudServiceRegionUtil;
import com.csiro.flower.util.HashMapUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
public class StormMgmtServiceImpl implements StormMgmtService {

    AmazonEC2 ec2;
    private static final int WAIT_FOR_TRANSITION_INTERVAL = 5000;
    String stoppedState = "stopped";
    String runningState = "running";
    String activeStatus = "ACTIVE";
    String supervisorPrefix = "Worker";
    String nimbusHost = "nimbus.host";
    Nimbus.Client nimbusClient;
    String serviceName= "ec2";

    @Override
    public void initService(String provider, String accessKey, String secretKey, String region) {
        ec2 = new AmazonEC2Client(new BasicAWSCredentials(accessKey,secretKey));
        String ec2Endpoint = CloudServiceRegionUtil.resolveEndpoint(provider, serviceName, region);
        ec2.setEndpoint(ec2Endpoint);
    }

    @Override
    public String getInstanceState(String instanceId) {
        DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult describeInstanceResult = ec2.describeInstances(describeInstanceRequest);
        String currentState = describeInstanceResult.getReservations().get(0).getInstances().get(0).getState().getName();
        return currentState;
    }

    @Override
    public List<String> getRunningWorkerIds() {

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        DescribeInstancesResult result = ec2.describeInstances(request);
        List<Reservation> reservations = result.getReservations();
        List<String> instanceIds = new ArrayList<String>();
        for (Reservation reservation : reservations) {
            List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
                for (Tag tag : instance.getTags()) {
                    if ((tag.getValue().startsWith(supervisorPrefix)) && (getInstanceState(instance.getInstanceId()).equals(runningState))) {
                        instanceIds.add(instance.getInstanceId());
                    }
                }
            }
        }
        return instanceIds;
    }

    @Override
    public List<String> getStoppedWorkerIds() {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        DescribeInstancesResult result = ec2.describeInstances(request);
        List<Reservation> reservations = result.getReservations();
        List<String> instanceIds = new ArrayList<String>();

        for (Reservation reservation : reservations) {
            List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
                for (Tag tag : instance.getTags()) {
                    if ((tag.getValue().startsWith(supervisorPrefix)) && (getInstanceState(instance.getInstanceId()).equals(stoppedState))) {
                        instanceIds.add(instance.getInstanceId());
                    }
                }
            }
        }
        return instanceIds;
    }

    @Override
    public void startWorkers(int no) {
        int startedWorkerCount = 0;
        HashMap<String, String> startedInstances = startInstances(getStoppedWorkerIds().subList(0, no));
        for (String state : startedInstances.values()) {
            if (state.matches(runningState)) {
                startedWorkerCount++;
            }
        }
//        LOGGER.info(startedWorkerCount + " workers out of " + no + " numbers started.");
    }

    public HashMap<String, String> startInstances(List<String> instanceIds) {

        HashMap<String, String> instancesState = new HashMap<String, String>();
        StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceIds);
        StartInstancesResult startResult = ec2.startInstances(startRequest);
        List<InstanceStateChange> stateChangeList = startResult.getStartingInstances();
        // Wait for the instance to be started
        for (String instanceId : instanceIds) {
            try {
                instancesState.put(instanceId, waitForTransitionCompletion(stateChangeList, runningState, instanceId));
            } catch (InterruptedException ex) {
                Logger.getLogger(StormMgmtServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instancesState;
    }

    @Override
    public void stopWorkers(int no) {
        int stoppedWorkerCount = 0;
        HashMap<String, String> stoppedInstances = stopInstances(getLowCostToStopRunningWorkers().subList(0, no), true);
        for (String state : stoppedInstances.values()) {
            if (state.matches(stoppedState)) {
                stoppedWorkerCount++;
            }
        }
//        LOGGER.info(stoppedWorkerCount + " workers out of " + no + " numbers stopped.");
    }

    public List<String> getLowCostToStopRunningWorkers() {

        // This method returns list of running workers that are more economical to be stopped.
        // To this end we use Max(Uptime % 1h) i.e. sort the list DESC
        Date endTime = new Date();
        long upTime;
        double remainingTime;
        int SixtyMinMill = 1 * 60 * 60 * 1000;
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        DescribeInstancesResult result = ec2.describeInstances(request);
        List<Reservation> reservations = result.getReservations();

        Map<String, Double> sortedLowCostWorkerList;
        HashMap<String, Double> workerCostMap = new HashMap<String, Double>();

        for (Reservation reservation : reservations) {
            List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
                for (Tag tag : instance.getTags()) {
                    if ((tag.getValue().startsWith(supervisorPrefix)) && (getInstanceState(instance.getInstanceId()).equals(runningState))) {
                        upTime = (endTime.getTime() - instance.getLaunchTime().getTime());
                        remainingTime = upTime % SixtyMinMill;
                        workerCostMap.put(instance.getInstanceId(), remainingTime);
                    }
                }
            }
        }
        sortedLowCostWorkerList = HashMapUtil.sortStrDoubleHashMap(workerCostMap, false);
        return new ArrayList<String>(sortedLowCostWorkerList.keySet());
    }

    public HashMap<String, String> stopInstances(List<String> instanceIds, final Boolean forceStop) {

        HashMap<String, String> instancesState = new HashMap<String, String>();
        // Stop the instance
        StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceIds).withForce(forceStop);
        StopInstancesResult startResult = ec2.stopInstances(stopRequest);
        List<InstanceStateChange> stateChangeList = startResult.getStoppingInstances();

        // Wait for the instance to be stopped
        for (String instanceId : instanceIds) {
            try {
                instancesState.put(instanceId, waitForTransitionCompletion(stateChangeList, stoppedState, instanceId));
            } catch (InterruptedException ex) {
                Logger.getLogger(StormMgmtServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instancesState;
    }

    public final String waitForTransitionCompletion(List<InstanceStateChange> stateChangeList,
            final String desiredState, String instanceId)
            throws InterruptedException {
        Boolean transitionCompleted = false;
        InstanceStateChange stateChange = stateChangeList.get(0);
        String previousState = stateChange.getPreviousState().getName();
        String currentState = stateChange.getCurrentState().getName();
        DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);

        while (!transitionCompleted) {
            try {
                DescribeInstancesResult describeInstanceResult = ec2.describeInstances(describeInstanceRequest);
                currentState = describeInstanceResult.getReservations().get(0).getInstances().get(0).getState().getName();

                if (previousState.equals(currentState)) {
//                    LOGGER.info("... '" + instanceId + "' is still in state " + currentState + " ...");
                } else {
//                    LOGGER.info("... '" + instanceId + "' entered state " + currentState + " ...");
                    //transitionReason = instance.getStateTransitionReason();
                }
                previousState = currentState;

                if (currentState.equals(desiredState)) {
                    transitionCompleted = true;
                }
            } catch (AmazonServiceException ase) {
//                LOGGER.log(Level.SEVERE, "Failed to describe instance '" + instanceId + "'!", ase);
                throw ase;
            }

            // Sleep for WAIT_FOR_TRANSITION_INTERVAL seconds until transition has completed.
            if (!transitionCompleted) {
                Thread.sleep(WAIT_FOR_TRANSITION_INTERVAL);
            }
        }

//        LOGGER.info("Transition of instance '" + instanceId + "' completed with state " + currentState + ").");
        return currentState;
    }

    @Override
    public void simpleRebalanceTopology(String topologyName) {
//        Nimbus.Client client = getStormClient();
        List<TopologySummary> toplogiesList;
        try {
            toplogiesList = nimbusClient.getClusterInfo().get_topologies();
            RebalanceOptions rebalanceOptions = new RebalanceOptions();
            rebalanceOptions.set_wait_secs(1);
//            nimbusClient.rebalance(toplogiesList.get(0).get_name(), rebalanceOptions);
            nimbusClient.rebalance(topologyName, rebalanceOptions);
        } catch (Exception ex) {
            Logger.getLogger(StormMgmtServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void fairRebalanceTopology() {
        try {
            int totalSlots = 0;
            int freeSlots;
            int usedSlots = 0;
            int activeTopologies = 0;
            int fairSlotShare;
//            Nimbus.Client client = getStormClient();
            List<TopologySummary> toplogiesList = nimbusClient.getClusterInfo().get_topologies();
            List<SupervisorSummary> supervisorsList = nimbusClient.getClusterInfo().get_supervisors();
            HashMap<String, Double> topologyWorkerMap = new HashMap<String, Double>();
            Map<String, Double> sortedTopologyWorkerMap;
            for (SupervisorSummary supervisor : supervisorsList) {
                totalSlots += supervisor.get_num_workers();
                usedSlots += supervisor.get_num_used_workers();
            }
            freeSlots = totalSlots - usedSlots;

            RebalanceOptions rebalanceOptions = new RebalanceOptions();
            rebalanceOptions.set_wait_secs(1);
            for (TopologySummary topology : toplogiesList) {
                if (topology.get_status().matches(activeStatus)) {
                    topologyWorkerMap.put(topology.get_name(), (double) topology.get_num_workers());
                    activeTopologies++;
                }
            }
            sortedTopologyWorkerMap = HashMapUtil.sortStrDoubleHashMap(topologyWorkerMap, true);
            fairSlotShare = freeSlots / activeTopologies;

            for (Map.Entry<String, Double> topology_worker : sortedTopologyWorkerMap.entrySet()) {
                if ((fairSlotShare == 0) && (freeSlots > 0)) {
                    rebalanceOptions.set_num_workers((int) (topology_worker.getValue() + 1));
                    freeSlots--;
                } else if (fairSlotShare > 0) {
                    rebalanceOptions.set_num_workers((int) (topology_worker.getValue() + fairSlotShare));
                    // freeSlots -= fairSlotShare;
                }
                nimbusClient.rebalance(topology_worker.getKey(), rebalanceOptions);
            }
        } catch (Exception ex) {
            Logger.getLogger(StormMgmtServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void buildStormClient(String nimbusIp) {
        Map storm_conf = Utils.readStormConfig();
        storm_conf.put(nimbusHost, nimbusIp);
        nimbusClient = NimbusClient.getConfiguredClient(storm_conf).getClient();
    }

}
