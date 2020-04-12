package com.archytasit.jersey.multipart;


import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.common.AbstractTest;
import com.archytasit.jersey.multipart.common.EnumTest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class TestBasicParamsTypes extends AbstractTest {

    @Path("/upload")
    public static class TestResource {

        public static class PojoTest {
            @FormDataParam("field") String field;
            @FormDataParam("field") List<String> fieldLists;
            @FormDataParam("date") LocalDate date;
            @FormDataParam("date") SortedSet<LocalDate> dateList;

            @FormDataParam("enum") EnumTest enu;
            @FormDataParam("enum") Set<EnumTest> enuList;

            @FormDataParam("long") Long longNum;
            @FormDataParam("long") List<Long> longNumList;

            @FormDataParam("decimal") BigDecimal bigDecimal;
            @FormDataParam("decimal") List<BigDecimal> bigDecimalList;

            @FormDataParam("file") FormDataBodyPart file;
            @FormDataParam("file") List<FormDataBodyPart> fileList;
        }


        @POST
        @Consumes("multipart/*")
        @Produces("text/plain")
        @Path("/1")
        public String upload(
                @FormDataParam("field") String field,
                @FormDataParam("field") List<String> fieldLists,
                @FormDataParam("date") LocalDate date,
                @FormDataParam("date") SortedSet<LocalDate> dateList,

                @FormDataParam("enum") EnumTest enu,
                @FormDataParam("enum") Set<EnumTest> enuList,

                @FormDataParam("long") Long longNum,
                @FormDataParam("long") List<Long> longNumList,

                @FormDataParam("decimal") BigDecimal bigDecimal,
                @FormDataParam("decimal") List<BigDecimal> bigDecimalList,

                @FormDataParam("file") FormDataBodyPart file,
                @FormDataParam("file") List<FormDataBodyPart> fileList) {

            PojoTest pojo = new PojoTest();
            pojo.field = field;
            pojo.fieldLists = fieldLists;
            pojo.date = date;
            pojo.dateList = dateList;

            pojo.enu = enu;
            pojo.enuList = enuList;

            pojo.longNum = longNum;
            pojo.longNumList = longNumList;

            pojo.bigDecimal = bigDecimal;
            pojo.bigDecimalList = bigDecimalList;

            pojo.file = file;
            pojo.fileList = fileList;

            return checkPojo(pojo);

        }


        @POST
        @Consumes("multipart/*")
        @Produces("text/plain")
        @Path("/2")
        public String upload(@BeanParam PojoTest pojo) {
            return checkPojo(pojo);
        }

        private String checkPojo(PojoTest pojo) {
            try {


                Assert.assertEquals("HA", pojo.field);
                Assert.assertEquals(3, pojo.fieldLists.size());
                Assert.assertEquals("HA", pojo.fieldLists.get(0));
                Assert.assertEquals("HO", pojo.fieldLists.get(1));
                Assert.assertEquals("HI", pojo.fieldLists.get(2));

                Assert.assertEquals(LocalDate.of(2020, 02, 03), pojo.date);
                Assert.assertEquals(2, pojo.dateList.size());
                Assert.assertTrue(pojo.dateList.contains(LocalDate.of(2020, 02, 03)));
                Assert.assertTrue(pojo.dateList.contains(LocalDate.of(2020, 01, 10)));

                Assert.assertEquals(EnumTest.V2, pojo.enu);
                Assert.assertEquals(4, pojo.enuList.size());
                Assert.assertTrue(pojo.enuList.contains(EnumTest.V1));
                Assert.assertTrue(pojo.enuList.contains(EnumTest.V2));
                Assert.assertTrue(pojo.enuList.contains(EnumTest.V3));
                Assert.assertTrue(pojo.enuList.contains(null));

                Assert.assertEquals(Long.valueOf(22l), pojo.longNum);
                Assert.assertEquals(2, pojo.longNumList.size());
                Assert.assertEquals(Long.valueOf(22l), pojo.longNumList.get(0));
                Assert.assertEquals(Long.valueOf(-456l), pojo.longNumList.get(1));

                Assert.assertEquals(BigDecimal.valueOf(3.456), pojo.bigDecimal);
                Assert.assertEquals(3, pojo.bigDecimalList.size());
                Assert.assertEquals(BigDecimal.valueOf(3.456), pojo.bigDecimalList.get(0));
                Assert.assertNull(pojo.bigDecimalList.get(1));
                Assert.assertEquals(BigDecimal.valueOf(Double.valueOf("1235.543")), pojo.bigDecimalList.get(2));

                Assert.assertTrue((pojo.file instanceof FormDataFileBodyPart));

                Assert.assertFalse(pojo.file.isFormField());
                Assert.assertEquals(188, pojo.file.getContentLength());
                Assert.assertEquals("upload.bin1", pojo.file.getFilename());

                Assert.assertEquals(3, pojo.fileList.size());


                Assert.assertFalse(pojo.fileList.get(0).isFormField());
                Assert.assertEquals(188, pojo.fileList.get(0).getContentLength());
                Assert.assertEquals("upload.bin1", pojo.fileList.get(0).getFilename());

                Assert.assertFalse(pojo.fileList.get(1).isFormField());
                Assert.assertEquals(226, pojo.fileList.get(1).getContentLength());
                Assert.assertEquals("upload.bin3", pojo.fileList.get(1).getFilename());

                Assert.assertFalse(pojo.fileList.get(2).isFormField());
                Assert.assertEquals(207, pojo.fileList.get(2).getContentLength());
                Assert.assertEquals("upload.bin2", pojo.fileList.get(2).getFilename());


            } catch (Throwable a) {
                return "FAIL: " + a.getMessage();
            }
            return "OK !";
        }
    }


    @Override
    protected void configureServer(ResourceConfig rc) {
        rc.register(TestResource.class);
    }

    @Test
    public void testTypesAsParameters() {
        MultiPart multiPart = MultiPart.formDataMultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020,02,03))));
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020,01,10))));
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
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text("")));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V1)));

        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(1)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(3)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(2)));

        postSuccess("/upload/1", multiPart);


    }

    @Test
    public void testTypesAsBeanParams() {
        MultiPart multiPart = MultiPart.formDataMultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020, 02, 03))));
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020, 01, 10))));
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
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text("")));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V1)));

        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(1)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(3)));
        multiPart.add(new FormDataFileBodyPart("file", getFakeFile(2)));

        postSuccess("/upload/2", multiPart);

    }

    @Test
    public void testNestedFiles() {
        MultiPart multiPart = MultiPart.formDataMultiPart();
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020, 02, 03))));
        multiPart.add(new FormDataEntityBodyPart<>("date", Entity.text(LocalDate.of(2020, 01, 10))));
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
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text("")));
        multiPart.add(new FormDataEntityBodyPart<>("enum", Entity.text(EnumTest.V1)));

        MultiPart nestedPart = MultiPart.mixedMultiPart("file");
        nestedPart.add(new FormDataFileBodyPart("file1", getFakeFile(1)));
        nestedPart.add(new FormDataFileBodyPart("file2", getFakeFile(3)));
        nestedPart.add(new FormDataFileBodyPart("file3", getFakeFile(2)));
        multiPart.add(nestedPart);


        postSuccess("/upload/2", multiPart);
    }

}


