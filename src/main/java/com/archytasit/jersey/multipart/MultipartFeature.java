package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.bodypartproviders.*;
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
     * sets a custom body part provider.
     *
     * @param bodyPartProvider the body part provider
     * @return the multipart feature
     */
    public MultipartFeature withBodyPartProvider(IFormDataBodyPartProvider bodyPartProvider) {
        this.bodyPartProvider = bodyPartProvider;
        return this;
    }


    /**
     * sets a custom request parser.
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

        // binding bodyPartProvider and requestParser in Jersey injection framework (HK2)
        // sets default implementation for each if not set before configure.
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(bodyPartProvider != null ? bodyPartProvider : new FieldOrAttachementBodyPartProvider(
                            new MemoryLimitBodyPartProvider(null, new FormDataStringBodyPartProvider(), new FormDataFileBodyPartProvider()), new FormDataFileBodyPartProvider())).to(IFormDataBodyPartProvider.class);

                bind(requestParser != null ? requestParser : new ApachePartParser()).to(IRequestParser.class);

            }
        });


        // on sever side
        if (RuntimeType.SERVER.equals(runtime)) {


            context.register(new AbstractBinder() {
                @Override
                protected void configure() {

                    // binding ResourceCleaner in HK2
                    bindAsContract(ResourceCleaner.class);

                    // this allos to extract potentially multivalued parameters from incoming requests (like headers, but FormDataParam also)
                    Provider<MultivaluedParameterExtractorProvider> extractorProvider =
                            createManagedInstanceProvider(MultivaluedParameterExtractorProvider.class);

                    // request provider for getting request in message body workers
                    Provider<ContainerRequest> requestProvider =
                            createManagedInstanceProvider(ContainerRequest.class);

                    // the value supplier for annotated methods or fields annotated with FormDataParam
                    FormDataParamValueParamProvider valueSupplier =
                            new FormDataParamValueParamProvider(extractorProvider);
                    bind(Bindings.service(valueSupplier).to(ValueParamProvider.class));
                    bind(Bindings.injectionResolver(
                            new ParamInjectionResolver<>(valueSupplier, FormDataParam.class, requestProvider)));
                }
            });
            // registering the resourceCleaningListener
            context.register(ResourceCleaningListener.class);
            // registering the message body reader for incoming requests
            context.register(MultiPartMessageBodyReaderServer.class);
        } else {
            // registering the message body reader for client in case a server respond a multipart
            context.register(MultiPartMessageBodyReaderClient.class);

        }

        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                // this allows to get all paramconverters (default and custom ones available.
                bind(new MultiPartParameterConverterProvider(
                            Values.lazy((Value<ParamConverterFactory>) () ->
                                    new ParamConverterFactory(
                                            Providers.getProviders(InjectionManagerProvider.getInjectionManager(context), ParamConverterProvider.class),
                                            Providers.getCustomProviders(InjectionManagerProvider.getInjectionManager(context), ParamConverterProvider.class)))))
                    .to(MultiPartParameterConverterProvider.class);
            }
        });

        // writer for multipart
        context.register(MultiPartMessageBodyWriter.class);

        return true;
    }
}

