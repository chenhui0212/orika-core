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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

/**
 * DefaultConstructorObjectFactory is used for types which should be instantiated
 * using their default constructor.
 * 
 * @author matt.deboer@gmail.com
 *
 */
public class DefaultConstructorObjectFactory<T> implements ObjectFactory<T> {

    private final String description;
    private Constructor<T> constructor;
    
    public DefaultConstructorObjectFactory(Class<T> type) {
        this.constructor = getDefaultConstructor(type);
        constructor.setAccessible(true);
        this.description = getClass().getSimpleName() + "<" + type.getSimpleName() + ">";
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getDefaultConstructor(Class<T> type) {
        for (Constructor<?> c : type.getDeclaredConstructors()) {
            if (c.getParameterTypes().length ==  0) {
                return (Constructor<T>) c;
            }
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see ma.glasnost.orika.ObjectFactory#create(java.lang.Object, ma.glasnost.orika.MappingContext)
     */
    @Override
    public T create(Object source, MappingContext mappingContext) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public String toString() {
    	return description;
    }
    
}
