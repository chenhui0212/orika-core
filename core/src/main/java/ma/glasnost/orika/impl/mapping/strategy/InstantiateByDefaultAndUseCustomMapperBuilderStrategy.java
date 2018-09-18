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

import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * InstantiateByDefaultAndUseCustomMapperStrategy uses a custom mapper and creates instances
 * using the default constructor for the destination type.
 */
public class InstantiateByDefaultAndUseCustomMapperBuilderStrategy extends UseCustomMapperStrategy {

    /**
     * Creates a new instance of InstantiateByDefaultAndUseCustomMapperStrategy
     *
     * @param sourceType
     * @param destinationType
     * @param customMapper
     * @param unenhancer
     */
    public InstantiateByDefaultAndUseCustomMapperBuilderStrategy(Type<Object> sourceType, Type<Object> destinationType, Mapper<Object, Object> customMapper, UnenhanceStrategy unenhancer) {
        super(sourceType, destinationType, customMapper, unenhancer);
    }

    /**
     * 实例化 gRPC 类的内部类 Builder
     */
    protected Object getInstance(Object sourceObject, Object destinationObject, MappingContext context) {
        try {
            // 通过调用 gRPC 类的 newBuilder 方法，生成其内部类 Builder 的实例
            Method newBuilder = destinationType.getRawType().getEnclosingClass().getMethod("newBuilder");
            return newBuilder.invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object afterMap(Object destinationObject) {
        try {
            // 通过调用 Builder 的 build 方法，生成 gRPC 类实例
            Method build = destinationType.getRawType().getMethod("build");
            return build.invoke(destinationObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
