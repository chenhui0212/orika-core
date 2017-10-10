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
package ma.glasnost.orika.test.metadata;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.CaseInsensitiveClassMapBuilder;
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author matt.deboer@gmail.com
 * 
 */
public class CaseInsensitiveClassMapBuilderTest {
    
    @Test
    public void byDefault() {
        
        MapperFactory factory = new DefaultMapperFactory.Builder()
            .classMapBuilderFactory(new CaseInsensitiveClassMapBuilder.Factory()).build();
        
        factory.classMap(Source.class, Destination.class).byDefault().register();

        MapperFacade mapper = factory.getMapperFacade();
        
        Source s = new Source();
        s.lastNAME = "Smith";
        s.firstName = "Joe";
        s.age = 25;
        
        Destination d = mapper.map(s, Destination.class);
        /*
         * Check that properties we expect were mapped
         */
        assertEquals(s.firstName, d.fIrStNaMe);
        assertEquals(s.lastNAME, d.LastName);
        assertEquals(s.age, d.AGE);
    }
    
    @Test
    public void fieldMap_withoutNestedProperties() {
        
        MapperFactory factory = new DefaultMapperFactory.Builder()
            .classMapBuilderFactory(new CaseInsensitiveClassMapBuilder.Factory()).build();
        
        factory.classMap(Source.class, Destination.class)
            .field("FIRSTname", "FIRSTname")
            .field("lastNAME", "lastNAME")
            .field("aGE", "aGE")
            .register();
        

        MapperFacade mapper = factory.getMapperFacade();
        
        Source s = new Source();
        s.lastNAME = "Smith";
        s.firstName = "Joe";
        s.age = 25;
        
        Destination d = mapper.map(s, Destination.class);
        /*
         * Check that properties we expect were mapped
         */
        assertEquals(s.firstName, d.fIrStNaMe);
        assertEquals(s.lastNAME, d.LastName);
        assertEquals(s.age, d.AGE);
    }
    
    @Test
    public void fieldMap_withNestedProperties() {
        
        MapperFactory factory = new DefaultMapperFactory.Builder()
            .classMapBuilderFactory(new CaseInsensitiveClassMapBuilder.Factory()).build();
        
        factory.classMap(Source.class, Destination.class)
            .field("FIRSTname", "FIRSTname")
            .field("lastNAME", "lastNAME")
            .field("aGE", "aGE")
            .field("name.first", "name.first")
            .field("name.last", "name.last")
            .register();
        

        MapperFacade mapper = factory.getMapperFacade();
        
        Source s = new Source();
        s.lastNAME = "Smith";
        s.firstName = "Joe";
        s.age = 25;
        s.NaMe = new SourceName();
        s.NaMe.FIRST = "Joe";
        s.NaMe.LAST = "Smith";
        
        Destination d = mapper.map(s, Destination.class);
        /*
         * Check that properties we expect were mapped
         */
        assertEquals(s.firstName, d.fIrStNaMe);
        assertEquals(s.lastNAME, d.LastName);
        assertEquals(s.age, d.AGE);
        assertEquals(s.NaMe.FIRST, d.nAme.fIrSt);
        assertEquals(s.NaMe.LAST, d.nAme.LaSt);
    }

    @Test // issue 236
    public void maps_date_to_xml_gregorian_calendar_by_default_without_mapping_its_fields() throws Exception {
        MapperFactory factory = new DefaultMapperFactory.Builder()
                .classMapBuilderFactory(new CaseInsensitiveClassMapBuilder.Factory())
                .build();
        
        factory.classMap(SourceDate.class, DestinationDate.class)
                .byDefault()
                .register();

        MapperFacade mapper = factory.getMapperFacade();

        final SourceDate source = new SourceDate();
        source.date = new Date();

        DestinationDate destination = mapper.map(source, DestinationDate.class);

        assertEquals(source.date, destination.date.toGregorianCalendar().getTime());
    }

    public static class Source {
        public String lastNAME;
        public String firstName;
        public Integer age;
        public SourceName NaMe;
    }

    public static class SourceName {
        public String FIRST;
        public String LAST;
    }

    public static class Destination {
        public String LastName;
        public String fIrStNaMe;
        public Integer AGE;
        public DestinationName nAme;
    }

    public static class DestinationName {
        public String fIrSt;
        public String LaSt;
    }

    public static class SourceDate {
        public Date date;
    }

    public static class DestinationDate {
        public XMLGregorianCalendar date;
    }

}
