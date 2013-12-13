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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * AbstractMappingStrategy provides base MappingStrategy functionality
 */
public abstract class AbstractMappingStrategy implements MappingStrategy {

    /**
     * The source type mapped by this strategy
     */
    protected final Type<Object> sourceType;
    /**
     * The destination type mapped by this strategy
     */
    protected final Type<Object> destinationType;
    
    /**
     * @param sourceType
     * @param destinationType
     */
    public AbstractMappingStrategy(Type<Object> sourceType, Type<Object> destinationType) {
        this.sourceType = sourceType;
        this.destinationType = destinationType;
    }
    
    public Type<Object> getAType() {
        return sourceType;
    }

    public Type<Object> getBType() {
        return destinationType;
    }
    
    public String toString() {
    	StringBuilder out = new StringBuilder(getClass().getSimpleName());
    	String srcName = TypeFactory.nameOf(sourceType, destinationType);
    	String dstName = TypeFactory.nameOf(destinationType, sourceType);
    	out.append("<").append(srcName).append(", ")
    		.append(dstName).append(">").append(" {");
    	LinkedHashMap<String, Object> members = new LinkedHashMap<String, Object>();
    	describeMembers(members);
    	String separator = "";
    	for (Entry<String, Object> member: members.entrySet()) {
    		out.append(separator).append(member.getKey()).append(": ").append(member.getValue());
    		separator = ", ";
    	}
    	out.append("}");
    	return out.toString();
    }
    
    protected abstract void describeMembers(Map<String, Object> members);
}
