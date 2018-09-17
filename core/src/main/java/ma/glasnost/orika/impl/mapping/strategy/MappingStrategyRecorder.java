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

package ma.glasnost.orika.impl.mapping.strategy;

import com.google.protobuf.GeneratedMessageV3;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.MappingStrategy.Key;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.ReversedMapper;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;

/**
 * MappingStrategyRecorder is used to record the important details regarding the
 * branch logic and component lookups performed for a given set of input types.<br>
 * After recording these details, it can be used to generate an appropriate
 * MappingStrategy instance which can be cached and reused for that particular
 * set of inputs.
 * 
 * @author matt.deboer@gmail.com
 * 
 */
public class MappingStrategyRecorder {
    
    private boolean copyByReference;
    private boolean mapReverse;
    private boolean instantiate;
    
    private Mapper<Object, Object> resolvedMapper;
    private ObjectFactory<Object> resolvedObjectFactory;
    private Converter<Object, Object> resolvedConverter;
    private Type<Object> resolvedSourceType;
    private Type<Object> resolvedDestinationType;
    private MappingStrategy resolvedStrategy;
    
    private final UnenhanceStrategy unenhanceStrategy;
    private final Key key;
    
    /**
     * @param key
     * @param unenhanceStrategy
     */
    public MappingStrategyRecorder(final Key key, final UnenhanceStrategy unenhanceStrategy) {
        this.unenhanceStrategy = unenhanceStrategy;
        this.key = key;
    }
    
    /**
     * @return true of the strategy should instantiate new instances
     */
    public boolean isInstantiate() {
        return instantiate;
    }
    
    /**
     * Sets whether the strategy should create new instances
     * 
     * @param instantiate
     */
    public void setInstantiate(final boolean instantiate) {
        this.instantiate = instantiate;
    }
    
    /**
     * @return the converter resolved for this strategy
     */
    public Converter<Object, Object> getResolvedConverter() {
        return resolvedConverter;
    }
    
    /**
     * Set the converter that should be used by the MappingStrategy
     * 
     * @param resolvedConverter
     */
    public void setResolvedConverter(final Converter<Object, Object> resolvedConverter) {
        this.resolvedConverter = resolvedConverter;
    }
    
    /**
     * @return the source type for the strategy
     */
    public Type<?> getResolvedSourceType() {
        return resolvedSourceType;
    }
    
    /**
     * Sets the source type that should be used with the strategy
     * 
     * @param resolvedSourceType
     */
    @SuppressWarnings("unchecked")
    public void setResolvedSourceType(final Type<?> resolvedSourceType) {
        this.resolvedSourceType = (Type<Object>) resolvedSourceType;
    }
    
    /**
     * @return the destination type for the strategy
     */
    public Type<?> getResolvedDestinationType() {
        return resolvedDestinationType;
    }
    
    /**
     * Set the destination type to be used with the strategy
     * 
     * @param resolvedDestinationType
     */
    @SuppressWarnings("unchecked")
    public void setResolvedDestinationType(final Type<?> resolvedDestinationType) {
        this.resolvedDestinationType = (Type<Object>) resolvedDestinationType;
    }
    
    /**
     * @return true if the strategy should use copy-by-reference
     */
    public boolean isCopyByReference() {
        return copyByReference;
    }
    
    /**
     * Set whether the strategy should use copy-by-reference
     * 
     * @param copyByReference
     */
    public void setCopyByReference(final boolean copyByReference) {
        this.copyByReference = copyByReference;
    }
    
    /**
     * @return true if the strategy should map in reverse
     */
    public boolean isMapReverse() {
        return mapReverse;
    }
    
    /**
     * Set whether this strategy should map in reverse
     * 
     * @param mapReverse
     */
    public void setMapReverse(final boolean mapReverse) {
        this.mapReverse = mapReverse;
    }
    
    /**
     * @return the mapper resolved for the strategy
     */
    public Mapper<Object, Object> getResolvedMapper() {
        return resolvedMapper;
    }
    
    /**
     * Sets the mapper to be used with the strategy
     * 
     * @param resolvedMapper
     */
    public void setResolvedMapper(final Mapper<Object, Object> resolvedMapper) {
        this.resolvedMapper = resolvedMapper;
    }
    
    /**
     * @return the ObjectFactory to use for the strategy
     */
    public ObjectFactory<Object> getResolvedObjectFactory() {
        return resolvedObjectFactory;
    }
    
    /**
     * Set the ObjectFactory to use for the strategy
     * 
     * @param resolvedObjectFactory
     */
    @SuppressWarnings("unchecked")
    public void setResolvedObjectFactory(final ObjectFactory<?> resolvedObjectFactory) {
        this.resolvedObjectFactory = (ObjectFactory<Object>) resolvedObjectFactory;
    }
    
    /**
     * @return a new instance of the MappingStrategy which can "playback" the
     *         route taken to map a given set of inputs.
     */
    public synchronized MappingStrategy playback() {
        
        UnenhanceStrategy unenhanceStrategy;
        unenhanceStrategy = this.unenhanceStrategy;
        
        if (copyByReference) {
            resolvedStrategy = CopyByReferenceStrategy.getInstance();
        } else if (resolvedConverter != null) {
            resolvedStrategy = new UseConverterStrategy(resolvedSourceType, resolvedDestinationType, resolvedConverter, unenhanceStrategy);
        } else {
            
            if (mapReverse) {
                resolvedMapper = ReversedMapper.reverse(resolvedMapper);
            }

            // ==================== Start ====================
            // 当目标类型为 GeneratedMessageV3.Builder 子类，则使用自定义的 CustomMapper 策略
            if (GeneratedMessageV3.Builder.class.isAssignableFrom(resolvedDestinationType.getRawType())) {
                resolvedStrategy = new InstantiateByDefaultAndUseCustomMapperBuilderStrategy(resolvedSourceType, resolvedDestinationType, resolvedMapper,
                        unenhanceStrategy);
            }
            // ==================== Start ====================

            else if (resolvedObjectFactory != null) {
                resolvedStrategy = new InstantiateAndUseCustomMapperStrategy(resolvedSourceType, resolvedDestinationType, resolvedMapper,
                        resolvedObjectFactory, unenhanceStrategy);
            } else {
                resolvedStrategy = new MapExistingAndUseCustomMapperStrategy(resolvedSourceType, resolvedDestinationType, resolvedMapper,
                        unenhanceStrategy);
            }
            
        }
        return resolvedStrategy;
    }
    
    /**
     * Describes the details of the strategy chosen for this particular set of
     * inputs
     * 
     * @return a String description of this strategy suitable for logging
     */
    public String describeDetails() {
        if (resolvedStrategy == null) {
            throw new IllegalStateException("Strategy recording not complete");
        }
        StringBuilder details = new StringBuilder();
        details.append("MappingStrategy resolved and cached:")
                .append("\n\tInputs:[ sourceClass: " + key.getRawSourceType().getCanonicalName())
                .append(", sourceType: " + key.getSourceType())
                .append(", destinationType: " + key.getDestinationType())
                .append("]\n\tResolved:[ strategy: " + resolvedStrategy.getClass().getSimpleName())
                .append(", sourceType: " + getResolvedSourceType())
                .append(", destinationType: " + getResolvedDestinationType());
        if (isCopyByReference()) {
            details.append(", copyByReference?: true");
        }
        
        if (getResolvedConverter() != null) {
            details.append(", converter: " + getResolvedConverter());
        }
        
        if (getResolvedMapper() != null) {
            details.append(", mapper: " + getResolvedMapper());
            details.append(", mapReverse?: " + mapReverse);
        }
        details.append("]");
        
        return details.toString();
    }
    
    static class NoOpUnenhancer implements UnenhanceStrategy {
        
        private static final NoOpUnenhancer INSTANCE = new NoOpUnenhancer();
        
        public static UnenhanceStrategy getInstance() {
            return INSTANCE;
        }
        
        public <T> Type<T> unenhanceType(final T object, final Type<T> type) {
            throw new UnsupportedOperationException();
        }
        
        public <T> T unenhanceObject(final T object, final Type<T> type) {
            return object;
        }
        
        public String toString() {
        	return getClass().getSimpleName();
        }
    }
}
