/**
 * 
 */
package com.rayzr522.decoheads.util;

import java.util.HashMap;

/**
 * @author Rayzr
 *
 */
public class CompatMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = -997967509749179259L;

    public V getOrDefault(Object key, V defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }
}
