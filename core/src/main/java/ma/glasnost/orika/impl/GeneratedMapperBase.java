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

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

public abstract class GeneratedMapperBase extends GeneratedObjectBase implements Mapper<Object, Object> {
    
    /**
     * Returns true if <code>usedMapper</code> is found within the usedMapper hierarchy
     * of <code>ofMapper</code>.
     * 
     * @param usedMapper the mapper to look for in the hierarchy
     * @param ofMapper the mapper whose usedMapper hierarchy is searched
     * @return
     */
    public static boolean isUsedMapperOf(Mapper<Object, Object> usedMapper, Mapper<Object, Object> ofMapper) {
        return (ofMapper instanceof GeneratedMapperBase && ((GeneratedMapperBase) ofMapper).uses(usedMapper));
    }
    
    protected Mapper<Object, Object> customMapper;
    private Mapper<Object, Object>[] usedMappers;
    private Type<Object> aType;
    private Type<Object> bType;
    private Boolean favorsExtension;
    
    public Type<Object> getAType() {
        return aType;
    }
    
    public Type<Object> getBType() {
        return bType;
    }
    
    @SuppressWarnings("unchecked")
    public void setAType(Type<?> aType) {
        this.aType = (Type<Object>) aType;
    }
    
    @SuppressWarnings("unchecked")
    public void setBType(Type<?> bType) {
        this.bType = (Type<Object>) bType;
    }
    
    public void setCustomMapper(Mapper<Object, Object> customMapper) {
        this.customMapper = customMapper;
        this.customMapper.setMapperFacade(mapperFacade);
    }
    
    public Mapper<Object, Object>[] getUsedMappers() {
        return usedMappers;
    }
    
    /**
     * Returns true if this mapper (or any of it's usedMappers, recursively)
     * makes use of the specified mapper.
     * 
     * @param mapper
     * @return
     */
    public boolean uses(Mapper<Object, Object> mapper) {
        if (usedMappers != null) {
            for (Mapper<Object, Object> um : usedMappers) {
                if (um.equals(mapper)) {
                    return true;
                } else if (um instanceof GeneratedMapperBase && ((GeneratedMapperBase) um).uses(mapper)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setUsedMappers(Mapper<Object, Object>[] usedMappers) {
        this.usedMappers = usedMappers;
    }
    
    public void setUsedTypes(Type<Object>[] types) {
        this.usedTypes = types;
    }
    
    public void setUsedConverters(Converter<Object, Object>[] usedConverters) {
        this.usedConverters = usedConverters;
    }
    
    public void setUsedMapperFacades(BoundMapperFacade<Object, Object>[] usedMapperFacades) {
        this.usedMapperFacades = usedMapperFacades;
    }
    
    public void mapAtoB(Object a, Object b, MappingContext context) {
        if (usedMappers == null) {
            return;
        }
        for (Mapper<Object, Object> mapper : usedMappers) {
            mapper.mapAtoB(a, b, context);
        }
    }
    
    public void setFavorsExtension(Boolean favorsExtension) {
        this.favorsExtension = favorsExtension;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ma.glasnost.orika.Mapper#isAbstract()
     */
    public Boolean favorsExtension() {
        return favorsExtension;
    }
    
    public void mapBtoA(Object b, Object a, MappingContext context) {
        if (usedMappers == null) {
            return;
        }
        for (Mapper<Object, Object> mapper : usedMappers) {
            mapper.mapBtoA(b, a, context);
        }
    }
    
    public String toString() {
        String aTypeName = TypeFactory.nameOf(aType, bType);
        String bTypeName = TypeFactory.nameOf(bType, aType);
        
        return "GeneratedMapper<" + aTypeName + ", " + bTypeName + "> {" + "usedConverters: " + Arrays.toString(usedConverters) + ", "
                + "usedMappers: " + Arrays.toString(usedMappers) + ", " + "usedMapperFacades: " + Arrays.toString(usedMapperFacades) + ", "
                + "usedTypes: " + Arrays.toString(usedTypes) + " }";
    }
}