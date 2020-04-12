package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.annotations.Map;
import com.archytasit.jersey.multipart.common.AbstractTest;
import com.archytasit.jersey.multipart.common.SamplePojo;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import java.util.List;

public class TestMapContentType extends AbstractTest {

    @Path("/upload")
    public static class TestResource {

        @POST
        @Path("/string")
        @Consumes("multipart/*")
        @Produces("text/plain")
        public String uploadString(
                @FormDataParam("field") String field,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {

            return "OK ! " + ((field == null) ? " null" : "");
        }

        @POST
        @Path("/json-force")
        @Consumes("multipart/*")
        @Produces("text/plain")
        public String uploadForceJson(
                @FormDataParam(value = "field", mapContentTypeAs = {
                        @Map(to= MediaType.APPLICATION_JSON)
                }) SamplePojo jsonPojo,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {

            return "OK ! "+((jsonPojo == null) ? " null" : "");
        }

        @POST
        @Path("/json")
        @Consumes("multipart/*")
        @Produces("text/plain")
        public String uploadJson(
                @FormDataParam("field") SamplePojo jsonPojo,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {

            return "OK ! "+((jsonPojo == null) ? " null" : "");
        }
    }

    @Override
    protected void configureServer(ResourceConfig rc) {
        rc.register(TestResource.class);
        rc.register(JacksonFeature.withoutExceptionMappers());
    }

    @Test
    public void testUploadWithoutContentType() {

        MultiPart multiPart = getMultiPart(MediaType.TEXT_PLAIN_TYPE);
        String reponse = postSuccess("/upload/string", multiPart);
        Assert.assertFalse(reponse.endsWith(" null"));

        reponse = postSuccess("/upload/json", multiPart);
        Assert.assertTrue(reponse.endsWith(" null"));

        reponse = postSuccess("/upload/json-force", multiPart);
        Assert.assertFalse(reponse.endsWith(" null"));
    }

    @Test
    public void testUploadWithRightContentType() {

        MultiPart multiPart = getMultiPart(MediaType.APPLICATION_JSON_TYPE);
        String reponse = postSuccess("/upload/string", multiPart);
        Assert.assertFalse(reponse.endsWith(" null"));

        reponse = postSuccess("/upload/json", multiPart);
        Assert.assertFalse(reponse.endsWith(" null"));

        reponse = postSuccess("/upload/json-force", multiPart);
        Assert.assertFalse(reponse.endsWith(" null"));
    }

    private MultiPart getMultiPart(MediaType jsonMediaType) {

        MultiPart multiPart = MultiPart.formDataMultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.entity("{\"a\":12, \"b\":\"TestString\", \"c\":\"V3\"}", jsonMediaType)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(1)));
        return multiPart;
    }

}
