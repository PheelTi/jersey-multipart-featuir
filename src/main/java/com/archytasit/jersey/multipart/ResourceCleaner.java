package com.archytasit.jersey.multipart;

import org.glassfish.jersey.server.ContainerRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The Resource cleaner class
 */
public class ResourceCleaner {

    private static String REQUEST_FILES_LIST_PROPERTY_NAME = ResourceCleaner.class.getName();


    /**
     * Track a resource to be cleaned after request by storing it in the RequestContext.
     *
     * @param resourceToClean the resource to clean
     * @param cleanMode       the clean mode
     * @param request         the request
     */
    public void trackResourceToClean(BodyPart resourceToClean, MultiPartConfig.CleanResourceMode cleanMode, ContainerRequest request) {
        if (!cleanMode.equals(MultiPartConfig.CleanResourceMode.NEVER)) {
            Map<MultiPartConfig.CleanResourceMode, Set<BodyPart>> filesToClean = getOrCreateRequestProperty(request, REQUEST_FILES_LIST_PROPERTY_NAME, HashMap::new);
            if (filesToClean.get(cleanMode) == null) {
                filesToClean.put(cleanMode, new HashSet<>());
            }
            filesToClean.get(cleanMode).add(resourceToClean);
        }
    }


    /**
     * Clean the resources stored in the request.
     *
     * @param request   the request
     * @param isSuccess the is success
     */
    public void cleanRequest(ContainerRequest request, boolean isSuccess) {

        Map<MultiPartConfig.CleanResourceMode, Set<BodyPart>> filesToClean = (Map<MultiPartConfig.CleanResourceMode, Set<BodyPart>>) request.getProperty(ResourceCleaner.class.getName());
        if (filesToClean != null) {
            filesToClean.getOrDefault(MultiPartConfig.CleanResourceMode.ALWAYS, new HashSet<>()).forEach(BodyPart::close);
            if (!isSuccess) {
                filesToClean.getOrDefault(MultiPartConfig.CleanResourceMode.ON_ERROR, new HashSet<>()).forEach(BodyPart::close);
            }
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
