package ma.glasnost.orika.test.objectfactory;

import static org.junit.Assert.*;
import org.junit.Test;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.TypeFactory;

public class MultipleObjectFactoryTest {
	public static class Base {
	}

	public static class Sub1 extends Base {
	}

	public static class Sub2 extends Base {
	}

	@Test
	public void orikaTest() {
		MapperFactory factory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy())
				.build();

		factory.registerObjectFactory(new CustomFactory<Sub1>(Sub1.class), TypeFactory.<Sub1>valueOf(Sub1.class));
		factory.registerObjectFactory(new CustomFactory<Sub2>(Sub2.class), TypeFactory.<Sub2>valueOf(Sub2.class));
		factory.registerObjectFactory(new CustomFactory<Base>(Base.class), TypeFactory.<Base>valueOf(Base.class));

		MapperFacade mapperFacade = factory.getMapperFacade();
		Base mapped = mapperFacade.map(new Object(), Base.class);
		assertEquals("returned instance is not Base", Base.class, mapped.getClass());
	}
}
