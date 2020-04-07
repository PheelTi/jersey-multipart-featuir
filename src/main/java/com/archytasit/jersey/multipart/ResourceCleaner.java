package com.archytasit.jersey.multipart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.glassfish.jersey.server.ContainerRequest;

import com.archytasit.jersey.multipart.model.databags.IDataBag;

public class ResourceCleaner {

    private static String REQUEST_FILES_LIST_PROPERTY_NAME = ResourceCleaner.class.getName();
    private static String REQUEST_MARKER_PROPERTY_NAME = "MARKER_" + ResourceCleaner.class.getName();

    public void trackResourceToClean(IDataBag resourceToClean, ContainerRequest request) {

        UUID marker = getOrCreateRequestProperty(request, REQUEST_MARKER_PROPERTY_NAME, UUID::randomUUID);
        Set<IDataBag> filesToClean = getOrCreateRequestProperty(request, REQUEST_FILES_LIST_PROPERTY_NAME, HashSet::new);
        filesToClean.add(resourceToClean);


    }


    public void cleanRequest(ContainerRequest request) {
        Set<IDataBag> filesToClean = (Set<IDataBag>) request.getProperty(ResourceCleaner.class.getName());
        if (filesToClean != null) {
            filesToClean.forEach(IDataBag::cleanResource);
        }
    }



    private <T> T getOrCreateRequestProperty(ContainerRequest request, String propName, Supplier<T> newItem) {
        T item = (T) request.getProperty(propName);
        if (item == null) {
            request.setProperty(propName, item = newItem.get());
        }
        return item;
    }

}
