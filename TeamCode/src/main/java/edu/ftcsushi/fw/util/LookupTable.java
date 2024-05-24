package edu.ftcsushi.fw.util;

import java.util.TreeMap;

/**
 * A lookup table that can find the item closest to a given key.
 *
 * <P/> Example use-case: We need to save history of poses by timestamps, and later find the pose
 * closest to the time when an image was processed.
 *
 * TODO: The insert and lookup operations take O(log(N)).  Can this be improved?
 */
public class LookupTable<T extends Number, R> extends TreeMap<T, R> {

    public void add(T key, R out) {
        put(key, out);
    }

    /**
     * Returns the closest possible value for the given key.
     *
     * @param key the input key
     * @return the closest value to the input key
     */
    public R getClosest(T key) {
        Entry<T, R> ceil = ceilingEntry(key);
        Entry<T, R> floor = floorEntry(key);

        if (ceil != null && floor != null) {
            double keyVal = key.doubleValue();
            double ceilDiff = Math.abs(ceil.getKey().doubleValue() - keyVal);
            double floorDiff = Math.abs(floor.getKey().doubleValue() - keyVal);
            return floorDiff < ceilDiff ? floor.getValue() : ceil.getValue();
        } else if (ceil != null) {
            return ceil.getValue();
        } else if (floor != null) {
            return floor.getValue();
        } else {
            return null;
        }
    }
}
