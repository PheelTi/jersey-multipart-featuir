package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.bodypartproviders.FieldOrAttachementBodyPartProvider;
import com.archytasit.jersey.multipart.bodypartproviders.FormDataFileBodyPartProvider;
import com.archytasit.jersey.multipart.bodypartproviders.FormDataStringBodyPartProvider;
import com.archytasit.jersey.multipart.bodypartproviders.IFormDataBodyPartProvider;
import com.archytasit.jersey.multipart.internal.FormDataParamValueParamProvider;
import com.archytasit.jersey.multipart.internal.MultiPartParameterConverterProvider;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.apache.ApachePartParser;
import org.glassfish.jersey.InjectionManagerProvider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Bindings;
import org.glassfish.jersey.internal.inject.ParamConverterFactory;
import org.glassfish.jersey.internal.inject.Providers;
import org.glassfish.jersey.internal.util.collection.Value;
import org.glassfish.jersey.internal.util.collection.Values;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import javax.inject.Provider;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.ParamConverterProvider;


/**
 * The type Multipart feature.
 */
public class MultipartFeature implements Feature {

    private IRequestParser requestParser;

    private IFormDataBodyPartProvider bodyPartProvider;


    /**
     * With body part provider multipart feature.
     *
     * @param bodyPartProvider the body part provider
     * @return the multipart feature
     */
    public MultipartFeature withBodyPartProvider(IFormDataBodyPartProvider bodyPartProvider) {
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
        final RuntimeType runtime = context.getConfiguration().getRuntimeType();

        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                if (bodyPartProvider == null) {
                    bind(new FieldOrAttachementBodyPartProvider(new FormDataStringBodyPartProvider(), new FormDataFileBodyPartProvider())).to(IFormDataBodyPartProvider.class);
                }

                if (requestParser == null) {
                    bind(new ApachePartParser()).to(IRequestParser.class);
                }
            }
        });


        if (RuntimeType.SERVER.equals(runtime)) {



            context.register(new AbstractBinder() {
                @Override
                protected void configure() {

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
            context.register(MultiPartMessageBodyReaderServer.class);
        } else {
            context.register(MultiPartMessageBodyReaderClient.class);

        }

        context.register(new AbstractBinder() {
            @Override
            protected void configure() {

                bind(new MultiPartParameterConverterProvider(
                            Values.lazy((Value<ParamConverterFactory>) () ->
                                    new ParamConverterFactory(
                                            Providers.getProviders(InjectionManagerProvider.getInjectionManager(context), ParamConverterProvider.class),
                                            Providers.getCustomProviders(InjectionManagerProvider.getInjectionManager(context), ParamConverterProvider.class)))))
                    .to(MultiPartParameterConverterProvider.class);
            }
        });
        context.register(MultiPartMessageBodyWriter.class);

        return true;
    }
}

