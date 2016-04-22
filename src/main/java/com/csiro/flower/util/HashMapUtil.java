/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.util;

import com.amazonaws.services.kinesis.model.Shard;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kho01f
 */
public class HashMapUtil {

    public static Map<String, Double> sortStrDoubleHashMap(Map<String, Double> unsortMap, final boolean order) {

        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1,
                    Map.Entry<String, Double> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static Map<Shard, BigInteger> sortShardBigIntHashMap(Map<Shard, BigInteger> unsortMap, final boolean order) {

        List<Map.Entry<Shard, BigInteger>> list = new LinkedList<Map.Entry<Shard, BigInteger>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Shard, BigInteger>>() {
            @Override
            public int compare(Map.Entry<Shard, BigInteger> o1,
                    Map.Entry<Shard, BigInteger> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Shard, BigInteger> sortedMap = new LinkedHashMap<Shard, BigInteger>();
        for (Map.Entry<Shard, BigInteger> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
