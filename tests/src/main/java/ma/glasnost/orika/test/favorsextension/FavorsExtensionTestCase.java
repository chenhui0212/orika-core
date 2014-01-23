package ma.glasnost.orika.test.favorsextension;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.test.MappingUtil;

import org.junit.Assert;
import org.junit.Test;

public class FavorsExtensionTestCase {

	
	@Test
	public void favorsExtension() throws Throwable {
		
		MapperFactory factory = MappingUtil.getMapperFactory();
		factory.classMap(Animal.class, AnimalDto.class)
			.field("category", "type")
			.field("name", "qualifier")
			.favorExtension(true)
			.register();
		
		MapperFacade mapper = factory.getMapperFacade();
		
		Bird src = new Bird();
		src.category = "falcon";
		src.name = "Falcor";
		src.wingSpanInCm = 120;
		
		BirdDto dest = mapper.map(src, BirdDto.class);
		Assert.assertEquals(src.category, dest.type);
		Assert.assertEquals(src.name, dest.qualifier);
		Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);
		
		Cat cat = new Cat();
		cat.category = "tiger";
		cat.name = "Tigger";
		cat.striped = true;
		
		CatDto dest2 = mapper.map(cat, CatDto.class);
		Assert.assertEquals(cat.category, dest2.type);
		Assert.assertEquals(cat.name, dest2.qualifier);
		Assert.assertEquals(cat.striped, dest2.striped);
	}
	
	@Test
	public void favorsExtensionGlobally() throws Throwable {
		
		MapperFactory factory = new DefaultMapperFactory.Builder().favorExtension(true).build();
		factory.classMap(Animal.class, AnimalDto.class)
			.field("category", "type")
			.field("name", "qualifier")
			.register();
		
		MapperFacade mapper = factory.getMapperFacade();
		
		Bird src = new Bird();
		src.category = "falcon";
		src.name = "Falcor";
		src.wingSpanInCm = 120;
		
		BirdDto dest = mapper.map(src, BirdDto.class);
		Assert.assertEquals(src.category, dest.type);
		Assert.assertEquals(src.name, dest.qualifier);
		Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);
		
		Cat cat = new Cat();
		cat.category = "tiger";
		cat.name = "Tigger";
		cat.striped = true;
		
		CatDto dest2 = mapper.map(cat, CatDto.class);
		Assert.assertEquals(cat.category, dest2.type);
		Assert.assertEquals(cat.name, dest2.qualifier);
		Assert.assertEquals(cat.striped, dest2.striped);
	}
	
	@Test
	public void withoutFavorsExtension() throws Throwable {
		
		/*
		 * Note: without using 'favorsExtension', the result is that Orika
		 * uses the Animal <-> AnimalDto mapping that was registered without
		 * generating a dynamic mapping for the downstream classes
		 */
		MapperFactory factory = MappingUtil.getMapperFactory();
		factory.classMap(Animal.class, AnimalDto.class)
			.field("category", "type")
			.field("name", "qualifier")
			.register();
		
		MapperFacade mapper = factory.getMapperFacade();
		
		Bird src = new Bird();
		src.category = "falcon";
		src.name = "Falcor";
		src.wingSpanInCm = 120;
		
		BirdDto dest = mapper.map(src, BirdDto.class);
		Assert.assertEquals(src.category, dest.type);
		Assert.assertEquals(src.name, dest.qualifier);
		Assert.assertNotEquals(src.wingSpanInCm, dest.wingSpanInCm);
		
		Cat cat = new Cat();
		cat.category = "tiger";
		cat.name = "Tigger";
		cat.striped = true;
		
		CatDto dest2 = mapper.map(cat, CatDto.class);
		Assert.assertEquals(cat.category, dest2.type);
		Assert.assertEquals(cat.name, dest2.qualifier);
		Assert.assertNotEquals(cat.striped, dest2.striped);
		
		/*
		 * But after we explicitly register the mapping, and
		 * declare that it should 'use' the Animal to AnimalDto
		 * mapper, it works as expected
		 */
		factory.classMap(Bird.class, BirdDto.class)
			.byDefault()
			.use(Animal.class, AnimalDto.class)
			.register();
		
		dest = mapper.map(src, BirdDto.class);
		Assert.assertEquals(src.category, dest.type);
		Assert.assertEquals(src.name, dest.qualifier);
		Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);	
		
	}
	
	public static class Animal {
		public String category;
		public String name;
	}
	
	public static class Bird extends Animal {
		public int wingSpanInCm;
	}
	
	public static class Cat extends Animal {
		public boolean striped;
	}
	
	public static class AnimalDto {
		public String type;
		public String qualifier;
	}
	
	public static class BirdDto extends AnimalDto {
		public int wingSpanInCm;
	}
	
	public static class CatDto extends AnimalDto {
		public boolean striped;
	}
	
}
