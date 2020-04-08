package com.archytasit.jersey.multipart;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.archytasit.jersey.multipart.model.FileBodyPartProvider;
import com.archytasit.jersey.multipart.model.FormFieldOrAttachementBodyPartProvider;
import com.archytasit.jersey.multipart.model.StringBodyPartProvider;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.apache.ApachePartParser;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Bindings;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.internal.FormDataParamValueParamProvider;
import com.archytasit.jersey.multipart.model.IBodyPartProvider;


/**
 * The type Multipart feature.
 */
public class MultipartFeature implements Feature {

    private IRequestParser requestParser;

    private IBodyPartProvider bodyPartProvider;


    /**
     * With body part provider multipart feature.
     *
     * @param bodyPartProvider the body part provider
     * @return the multipart feature
     */
    public MultipartFeature withBodyPartProvider(IBodyPartProvider bodyPartProvider) {
        this.bodyPartProvider = bodyPartProvider;
        return this;
    }


    /**
     * With request parser multipart feature.
     *
     * @param requestParser the request parser
     * @return the multipart feature
     */
    public MultipartFeature withRequestParser(IRequestParser requestParser) {
        this.requestParser = requestParser;
        return this;
    }


    public boolean configure(FeatureContext context) {


        context.register(new AbstractBinder() {
            @Override
            protected void configure() {

                if (bodyPartProvider == null) {
                    bind(new FormFieldOrAttachementBodyPartProvider(new StringBodyPartProvider(), new FileBodyPartProvider())).to(IBodyPartProvider.class);
                }

                if (requestParser == null) {
                    bind(new ApachePartParser()).to(IRequestParser.class);
                }

                bindAsContract(ResourceCleaner.class);

                Provider<MultivaluedParameterExtractorProvider> extractorProvider =
                        createManagedInstanceProvider(MultivaluedParameterExtractorProvider.class);
                Provider<ContainerRequest> requestProvider =
                        createManagedInstanceProvider(ContainerRequest.class);

                FormDataParamValueParamProvider valueSupplier =
                        new FormDataParamValueParamProvider(extractorProvider);
                bind(Bindings.service(valueSupplier).to(ValueParamProvider.class));
                bind(Bindings.injectionResolver(
                        new ParamInjectionResolver<>(valueSupplier, FormDataParam.class, requestProvider)));
            }
        });
        context.register(ResourceCleaningListener.class);
        context.register(FileUploadMessageBodyWorker.class);

        return true;
    }
}

