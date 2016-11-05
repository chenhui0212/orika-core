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
package ma.glasnost.orika.impl.generator;

import ma.glasnost.orika.Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author matt.deboer@gmail.com
 *
 */
public class UsedConvertersContext {
    
    private Map<Converter,Integer> usedConverters = new HashMap<Converter,Integer>();
    private int usedTypeIndex = 0;

    public int getIndex(Converter converter) {
        if (converter == null) {
            throw new NullPointerException("type must not be null");
        }
        Integer index = usedConverters.get(converter);
        if (index == null) {
            index = usedTypeIndex++;
            usedConverters.put(converter, index);
        }
        return index;
    }
    
    public Converter<?, ?>[] toArray() {
        Converter[] converters = new Converter[usedConverters.size()];
        for (Entry<Converter, Integer> entry : usedConverters.entrySet()) {
            converters[entry.getValue()] = entry.getKey();
        }
        return converters;
    }
}
