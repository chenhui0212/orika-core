package ma.glasnost.orika.test.objectfactory;

import java.lang.reflect.Constructor;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

public class CustomFactory<T> implements ObjectFactory<T> {
	private Class<T> type;

	public CustomFactory(Class<T> type) {
		this.type = type;
	}

	public T create(Object o, MappingContext mappingContext) {
		try {
			Constructor<T> declaredConstructor = type.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			return declaredConstructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
