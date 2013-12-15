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

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * BidirectionalConverter which describes itself as builtin
 *
 * @param <C>
 * @param <D>
 */
abstract class BuiltinBidirectionalConverter<C, D> extends
		BidirectionalConverter<C, D> {

	private final String description;
	private volatile Reversed<D, C> reversed;
	
	public BuiltinBidirectionalConverter() {
	    super();
	    String srcName = TypeFactory.nameOf(sourceType, destinationType);
	    String dstName = TypeFactory.nameOf(destinationType, sourceType); 
	    description = "builtin:" + getClass().getSimpleName() + "<"
        + srcName + ", " + dstName + ">";
	}
	
	public String toString() {
		return description;
	}
	
	public BidirectionalConverter<D, C> reverse() {
	    if (reversed == null) {
	        synchronized(this) {
	            if (reversed == null) {
	                reversed = new Reversed<D, C>(this);
	            }
	        }
	    }
	    return reversed;
	}
	
	public static class Reversed<D, C> extends BidirectionalConverter.Reversed<D, C> {

	    private final String description;
	    
        public Reversed(BidirectionalConverter<C, D> bidi) {
            super(bidi);
            String srcName = TypeFactory.nameOf(getAType(), getBType());
            String dstName = TypeFactory.nameOf(getBType(), getAType());
            description = "builtin:reversed:" + bidi.getClass().getSimpleName() + "<"
            + srcName + ", " + dstName + ">";
        }
	    
        public String toString() {
            return description;
        }
	}
}
