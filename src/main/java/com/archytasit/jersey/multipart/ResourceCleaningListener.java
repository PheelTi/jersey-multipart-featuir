package com.archytasit.jersey.multipart;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.inject.Inject;

/**
 * Resource cleaning listener, whic triggers the resource cleaning
 */
public class ResourceCleaningListener implements ApplicationEventListener {

    @Inject
    private ResourceCleaner resourceCleaner;

    @Override
    public void onEvent(ApplicationEvent event) {

    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return (e) -> {
            switch (e.getType()) {
                case FINISHED:
                    resourceCleaner.cleanRequest(e.getContainerRequest(), e.isSuccess());
                    break;
            }
        };
    }

}
