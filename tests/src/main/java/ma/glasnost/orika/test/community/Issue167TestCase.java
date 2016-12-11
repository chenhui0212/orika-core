package ma.glasnost.orika.test.community;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.property.IntrospectorPropertyResolver;
import ma.glasnost.orika.property.PropertyResolver;

public class Issue167TestCase {
    
    @Test
    public void testIssue166() throws Exception {
        
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        
        // this is the test where the code was broken
        factory.classMap(ChildInterface.class, ChildInterface.class)
                .byDefault()
                .register();
        
        // validate result by mapping a ChildInterfaceImpl instance:
        MapperFacade beanMapper = factory.getMapperFacade();
        Foo foo = new Foo("test-Foo");
        ChildInterfaceImpl childInterface = new ChildInterfaceImpl();
        childInterface.getSet().add(foo);
        
        ChildInterfaceImpl newChild = beanMapper.map(childInterface, ChildInterfaceImpl.class);
        
        assertThat(newChild.getSet(), hasSize(1));
        assertThat(newChild.getSet().iterator().next(), is(not(foo))); // must be a new mapped instance...
        assertThat(newChild.getSet().iterator().next().getName(), is("test-Foo")); // with the same value.
        
    }
    
    @Test
    public void testCaseSimplified_withPropertyResolverResolvePropertyType() throws Exception {
        
        PropertyResolver propRes = new IntrospectorPropertyResolver();
        
        // readMethod = Set<Foo> ChildInterface.getSet()
        Method readMethod = ChildInterface.class.getMethod("getSet");
        @SuppressWarnings("rawtypes")
        Class<Set> rawType = Set.class;
        Class<ChildInterface> owningType = ChildInterface.class;
        Type<ChildInterface> referenceType = TypeFactory.valueOf(ChildInterface.class);
        
        // run test:
        Type<?> resolvedPropertyType = propRes.resolvePropertyType(readMethod, rawType, owningType, referenceType);
        
        // validate result
        assertThat(resolvedPropertyType.toString(), is("Set<Foo>"));
        
    }
    
    @SuppressWarnings("unused")
    public static interface BaseInterface<R> {
        // test generic interface.
    }

    public static interface ChildInterface extends BaseInterface<ChildInterface> {
        Set<Foo> getSet();
    }
    
    public static class ChildInterfaceImpl implements ChildInterface {
        private Set<Foo> set = new HashSet<Issue167TestCase.Foo>();
        
        public Set<Foo> getSet() {
            return set;
        }
    }
    
    public static class Foo {
        private String name;
        
        public Foo(String name) {
            super();
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
    }
    
}