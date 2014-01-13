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

package ma.glasnost.orika.metadata;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ma.glasnost.orika.DefaultFieldMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.property.PropertyResolverStrategy;

/**
 * CaseInsensitiveClassMapBuilder is an extension of ClassMapBuilder which performs
 * case-insensitive matching of property names in the 'byDefault()' method.
 * 
 * @author mattdeboer
 *
 * @param <A>
 * @param <B>
 */
public class CaseInsensitiveClassMapBuilder<A, B> extends ClassMapBuilder<A, B> {
  
    public static class Factory extends ClassMapBuilderFactory {
        
        @Override
        protected <A, B> ClassMapBuilder<A, B> newClassMapBuilder(final Type<A> aType, final Type<B> bType,
                final MapperFactory mapperFactory, final PropertyResolverStrategy propertyResolver, final DefaultFieldMapper[] defaults) {
            
            return new CaseInsensitiveClassMapBuilder<A, B>(aType, bType, mapperFactory, propertyResolver, defaults);
        }
    }
    
    private Map<String, String> lowercasePropertiesForA;
    private Map<String, String> lowercasePropertiesForB;
    private boolean initialized;
    
    
    protected CaseInsensitiveClassMapBuilder(final Type<A> aType, final Type<B> bType, final MapperFactory mapperFactory,
            final PropertyResolverStrategy propertyResolver, final DefaultFieldMapper[] defaults) {
        super(aType, bType, mapperFactory, propertyResolver, defaults);
        
        lowercasePropertiesForA = new LinkedHashMap<String, String>();
        for (String prop : propertyResolver.getProperties(getAType()).keySet()) {
            lowercasePropertiesForA.put(prop.toLowerCase(), prop);
        }
        
        lowercasePropertiesForB = new LinkedHashMap<String, String>();
        for (String prop : propertyResolver.getProperties(getBType()).keySet()) {
            lowercasePropertiesForB.put(prop.toLowerCase(), prop);
        }
        initialized = true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * ma.glasnost.orika.metadata.ClassMapBuilder#byDefault(ma.glasnost.orika
     * .DefaultFieldMapper[])
     * 
     * Applies default mapping allowing for case-insensitive matching of
     * property names
     */
    @Override
    public ClassMapBuilder<A, B> byDefault(final MappingDirection direction, final DefaultFieldMapper... withDefaults) {
        
        super.byDefault(direction, withDefaults);
        
        DefaultFieldMapper[] defaults;
        if (withDefaults.length == 0) {
            defaults = getDefaultFieldMappers();
        } else {
            defaults = withDefaults;
        }
        
        for (final Entry<String, String> entry : lowercasePropertiesForA.entrySet()) {
            String propertyNameA = entry.getValue();
            String lowercaseName = entry.getKey();
            if (!getMappedPropertiesForTypeA().contains(propertyNameA)) {
                if (lowercasePropertiesForB.containsKey(lowercaseName)) {
                    String propertyNameB = lowercasePropertiesForB.get(lowercaseName);
                    if (!getMappedPropertiesForTypeB().contains(propertyNameB)) {
                        if (!propertyNameA.equals("class")) {
                            fieldMap(propertyNameA, propertyNameB, true).direction(direction).add();
                        }
                    }
                } else {
                    Property prop = resolvePropertyForA(propertyNameA);
                    for (DefaultFieldMapper defaulter : defaults) {
                        String suggestion = defaulter.suggestMappedField(propertyNameA, prop.getType());
                        if (suggestion != null && getPropertiesForTypeB().contains(suggestion)) {
                            if (!getMappedPropertiesForTypeB().contains(suggestion)) {
                                fieldMap(propertyNameA, suggestion, true).direction(direction).add();
                            }
                        }
                    }
                }
            }
        }
        
        return this;
    }
    
}
