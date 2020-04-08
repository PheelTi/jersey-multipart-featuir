import com.archytasit.jersey.multipart.MultipartFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.JerseyTestNg;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class TestApplication extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig();
        rc.register(TestResource.class);
        rc.register(MultipartFeature.class);
        rc.register( new ParamConverterProvider() {
            @Override
            public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
                if (aClass != null && aClass.isAssignableFrom(LocalDate.class)) {
                    return (ParamConverter<T>) new ParamConverter<LocalDate>() {
                        @Override
                        public LocalDate fromString(String s) {
                            return s == null ? null : LocalDate.parse(s);
                        }

                        @Override
                        public String toString(LocalDate localDate) {
                            return localDate.toString();
                        }
                    };

                }
                return null;
            }


        });
        return rc;
    }

    @Test
    public void testUpload1() {
        Response response = target("/upload/ws1").request(MediaType.TEXT_PLAIN_TYPE)
                .post(Entity.entity(new ByteArrayInputStream(("\r\n" +
                        "--12345\r\n" +
                        "Content-Disposition: form-data; name=\"date\"\r\n" +
                        "\r\n" +
                        "2020-03-06\r\n" +
                        "--12345\r\n" +
                        "Content-Disposition: form-data; name=\"field\"\r\n" +
                        "\r\n" +
                        "Texte1\r\n" +
                        "--12345\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"filename.xyz\"\r\n" +
                        "\r\n" +
                        "content of filename.xyz that you upload in your form with input[type=file]\r\n" +
                        "--12345\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"picture_of_sunset.jpg\"\r\n" +
                        "\r\n" +
                        "content of picture_of_sunset.jpg ...\r\n" +
                        "--12345--\r\n").getBytes()), MediaType.MULTIPART_FORM_DATA_TYPE+"; boundary=12345"));

        System.out.println(response.getEntity());
        Assert.assertEquals("Http Response should be 204: ", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

    }

}
