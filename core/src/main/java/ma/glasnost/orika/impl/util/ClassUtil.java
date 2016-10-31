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

package ma.glasnost.orika.impl.util;

public final class ClassUtil {
    
    private static final String CGLIB_ID = "$$EnhancerByCGLIB$$";
    private static final String JAVASSIST_PACKAGE = "org.javassist.tmp.";
    private static final String JAVASSIST_NAME = "_$$_javassist_";

    private ClassUtil() {
        
    }

	/**
     * Verifies whether the passed type has a static valueOf method available for
     * converting a String into an instance of the type.<br>
     * Note that this method will also return true for primitive types whose
     * corresponding wrapper types have a static valueOf method.
     * 
     * @param type
     * @return
     */
    public static boolean isConvertibleFromString(Class<?> type) {
    	
    	if (type.isPrimitive()) {
    		type = getWrapperType(type);
    	}
    	
    	try {
			type.getMethod("valueOf", String.class);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		}
    }
    
    /**
     * Returns the corresponding wrapper type for the given primitive,
     * or null if the type is not primitive.
     * 
     * @param primitiveType
     * @return
     */
    public static Class<?> getWrapperType(Class<?> primitiveType) {
		if (boolean.class.equals(primitiveType)) {
			return Boolean.class;
		} else if (byte.class.equals(primitiveType)) {
			return Byte.class;
		} else if (char.class.equals(primitiveType)) {
			return Character.class;
		} else if (short.class.equals(primitiveType)) {
			return Short.class;
		} else if (int.class.equals(primitiveType)) {
			return Integer.class;
		} else if (long.class.equals(primitiveType)) {
			return Long.class;
		} else if (float.class.equals(primitiveType)) {
			return Float.class;
		} else if (double.class.equals(primitiveType)) {
			return Double.class;
		} else {
			return null;
		}
    }
    
    /**
     * Returns the corresponding primitive type for the given primitive wrapper,
     * or null if the type is not a primitive wrapper.
     * 
     * @param wrapperType
     * @return the corresponding primitive type
     */
    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
		if (Boolean.class.equals(wrapperType)) {
			return Boolean.TYPE;
		} else if (Byte.class.equals(wrapperType)) {
			return Byte.TYPE;
		} else if (Character.class.equals(wrapperType)) {
			return Character.TYPE;
		} else if (Short.class.equals(wrapperType)) {
			return Short.TYPE;
		} else if (Integer.class.equals(wrapperType)) {
			return Integer.TYPE;
		} else if (Long.class.equals(wrapperType)) {
			return Long.TYPE;
		} else if (Float.class.equals(wrapperType)) {
			return Float.TYPE;
		} else if (Double.class.equals(wrapperType)) {
			return Double.TYPE;
		} else {
			return null;
		}
    }
    
    public static boolean isProxy(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        final String className = clazz.getName();
        return className.contains(CGLIB_ID) || className.startsWith(JAVASSIST_PACKAGE) || className.contains(JAVASSIST_NAME);
    }

}
