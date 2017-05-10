package fr.avianey.lunatech.provider;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SuppressWarnings("rawtypes")
public class GsonProvider implements MessageBodyReader, MessageBodyWriter {

    public static ExclusionStrategy EXCLUSION = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> c) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fa) {
            return fa.getAnnotation(OneToMany.class) != null || fa.getAnnotation(ManyToMany.class) != null;
        }
    };

    public static GsonBuilder OUT_BUILDER = new GsonBuilder()
            .setExclusionStrategies(EXCLUSION)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    public static GsonBuilder IN_BUILDER = new GsonBuilder();

    @Override
    public long getSize(Object o, Class c, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class c, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Object o, Class c, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap values, OutputStream os) throws IOException, WebApplicationException {
        os.write(OUT_BUILDER.create().toJson(o, type).getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public boolean isReadable(Class c, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class c, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap values, InputStream is) throws IOException, WebApplicationException {
        Object o = IN_BUILDER.create().fromJson(new InputStreamReader(is), type);
        return o;
    }

}