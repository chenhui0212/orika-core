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

package ma.glasnost.orika.converter.builtin;

import java.util.HashSet;
import java.util.Set;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * PassThroughConverter allows configuration of a number of specific types which
 * should be passed through (as-is) without creating a mapped copy.<br><br>
 * 
 * This allows you to declare your own set of types which should be treated by
 * Orika as if they were in the set of immutable types.
 * 
 * @author matt.deboer@gmail.com
 *
 */
public class PassThroughConverter extends CustomConverter<Object, Object> {

	private final Set<Type<?>> passThroughTypes = new HashSet<Type<?>>();
	private final String description;
	/**
	 * Constructs a new PassThroughConverter configured to treat the provided
	 * list of types as immutable.
	 * 
	 * @param types one or more types that should be treated as immutable
	 */
	public PassThroughConverter(java.lang.reflect.Type...types) {
		StringBuilder desc = new StringBuilder(PassThroughConverter.class.getSimpleName()+"(Copy by reference:");
		String separator = "";
		for (java.lang.reflect.Type type: types) {
			Type<?> theType = TypeFactory.valueOf(type);
			passThroughTypes.add(theType);
			desc.append(separator).append(theType);
			separator = ", ";
		}
		desc.append(")");
		description = desc.toString();
	}
	
	private boolean shouldPassThrough(Type<?> type) {
		for (Type<?> registeredType: passThroughTypes) {
			if (registeredType.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see ma.glasnost.orika.Converter#canConvert(ma.glasnost.orika.metadata.Type, ma.glasnost.orika.metadata.Type)
	 */
	public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
	    return shouldPassThrough(sourceType) && sourceType.equals(destinationType);
    }

	public Object convert(Object source, Type<? extends Object> destinationType) {
	    return source;
    }
	
	
	public String toString() {
		return description;
	}
	
	/**
	 * Extends PassThroughConverter for use as a built-in Converter 
	 */
	static class Builtin extends PassThroughConverter {
		
		private final String description;
		
		Builtin(java.lang.reflect.Type...types) {
			super(types);
			description = "builtin:" + super.toString();
		}
		
		public String toString() {
			return description;
		}
	}
}
