package com.archytasit.jersey.multipart.common;

import com.archytasit.jersey.multipart.MultiPart;
import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.MultipartFeature;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.*;
import java.util.stream.Stream;

public abstract class AbstractTest extends JerseyTest {

    private static final Logger LOGGER = LogManager.getLogManager().getLogger("");;
    public static final Properties PROPS = new Properties();

    static {
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        for (Handler h : LOGGER.getHandlers()) {
            h.setLevel(Level.INFO);
        }
        InputStream propsStream =AbstractTest.class.getClassLoader().getResourceAsStream("unit.properties");
        if (propsStream != null) {
            try {
                PROPS.load(propsStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Logger.getLogger("org.glassfish.jersey.server.ServerRuntime").setLevel(Level.FINEST);
    }

    public static class MultiPartConfigResolver implements
            ContextResolver<MultiPartConfig> {

        @Inject
        private MultiPartConfig multiPartConfig;

        @Override
        public MultiPartConfig getContext(Class<?> type) {
            return multiPartConfig;
        }

    }

    protected File getFakeFile(Integer nb) {
        return new File(PROPS.getProperty("fake.file")+nb);
    }

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig();
        rc.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(getMultiPartConfig()).to(MultiPartConfig.class);
            }
        });
        rc.register(MultiPartConfigResolver.class);
        rc.register(MultipartFeature.class);
        rc.register(LocalDateParamConverter.class);
        rc.register(ExceptionMappers.ThrowableExceptionMapper.class);
        rc.register(ExceptionMappers.WebApplicationExceptionExceptionMapper.class);
        rc.register(new LoggingFeature(LOGGER, Level.FINEST, null, null));
        configureServer(rc);

        return rc;
    }

    protected abstract void configureServer(ResourceConfig rc);

    private MultiPartConfig getMultiPartConfig() {
        MultiPartConfig config = new MultiPartConfig();
        File tempDir = new File(PROPS.getProperty("temp.directory"));
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        config.tempFileDirectory(tempDir.getAbsolutePath());
        customizeMultiPartConfig(config);
        return config;
    }

    protected void customizeMultiPartConfig(MultiPartConfig config) {

    }


    @Override
    protected void configureClient(ClientConfig config) {
        config
                .register(MultipartFeature.class)
                .register(LocalDateParamConverter.class)
                .register(new LoggingFeature(LOGGER, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, null));

    }



    protected String postSuccess(String url, MultiPart content) {
        Response resp = post(url, MediaType.MULTIPART_FORM_DATA_TYPE, MediaType.TEXT_PLAIN_TYPE, content);
        Assert.assertEquals("POST response status failed", Response.Status.Family.SUCCESSFUL, resp.getStatusInfo().getFamily());
        String respStr = resp.readEntity(String.class);
        Assert.assertFalse(respStr, respStr.startsWith("FAIL: "));
        return respStr;
    }

    protected Response postException(String url, MultiPart content, int expectedStatus) {
        Response resp = post(url, MediaType.MULTIPART_FORM_DATA_TYPE, MediaType.TEXT_PLAIN_TYPE, content);
        Assert.assertEquals(resp.getStatus() + " " + resp.getEntity().toString(), expectedStatus, resp.getStatus());
        return resp;
    }

    private Response post(String url, MediaType requestCT, MediaType responseCT, Object entity) {


        return target(url).request(responseCT).post(Entity.entity(entity, requestCT));
    }



}
