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
package ma.glasnost.orika.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.Type;


/**
 * A custom converter that can be extended for mapping from one type to another in both directions
 * 
 * @author matt.deboer@gmail.com
 *
 * @param <S>
 * @param <D>
 */
public abstract class BidirectionalConverter<S, D> extends CustomConverter<Object, Object> implements ma.glasnost.orika.Converter<Object, Object> {
    
	private volatile Reversed<D,S> reversed;
	
    public abstract D convertTo(S source, Type<D> destinationType);
    
    public abstract S convertFrom(D source, Type<S> destinationType);
    
    @SuppressWarnings("unchecked")
    public Object convert(Object source, Type<? extends Object> destinationType) {
        if (this.destinationType.isAssignableFrom(destinationType) || this.destinationType.isWrapperFor(destinationType) || this.destinationType.isPrimitiveFor(destinationType)) {
            return convertTo((S) source, (Type<D>) destinationType);
        } else {
            return convertFrom((D) source, (Type<S>) destinationType);
        }
    }
    
    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
    	
    	 return super.canConvert(sourceType, destinationType) ||
    			this.destinationType.isAssignableFrom(sourceType) && this.sourceType.equals(destinationType);
    }
    
    public String toString() {
    	String subClass = getClass().equals(BidirectionalConverter.class) || getClass().isAnonymousClass() ? "" : "("+getClass().getSimpleName()+")";
    	return BidirectionalConverter.class.getSimpleName()+subClass+"<"+sourceType + ", " + destinationType+">";
    }

    
    public BidirectionalConverter<D, S> reverse() {
    	if (reversed == null) {
    		synchronized(this) {
    			if (reversed == null) {
    				reversed = new Reversed<D,S>(this);
    			}
    		}
    	}
    	return reversed;
    }
    
    /**
     * Provides a reversed facade to a given converter
     *
     * @param <S>
     * @param <D>
     */
    public static class Reversed<S,D> extends BidirectionalConverter<S,D> {

    	private final BidirectionalConverter<D, S> delegate;
    	
    	public Reversed(BidirectionalConverter<D, S> bidi) {
    		super();
    		delegate = bidi;
    	}
    	
		@Override
		public D convertTo(S source, Type<D> destinationType) {
			return delegate.convertFrom(source, destinationType);
		}

		@Override
		public S convertFrom(D source, Type<S> destinationType) {
			return delegate.convertTo(source, destinationType);
		}
		
		public BidirectionalConverter<D, S> reverse() {
			return delegate;
		}

		public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
			return delegate.canConvert(sourceType, destinationType);
		}

		public void setMapperFacade(MapperFacade mapper) {
			delegate.setMapperFacade(mapper);
		}

		public Type<Object> getAType() {
			return delegate.getBType();
		}

		public Type<Object> getBType() {
			return delegate.getAType();
		}
    }
}
