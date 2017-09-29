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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * DateAndTimeConverters provides a set of individual converters for conversion
 * between the below listed enumeration of commonly used data/time
 * representations:
 * <ul>
 * <li>java.util.Date
 * <li>java.sql.Date
 * <li>java.sql.Time
 * <li>java.sql.Timestamp
 * <li>java.util.Calendar
 * <li>javax.xml.datatype.XMLGregorianCalendar
 * <li>java.lang.Long or long
 * </ul>
 * 
 * @author elaatifi@gmail.com
 * @author matt.deboer@gmail.com
 *
 */
public class DateAndTimeConverters {
    
    /**
     * Provides conversion between Timestamp and Calendar
     */
    public static class TimestampToCalendarConverter extends BuiltinBidirectionalConverter<Timestamp, Calendar> {
        
        @Override
        public Calendar convertTo(Timestamp source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source.getTime());
        }
        
        @Override
        public Timestamp convertFrom(Calendar source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(source.getTime().getTime());
        }
    }
    
    /**
     * Provides conversion between Time and java.sql.Date
     */
    public static class TimeToSqlDateConverter extends BuiltinBidirectionalConverter<Time, java.sql.Date> {
        
        @Override
        public java.sql.Date convertTo(Time source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(source.getTime());
        }
        
        @Override
        public Time convertFrom(java.sql.Date source, Type<Time> destinationType, MappingContext context) {
            return new Time(source.getTime());
        }
    }
    
    /**
     * Provides conversion between Timestamp and Time
     */
    public static class TimestampToTimeConverter extends BuiltinBidirectionalConverter<Timestamp, Time> {
        
        @Override
        public Time convertTo(Timestamp source, Type<Time> destinationType, MappingContext context) {
            return new Time(source.getTime());
        }
        
        @Override
        public Timestamp convertFrom(Time source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(source.getTime());
        }
    }
    
    /**
     * Provides conversion between Timestamp and java.sql.Date
     */
    public static class TimestampToSqlDateConverter extends BuiltinBidirectionalConverter<Timestamp, java.sql.Date> {
        
        @Override
        public java.sql.Date convertTo(Timestamp source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(source.getTime());
        }
        
        @Override
        public Timestamp convertFrom(java.sql.Date source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(source.getTime());
        }
    }
    
    /**
     * Provides conversion between Date and Calendar
     */
    public static class DateToCalendarConverter extends BuiltinBidirectionalConverter<Date, Calendar> {
        
        @Override
        public Calendar convertTo(Date source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source);
        }
        
        @Override
        public Date convertFrom(Calendar source, Type<Date> destinationType, MappingContext context) {
            return toDate(source);
        }
    }
    
    /**
     * Provides conversion between Date and java.sql.Date
     */
    public static class DateToSqlDateConverter extends BuiltinBidirectionalConverter<Date, java.sql.Date> {
        
        @Override
        public java.sql.Date convertTo(Date source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(source.getTime());
        }
        
        @Override
        public Date convertFrom(java.sql.Date source, Type<Date> destinationType, MappingContext context) {
            return new Date(source.getTime());
        }
    }
    
    /**
     * Provides conversion between Date and Time
     */
    public static class DateToTimeConverter extends BuiltinBidirectionalConverter<Date, Time> {
        
        @Override
        public Time convertTo(Date source, Type<Time> destinationType, MappingContext context) {
            return new Time(source.getTime());
        }
        
        @Override
        public Date convertFrom(Time source, Type<Date> destinationType, MappingContext context) {
            return new Date(source.getTime());
        }
    }
    
    /**
     * Provides conversion between XMLGregorianCalendar and java.sql.Date
     */
    public static class XmlGregorianCalendarToSqlDateConverter extends BuiltinBidirectionalConverter<XMLGregorianCalendar, java.sql.Date> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public java.sql.Date convertTo(XMLGregorianCalendar source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(toLong(source));
        }
        
        @Override
        public XMLGregorianCalendar convertFrom(java.sql.Date source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source.getTime(), factory);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
        
    }
    
    /**
     * Provides conversion between XMLGregorianCalendar and Time
     */
    public static class XmlGregorianCalendarToTimeConverter extends BuiltinBidirectionalConverter<XMLGregorianCalendar, Time> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public Time convertTo(XMLGregorianCalendar source, Type<Time> destinationType, MappingContext context) {
            return new Time(toLong(source));
        }
        
        @Override
        public XMLGregorianCalendar convertFrom(Time source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source.getTime(), factory);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Calendar and java.sql.Date
     */
    public static class CalendarToSqlDateConverter extends BuiltinBidirectionalConverter<Calendar, java.sql.Date> {
        
        @Override
        public java.sql.Date convertTo(Calendar source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(toLong(source));
        }
        
        @Override
        public Calendar convertFrom(java.sql.Date source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source);
        }
    }
    
    /**
     * Provides conversion between Calendar and Time
     */
    public static class CalendarToTimeConverter extends BuiltinBidirectionalConverter<Calendar, Time> {
        
        @Override
        public Time convertTo(Calendar source, Type<Time> destinationType, MappingContext context) {
            return new Time(toLong(source));
        }
        
        @Override
        public Calendar convertFrom(Time source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source);
        }
    }
    
    /**
     * Provides conversion between Date and XMLGregorianCalendar
     */
    public static class DateToXmlGregorianCalendarConverter extends BuiltinBidirectionalConverter<Date, XMLGregorianCalendar> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public XMLGregorianCalendar convertTo(Date source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source, factory);
        }
        
        @Override
        public Date convertFrom(XMLGregorianCalendar source, Type<Date> destinationType, MappingContext context) {
            return toDate(source);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Calendar and XMLGregorianCalendar
     */
    public static class CalendarToXmlGregorianCalendarConverter extends BuiltinBidirectionalConverter<Calendar, XMLGregorianCalendar> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public XMLGregorianCalendar convertTo(Calendar source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source, factory);
        }
        
        @Override
        public Calendar convertFrom(XMLGregorianCalendar source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Long and Date
     */
    public static class LongToDateConverter extends BuiltinBidirectionalConverter<Long, Date> {
        
        @Override
        public Date convertTo(Long source, Type<Date> destinationType, MappingContext context) {
            return toDate(source);
        }
        
        @Override
        public Long convertFrom(Date source, Type<Long> destinationType, MappingContext context) {
            return toLong(source);
        }
    }
    
    /**
     * Provides conversion between Long and Date
     */
    public static class LongToSqlDateConverter extends BuiltinBidirectionalConverter<Long, java.sql.Date> {
        
        @Override
        public java.sql.Date convertTo(Long source, Type<java.sql.Date> destinationType, MappingContext context) {
            return new java.sql.Date(source);
        }
        
        @Override
        public Long convertFrom(java.sql.Date source, Type<Long> destinationType, MappingContext context) {
            return toLong(source);
        }
    }
    
    /**
     * Provides conversion between Long and Date
     */
    public static class LongToTimeConverter extends BuiltinBidirectionalConverter<Long, Time> {
        
        @Override
        public Time convertTo(Long source, Type<Time> destinationType, MappingContext context) {
            return new Time(source);
        }
        
        @Override
        public Long convertFrom(Time source, Type<Long> destinationType, MappingContext context) {
            return toLong(source);
        }
    }
    
    /**
     * Provides conversion between Long and Calendar
     * 
     */
    public static class LongToCalendarConverter extends BuiltinBidirectionalConverter<Long, Calendar> {
        
        @Override
        public Calendar convertTo(Long source, Type<Calendar> destinationType, MappingContext context) {
            return toCalendar(source);
        }
        
        @Override
        public Long convertFrom(Calendar source, Type<Long> destinationType, MappingContext context) {
            return toLong(source);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Long and Calendar
     */
    public static class LongToXmlGregorianCalendarConverter extends BuiltinBidirectionalConverter<Long, XMLGregorianCalendar> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public XMLGregorianCalendar convertTo(Long source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source, factory);
        }
        
        @Override
        public Long convertFrom(XMLGregorianCalendar source, Type<Long> destinationType, MappingContext context) {
            return toLong(source);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Long and Timestamp
     */
    
    public static class LongToTimestampConverter extends BuiltinBidirectionalConverter<Long, Timestamp> {
        
        @Override
        public Timestamp convertTo(Long source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(source);
        }
        
        @Override
        public Long convertFrom(Timestamp source, Type<Long> destinationType, MappingContext context) {
            return source.getTime();
        }
        
    }
    
    /**
     * Provides conversion between Calendar and Time
     */
    public static class XmlGregorianCalendarToTimestampConverter extends BuiltinBidirectionalConverter<XMLGregorianCalendar, Timestamp> {
        
        /**
         * To create XMLGregorianCalendar instances
         */
        private static DatatypeFactory factory;
        
        {
            try {
                factory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public Timestamp convertTo(XMLGregorianCalendar source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(toLong(source));
        }
        
        @Override
        public XMLGregorianCalendar convertFrom(Timestamp source, Type<XMLGregorianCalendar> destinationType, MappingContext context) {
            return toXMLGregorianCalendar(source, factory);
        }
        
        @Override
        public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
            return polyCanConvert(this.sourceType, this.destinationType, sourceType, destinationType);
        }
    }
    
    /**
     * Provides conversion between Calendar and Time
     */
    public static class DateToTimestampConverter extends BuiltinBidirectionalConverter<Date, Timestamp> {
        
        @Override
        public Timestamp convertTo(Date source, Type<Timestamp> destinationType, MappingContext context) {
            return new Timestamp(toLong(source));
        }
        
        @Override
        public Date convertFrom(Timestamp source, Type<Date> destinationType, MappingContext context) {
            return toDate(source.getTime());
        }
    }
    
    private static Date toDate(XMLGregorianCalendar source) {
        return source.toGregorianCalendar().getTime();
    }
    
    private static Date toDate(Calendar source) {
        return source.getTime();
    }
    
    private static Date toDate(Long source) {
        return new Date(source);
    }
    
    private static Calendar toCalendar(XMLGregorianCalendar source) {
        return toCalendar(source.toGregorianCalendar().getTime());
    }
    
    private static Calendar toCalendar(Date source) {
        Calendar c = Calendar.getInstance();
        c.setTime(source);
        return c;
    }
    
    private static Calendar toCalendar(Long source) {
        return toCalendar(new Date(source));
    }
    
    private static XMLGregorianCalendar toXMLGregorianCalendar(Calendar source, DatatypeFactory factory) {
        return toXMLGregorianCalendar(source.getTime(), factory);
    }
    
    private static XMLGregorianCalendar toXMLGregorianCalendar(Date source, DatatypeFactory factory) {
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(source);
        
        return factory.newXMLGregorianCalendar(c);
        
    }
    
    private static XMLGregorianCalendar toXMLGregorianCalendar(Long source, DatatypeFactory factory) {
        return toXMLGregorianCalendar(new Date(source), factory);
    }
    
    private static Long toLong(Date source) {
        return source.getTime();
    }
    
    private static Long toLong(Calendar source) {
        return toLong(source.getTime());
    }
    
    private static Long toLong(XMLGregorianCalendar source) {
        return toLong(source.toGregorianCalendar().getTime());
    }
    
    public static boolean _polyCanConvert(Type<?> a, Type<?> b, Type<?> c, Type<?> d) {
        return (a.isAssignableFrom(c)) && (!d.getRawType().equals(Object.class) && d.isAssignableFrom(b));
    }
    
    public static boolean polyCanConvert(Type<?> a, Type<?> b, Type<?> c, Type<?> d) {
        return _polyCanConvert(a, b, c, d) || _polyCanConvert(b, a, c, d);
    }
}
