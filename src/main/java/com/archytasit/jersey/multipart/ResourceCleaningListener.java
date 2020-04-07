package com.archytasit.jersey.multipart;

import javax.inject.Inject;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class ResourceCleaningListener implements ApplicationEventListener {

    @Inject
    private ResourceCleaner tempFileCleaner;

    @Override
    public void onEvent(ApplicationEvent event) {

    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return (e) -> {
            switch (e.getType()) {
                case FINISHED:
                    tempFileCleaner.cleanRequest(e.getContainerRequest());
                    break;
            }
        };
    }

}
