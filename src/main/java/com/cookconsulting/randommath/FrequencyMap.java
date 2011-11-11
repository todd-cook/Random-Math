/*
 * Copyright (c) 2011, Todd Cook.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the <ORGANIZATION> nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.cookconsulting.randommath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for counting categories, partitioning data, etc
 *
 * @author Todd Cook
 * @since Apr 28, 2008
 *
 */
public class FrequencyMap<T> {

    private int containerSize = 50;
    private ConcurrentHashMap<T, Integer> frequencyTable;
    private int itemsProcessed = 0;

    public FrequencyMap(int containerSize) {
        this.containerSize = containerSize;
        initialize();
    }

    public FrequencyMap() {
        initialize();
    }

    private void initialize() {
        frequencyTable = new ConcurrentHashMap<T, Integer>(containerSize);
    }

    public Map<T, Integer> getMap (){ return Collections.unmodifiableMap(frequencyTable);}

    public void clear() { frequencyTable.clear(); itemsProcessed=0;}

    /**
     * Swallows null inserts
     *
     * @param item
     */
    public void add(T item) {
        if (item == null) {
            return;
        }
        itemsProcessed++;
        if (frequencyTable.containsKey(item)) {
            Integer val = frequencyTable.get(item);
            val++;
            frequencyTable.put(item, val);
        }
        else {
            frequencyTable.put(item, 1);
        }
    }

    public void remove(T item) {
        frequencyTable.remove(item);
    }

    /**
     * @param item
     * @return item or null if no longer present
     */
    public Integer get(T item) {
        return frequencyTable.get(item);
    }

    public int getItemsProcessed() {
        return itemsProcessed;
    }

    /**
     * @return Set of unique keys
     */
    public Set<T> keys() {
        return frequencyTable.keySet();
    }

    /**
     * Return a list of strings with frequency values greater than floor.
     * Sorted in descending order
     *
     * @param floor
     * @return
     */
    public List<String> getFloor(Integer floor) {
        List<String> results = new ArrayList<String>(keys().size());
        List<FrequencyTuple<T>> tuples = this.getFloorFT(floor);
        for (FrequencyTuple ft : tuples) {
            results.add(ft.toString());
        }
        return results;
    }

    /**
     * Return a list of strings with frequency values greater than floor.
     * Sorted in descending order
     *
     * @param floor
     * @return
     */
    public List<String> getFloorKeys(Integer floor) {
        List<String> results = new ArrayList<String>(keys().size());
        List<FrequencyTuple<T>> tuples = this.getFloorFT(floor);
        for (FrequencyTuple ft : tuples) {
            results.add(ft.getKey().toString());
        }
        return results;
    }

    /**
     * @param floor
     * @return returns a sorted list of the tuples, with values above the specified floor
     */
    public List<FrequencyTuple<T>> getFloorFT(Integer floor) {
        List<FrequencyTuple<T>> tuples = new ArrayList<FrequencyTuple<T>>(keys().size());

        for (T key : keys()) {
            Integer val = frequencyTable.get(key);
            if (val.equals(floor) || val > floor) {
                tuples.add(new FrequencyTuple<T>(key, val));
            }
        }
        Collections.sort(tuples);
        return tuples;
    }
}
