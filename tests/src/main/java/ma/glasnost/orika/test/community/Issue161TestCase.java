package ma.glasnost.orika.test.community;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Test;

public class Issue161TestCase {

	public static class Optional<T> {
	}

	public static class Range<C extends Comparable<C>> {
	}

	public static class SelfReferencingGenericType<T extends SelfReferencingGenericType<T>> {
		public Optional<Range<String>> getOptionalStringRange() {
			return null;
		}
	}

	public static class Foo extends SelfReferencingGenericType<Foo> {
	}

	@Test
	public void mapSelfReferencingGenericType() {
		MapperFactory factory = new DefaultMapperFactory.Builder().build();
		factory.classMap(Foo.class, String.class).byDefault().register();
	}
}
