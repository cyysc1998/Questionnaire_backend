package com.example.questionnaire_backend.service.impl.tools;

import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class Statistic {

    public static Double sum(ArrayList<Double> array) {
        double sum = 0;
        for(double d: array)
            sum += d;
        return sum;
    }

    public static Double average(ArrayList<Double> array) {
        if(array.size() == 0)
            return 0.0;
        double average = sum(array) / array.size();
        return average;
    }

    public static Double min(ArrayList<Double> array) {
        if(array.size() == 0)
            return 0.0;
        double minElement = array.get(0);
        for(double d: array)
            minElement = Math.min(minElement, d);
        return minElement;
    }

    public static Double max(ArrayList<Double> array) {
        if(array.size() == 0)
            return 0.0;
        double maxElement = array.get(0);
        for(double d: array)
            maxElement = Math.max(maxElement, d);
        return maxElement;
    }

    public static Double median(ArrayList<Double> array) {
        if(array.size() == 0)
            return 0.0;
        Collections.sort(array);
        double median;
        if(array.size() % 2 == 1) {
            median = array.get(array.size() / 2);
        }
        else {
            System.out.println(array.size());
            median = (array.get((array.size()-1) / 2) + array.get(array.size() / 2)) / 2;
        }
        return median;
    }

    public static Double mode(ArrayList<Double> array) {
        if(array.size() == 0)
            return 0.0;
        HashMap<Double, Integer> map = new HashMap<>();
        for(double d: array) {
            if(map.containsKey(d))
                map.put(d, map.get(d) + 1);
            else
                map.put(d, 1);
        }
        ArrayList<Double> maxTimes = new ArrayList<>();
        int times = 0;
        for(Map.Entry<Double, Integer> entry: map.entrySet()) {
            if(entry.getValue() > times) {
                maxTimes.clear();
                maxTimes.add(entry.getKey());
                times = entry.getValue();
            }
            else if(entry.getValue() == times)
                maxTimes.add(entry.getKey());
        }
        return average(maxTimes);
    }
}
