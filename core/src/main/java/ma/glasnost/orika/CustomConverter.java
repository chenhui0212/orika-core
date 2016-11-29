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
package ma.glasnost.orika;

import java.lang.reflect.ParameterizedType;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.builtin.CloneableConverter;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;

import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.util.ClassHelper;

/**
 * CustomConverterBase provides a utility base for creating customized converters,
 * which determines type parameters automatically. <br><br>
 * 
 * It is recommend to extend this class to create your own custom converters.
 * 
 * @author elaatifi@gmail.com
 * @author matt.deboer@gmail.com
 *
 * @param <S> the source type
 * @param <D> the destination type
 */
public abstract class CustomConverter<S, D> implements ma.glasnost.orika.Converter<S, D> {
    
    protected Type<S> sourceType;
    protected Type<D> destinationType;
    protected MapperFacade mapperFacade;

    public CustomConverter() {
//        java.lang.reflect.Type genericSuperclass = getClass().getGenericSuperclass();
//        while (genericSuperclass instanceof Class) {
//        	genericSuperclass = ((Class<?>)genericSuperclass).getGenericSuperclass();
//        }
//        if (genericSuperclass != null && genericSuperclass instanceof ParameterizedType) {
//            ParameterizedType superType = (ParameterizedType)genericSuperclass;
//            sourceType = TypeFactory.valueOf(superType.getActualTypeArguments()[0]);
//            destinationType = TypeFactory.valueOf(superType.getActualTypeArguments()[1]);
//        } else {
//            throw new IllegalStateException("When you subclass CustomConverter S and D type-parameters are required.");
//        }
        /* Now the correct types are determined, but....
         * BidirectionalConverter (and others) extends CustomConverter using <Object, Object>. This enables a call to convert with any type of source and destination.
         * At runtime the arguments passed to convert are used to decide to call either convertTo or convertFrom.
         * This fails because the source and destination (now correctly determined) are both Object.
         * I realy don't like the <Object, Object> construct and the way this is used in the class hierarchy.
         * Generics are only in the way in this design.
         * Consider a redesign: use generics more transparently in a way that helps and don't try to dynamically overload convert at runtime.
         * I think encapsulation and the decoration pattern will help, each decorator for a converter is responsible for only one aspect such as bidirectionality.
         * 
         */
        if (this instanceof BidirectionalConverter) {
            sourceType = (Type<S>) TypeFactory.valueOf(ClassHelper.findParameterClassOrObjectClass(0,getClass(),BidirectionalConverter.class));
            destinationType = (Type<D>) TypeFactory.valueOf(ClassHelper.findParameterClassOrObjectClass(1,getClass(),BidirectionalConverter.class));
        } else if (this instanceof CloneableConverter) {
            sourceType = (Type<S>) TypeFactory.valueOf(Object.class);
            destinationType = (Type<D>) TypeFactory.valueOf(Object.class);
        } else if (this instanceof PassThroughConverter) {
            sourceType = (Type<S>) TypeFactory.valueOf(Object.class);
            destinationType = (Type<D>) TypeFactory.valueOf(Object.class);
        } else {
            sourceType = (Type<S>) TypeFactory.valueOf(ClassHelper.findParameterClassOrObjectClass(0,getClass(),CustomConverter.class));
            destinationType = (Type<D>) TypeFactory.valueOf(ClassHelper.findParameterClassOrObjectClass(1,getClass(),CustomConverter.class));
        }
        if (sourceType==null || destinationType == null) {
            throw new IllegalStateException("When you subclass CustomConverter S and D type-parameters are required.");
        }
    }
    
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return this.sourceType.equals(sourceType) && destinationType.equals(this.destinationType);
    }
    
    public void setMapperFacade(MapperFacade mapper) {
        this.mapperFacade = mapper;
    }
    
    public String toString() {
    	String subClass = getClass().equals(CustomConverter.class) || getClass().isAnonymousClass() ? "" : "("+getClass().getSimpleName()+")";
    	String srcName = TypeFactory.nameOf(sourceType, destinationType);
    	String dstName = TypeFactory.nameOf(destinationType, sourceType);
    	return CustomConverter.class.getSimpleName()+subClass+"<"+srcName + ", " + dstName+">";
    }
    
    public Type<S> getAType() {
        return sourceType;
    }
    
    public Type<D> getBType() {
        return destinationType;
    }
    
    public boolean equals(Object other) {
        return other != null && getClass().equals(other.getClass());
    }
    
    public int hashCode() {
        return getClass().hashCode();
    }
}
