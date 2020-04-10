package com.archytasit.jersey.multipart;


import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.common.AbstractTest;
import com.archytasit.jersey.multipart.common.EnumTest;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class TestParamsTypes extends AbstractTest {

    @Path("/upload")
    public static class MegaResource{

        public static class PojoTest {
            @FormDataParam("field") String field;
            @FormDataParam("field") List<String> fieldLists;
            @FormDataParam("date") LocalDate date;
            @FormDataParam("date") SortedSet<LocalDate> dateList;

            @FormDataParam("enum") EnumTest enu;
            @FormDataParam("enum") Set<EnumTest> enuList;

            @FormDataParam("long") Long longNum;
            @FormDataParam("long") List<Long> LongNumList;

            @FormDataParam("decimal") BigDecimal bigDecimal;
            @FormDataParam("decimal") List<BigDecimal> bigDecimalList;

            @FormDataParam("file") FormDataBodyPart file;
            @FormDataParam("file") List<FormDataBodyPart> fileList;
        }

        @POST
        @Consumes("multipart/*")
        @Produces("text/plain")
        @Path("/1")
        public String upload(@BeanParam PojoTest pojo) {
            return "OK !!!";

        }

        @POST
        @Consumes("multipart/*")
        @Produces("text/plain")
        @Path("/2")
        public String upload(
                @FormDataParam("field") String field,
                @FormDataParam("field") List<String> fieldLists,
                @FormDataParam("date") LocalDate date,
                @FormDataParam("date") SortedSet<LocalDate> dateList,

                @FormDataParam("enum") EnumTest enu,
                @FormDataParam("enum") Set<EnumTest> enuList,

                @FormDataParam("long") Long longNum,
                @FormDataParam("long") List<Long> LongNumList,

                @FormDataParam("decimal") BigDecimal bigDecimal,
                @FormDataParam("decimal") List<BigDecimal> bigDecimalList,

                @FormDataParam("file") FormDataBodyPart file,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {
            return "OK !!!";

        }
    }

    @Override
    protected Class<?>[] getResourcesForServer() {
        return new Class[] {MegaResource.class};
    }

    @Test
    public void testUpload1() {
        MultiPart multiPart = new MultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.now())));
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.now().minusDays(3))));
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text("")));

        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.text("HA")));
        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.text("HO")));
        multiPart.add(new FormDataEntityBodyPart<>("field", Entity.text("HI")));

        multiPart.add(new FormDataEntityBodyPart<>("long", Entity.text(22L)));
        multiPart.add(new FormDataEntityBodyPart<>("long", Entity.text("-456")));


        multiPart.add(new FormDataEntityBodyPart<>("decimal", Entity.text(BigDecimal.valueOf(3.456))));
        multiPart.add(new FormDataEntityBodyPart<>("decimal", Entity.text("")));
        multiPart.add(new FormDataEntityBodyPart<>("decimal", Entity.text("1235.543")));

        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V2)));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V3)));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V1)));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text("")));

        multiPart.add(new FormDataFileBodyPart("file", new File("D:\\DEV\\local\\workspace_neo4j\\jersey-multipart-upload-core-feature-save\\pom.xml")));

        postSuccess("/upload/1", multiPart);

        postSuccess("/upload/2", multiPart);

    }

    // TODO implementations
    // - implement nested parts with 2 modes of querying
    // - change multivalued extractor (check before) with classic extractor

    // TODO Tests
    // - test nested parts
    // - test fieldType annotation (rename ?)
    // - test map annotation
    // - test accept annotation
    // - test StreamBodyPart
    // - test entity too large
    // - test file : no absolute file
















}
