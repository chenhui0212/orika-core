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
package ma.glasnost.orika.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.MapperKey;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.util.Ordering;
import ma.glasnost.orika.util.SortedCollection;

/**
 * The MultipleMapperWrapper wraps multiple instances of {@link Mapper} and decide dynamically during Mapping which {@link Mapper} should be
 * used.
 * <p>
 * 
 * @see <a href="https://github.com/orika-mapper/orika/issues/176">https://github.com/orika-mapper/orika/issues/176</a>
 */
public final class MultipleMapperWrapper extends GeneratedMapperBase {
    private Collection<Mapper<Object, Object>> mappersRegistry;
    private Map<MapperKey, Mapper<Object, Object>> mappersCache;
    
    public MultipleMapperWrapper(Type<Object> typeA, Type<Object> typeB, List<Mapper<Object, Object>> mappers) {
        super();
        setAType(typeA);
        setBType(typeB);
        mappersRegistry = new SortedCollection<Mapper<Object, Object>>(mappers, Ordering.MAPPER);
        mappersCache = new WeakHashMap<MapperKey, Mapper<Object, Object>>();
    }
    
    @Override
    public void mapAtoB(Object a, Object b, MappingContext context) {
        getMapperFor(a, b).mapAtoB(a, b, context);
    }
    
    @Override
    public void mapBtoA(Object b, Object a, MappingContext context) {
        getMapperFor(a, b).mapBtoA(b, a, context);
    }
    
    private MapperKey createMapperKey(Object a, Object b) {
        Type<? extends Object> aType = TypeFactory.valueOf(a.getClass());
        Type<? extends Object> bType = TypeFactory.valueOf(b.getClass());
        if (aType.getRawType().isAssignableFrom(this.getAType().getRawType())) {
            aType = this.getAType();
        }
        if (bType.getRawType().isAssignableFrom(this.getBType().getRawType())) {
            bType = this.getBType();
        }
        return new MapperKey(aType, bType);
    }
    
    private Mapper<Object, Object> getMapperFor(Object a, Object b) {
        MapperKey mapperKey = createMapperKey(a, b);
        Mapper<Object, Object> mapper = mappersCache.get(mapperKey);
        if (mapper != null) {
            return mapper;
        }
        mapper = findMapperFor(mapperKey);
        if (mapper == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("No matching Mapper found for %s <-> %s", mapperKey.getAType(), mapperKey.getBType()));
            
            sb.append("\n");
            for (Mapper<?, ?> mapper2 : mappersRegistry) {
                sb.append(String.format("\t Existing Mapper: %s <-> %s", mapper2.getAType(), mapper2.getBType()));
                sb.append("\n");
                sb.append("\t");
                sb.append(String.format("Matching-A: %s; Matching-B: %s",
                        mapper2.getAType().isAssignableFrom(mapperKey.getAType()),
                        mapper2.getBType().isAssignableFrom(mapperKey.getBType())));
                sb.append("\n");
            }
            throw new IllegalStateException(sb.toString());
        }
        mappersCache.put(mapperKey, mapper);
        return mapper;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Mapper<Object, Object> findMapperFor(MapperKey mapperKey) {
        for (Mapper mapper : mappersRegistry) {
            if ((mapper.getAType().isAssignableFrom(mapperKey.getAType())
                    && mapper.getBType().isAssignableFrom(mapperKey.getBType()))) {
                return mapper;
            } else if ((mapper.getBType().isAssignableFrom(mapperKey.getAType())
                    && mapper.getAType().isAssignableFrom(mapperKey.getBType()))) {
                return ReversedMapper.reverse(mapper);
            }
        }
        // Generics could gone lost during mapping. So check again without generics
        for (Mapper mapper : mappersRegistry) {
            if ((mapper.getAType().getRawType().isAssignableFrom(mapperKey.getAType().getRawType())
                    && mapper.getBType().getRawType().isAssignableFrom(mapperKey.getBType().getRawType()))) {
                return mapper;
            } else if ((mapper.getBType().getRawType().isAssignableFrom(mapperKey.getAType().getRawType())
                    && mapper.getAType().getRawType().isAssignableFrom(mapperKey.getBType().getRawType()))) {
                return ReversedMapper.reverse(mapper);
            }
        }
        return null;
    }
    
    public void setUsedMappers(Mapper<Object, Object>[] usedMappers) {
        throw new IllegalStateException("Should not be called for a user MultipleMapperWrapper.");
    }
    
    @Override
    public Mapper<Object, Object>[] getUsedMappers() {
        Set<Mapper<Object, Object>> usedMappers = new HashSet<Mapper<Object, Object>>();
        for (Mapper<Object, Object> mapper : mappersRegistry) {
            if (mapper instanceof GeneratedMapperBase) {
                GeneratedMapperBase generatedMapper = (GeneratedMapperBase) mapper;
                usedMappers.addAll(Arrays.asList(generatedMapper.getUsedMappers()));
            }
        }
        return usedMappers.toArray(new Mapper[usedMappers.size()]);
    }
    
    public Collection<Mapper<Object, Object>> getMappersRegistry() {
        return Collections.unmodifiableCollection(mappersRegistry);
    }
    
}