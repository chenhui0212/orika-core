/*
 * Orika - simpler, better and faster Java bean mapping
 *
 * Copyright (C) 2011-2013 Orika authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ma.glasnost.orika.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import ma.glasnost.orika.MappedTypePair;

/**
 * HashMapUtility provides an wrapper for obtaining instances of ConcurrentLinkedHashMap
 * and is compatible with both 1.2 and later 1.x versions of this class.
 */
public class HashMapUtility {
    
    /**
     * Generates a new instance of ConcurrentLinkedHashMap with the specified max weighted capacity
     *
     * @param capacity
     * @return a new instance of ConcurrentLinkedHashMap with the specified max weighted capacity
     */
    public static <K, V extends MappedTypePair<Object, Object>> Cache<K, V> getCache(long capacity) {
        
    	// Evict based on the number of entries in the cache
    	return Caffeine.newBuilder()
    	    .maximumSize(capacity)
    	    .build();
    	
    	
    }
}
