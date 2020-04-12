package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.common.AbstractTest;
import com.archytasit.jersey.multipart.common.EnumTest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class TestRequestSizeLimit extends AbstractTest {

    @Path("/upload")
    public static class TestResource {

        @POST
        @Consumes("multipart/*")
        @Produces("text/plain")
        public String upload(
                @FormDataParam("field") String field,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {

            return "OK !";

        }
    }

    @Override
    protected void configureServer(ResourceConfig rc) {
        rc.register(TestResource.class);
    }

    @Override
    protected void customizeMultiPartConfig(MultiPartConfig config) {
        super.customizeMultiPartConfig(config);
        config.requestSizeLimit(600);
    }

    @Test
    public void testUploadSuccess() {
        MultiPart multiPart = MultiPart.formDataMultiPart();

        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.text("dummy Text")));

        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(1)));

        postSuccess("/upload", multiPart);


    }

    @Test
    public void testUploadFailure() {
        MultiPart multiPart = MultiPart.formDataMultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.text("dummy Text")));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(1)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(2)));
        postException("/upload", multiPart, 413);

    }
}
