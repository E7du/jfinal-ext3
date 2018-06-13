/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
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
package com.jfinal.ext.kit;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.common.base.Throwables;
import com.jfinal.log.Log;

public class JaxbKit {

    protected final static Log LOG = Log.getLog(JaxbKit.class);

    /**
     * 
     * string -> object
     * 
     * @param src
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(String src, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(new StringReader(src));
        } catch (JAXBException e) {
            Throwables.propagate(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(File xmlFile, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(xmlFile);
        } catch (JAXBException e) {
            Throwables.propagate(e);
        }
        return result;
    }

    /**
     * object -> string
     */
    public static String marshal(Object jaxbElement) {
        StringWriter sw ;
        try {
            Marshaller fm = JAXBContext.newInstance(jaxbElement.getClass()).createMarshaller();
            fm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sw = new StringWriter();
            fm.marshal(jaxbElement, sw);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }
}
