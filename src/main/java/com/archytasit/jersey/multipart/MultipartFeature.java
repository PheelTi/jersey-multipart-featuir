package com.archytasit.jersey.multipart;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.archytasit.jersey.multipart.model.bodyparts.apache.ApacheFileUploadBodyPartProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Bindings;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.internal.FormDataParamValueParamProvider;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPartProvider;
import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;
import com.archytasit.jersey.multipart.model.databags.file.FileDataBagProvider;



public class MultipartFeature implements Feature {

    private IBodyPartProvider bodyPartProvider;

    private IDataBagProvider dataBagProvider;

    public MultipartFeature withBodyPartProvider(IBodyPartProvider bodyPartProvider) {
        this.bodyPartProvider = bodyPartProvider;
        return this;
    }

    public MultipartFeature withDataBagProvider(IDataBagProvider dataBagProvider) {
        this.dataBagProvider = dataBagProvider;
        return this;
    }


    public boolean configure(FeatureContext context) {


        context.register(new AbstractBinder() {
            @Override
            protected void configure() {

                if (bodyPartProvider == null) {
                    bind(new ApacheFileUploadBodyPartProvider()).to(IBodyPartProvider.class);
                }

                if (dataBagProvider == null) {
                    bind(new FileDataBagProvider()).to(IDataBagProvider.class);
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

