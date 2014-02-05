package ma.glasnost.orika.test.community;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class StringToStringTestCase {
    @Test
    public void test() {
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();
        MapperFactory factory = builder.build();

        factory.getConverterFactory().registerConverter(new StringToStringConverter());

        MapperFacade mapperFacade = factory.getMapperFacade();

        Class1 class1 = new Class1();

        Class2 class2 = mapperFacade.map(class1, Class2.class);

        // class2.getString is "null"
        assertTrue(class2.getString() == null);
    }

    public static class Class1 {
        String string;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class Class2 {
        String string;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class StringToStringConverter extends BidirectionalConverter<String, String> {
        @Override
        public String convertTo(String source, Type<String> destinationType) {
            String result = source != null && !"".equals(source) ? source : null;
            return result;
        }

        @Override
        public String convertFrom(String source, Type<String> destinationType) {
            return convertTo(source, destinationType);
        }
    }
}
