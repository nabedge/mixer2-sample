package org.mixer2.sample;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;

@Provider
@Produces(MediaType.TEXT_HTML)
public class Mixer2Writer implements MessageBodyWriter<Html> {

    private Mixer2Engine mixer2Engine = Mixer2EngineSingleton.getInstance();
    
    @Override
    public long getSize(Html html, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        if (type.equals(Html.class)) {
            return true;
        }
        return false;
    }

    @Override
    public void writeTo(Html html, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap,
            OutputStream outputStream) throws IOException, WebApplicationException {
        // marshalling html object to string.
        String str = mixer2Engine.saveToString(html);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(str);
        writer.flush();
    }

}
