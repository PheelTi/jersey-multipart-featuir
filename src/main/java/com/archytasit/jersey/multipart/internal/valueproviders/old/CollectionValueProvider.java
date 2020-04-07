//package com.archytasit.jersey.multipart.internal.valueproviders.old;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.ext.MessageBodyReader;
//
//import org.glassfish.jersey.internal.util.ReflectionHelper;
//import org.glassfish.jersey.internal.util.collection.NullableMultivaluedHashMap;
//import org.glassfish.jersey.message.MessageBodyWorkers;
//import org.glassfish.jersey.model.Parameter;
//import org.glassfish.jersey.server.ContainerRequest;
//import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor;
//
//import com.archytasit.jersey.multipart.exception.FormDataParamException;
//import com.archytasit.jersey.multipart.internal.valueproviders.AbstractMultiPartValueProvider;
//import com.archytasit.jersey.multipart.model.bodyparts.FormBodyPart;
//import com.archytasit.jersey.multipart.model.IBodyPart;
//import com.archytasit.jersey.multipart.utils.ClassUtils;
//
//public class CollectionValueProvider extends AbstractMultiPartValueProvider<Object> {
//
//    private enum MODE {
//        MESSAGE_BODY_WORKER,
//        EXTRACTOR
//    }
//
//    private static final class BodyPartInfo {
//
//        private BodyPartInfo(IBodyPart bodyPart) {
//            this.bodyPart = bodyPart;
//        }
//
//        private IBodyPart bodyPart;
//
//        private Optional<Object> readerValue = null;
//
//    }
//
//    private String paramName;
//    private Parameter parameter;
//    private Class<?> targetClass;
//    private MultivaluedParameterExtractor<?> extractor;
//    private Class<? extends Collection> collectionClass;
//
//    public CollectionValueProvider(String paramName, Parameter parameter, Class<?> targetClass, Class<? extends Collection> collectionClass, MultivaluedParameterExtractor<?> extractor) {
//        this.paramName = paramName;
//        this.parameter = parameter;
//        this.targetClass = targetClass;
//        this.extractor = extractor;
//        this.collectionClass = collectionClass;
//    }
//
//    @Override
//    public Object apply(ContainerRequest containerRequest) {
//
//        MessageBodyWorkers messageBodyWorkers = containerRequest.getWorkers();
//
//        // non null bodypart meta list
//        List<BodyPartInfo> bodyPartInfos = getWholeMultiPart(containerRequest).getBodyParts()
//                .stream()
//                .filter(Objects::nonNull)
//                .filter(b -> b.getFieldName().equals(paramName))
//                .map(BodyPartInfo::new)
//                .collect(Collectors.toList());
//
//        // optimisation if it is a primitive type
//        if (ClassUtils.isPrimitive(targetClass)) {
//            return getValuesFromExtractor(bodyPartInfos);
//        }
//
//        // We try to find a body reader and a value for each
//        // if a reader is found readerValue won't be null, but may be Optional.absent
//        bodyPartInfos.forEach(bi -> bi.readerValue = this.fetchValueFromBodyWorkers(bi, messageBodyWorkers));
//
//        // we now try to find values from extractors
//        List<Object> finalValues = getValuesFromExtractor(bodyPartInfos);
//
//        // we replace messageBodyWorkers values
//        for (int i = 0 ; i < bodyPartInfos.size() ; i++) {
//            BodyPartInfo bi = bodyPartInfos.get(i);
//            if (bi.readerValue != null) {
//                finalValues.set(i, bi.readerValue.orElse(null));
//            }
//        }
//        return finalValues.stream().collect(ClassUtils.getStreamCollector(collectionClass));
//    }
//
//    private List<Object> getValuesFromExtractor(List<BodyPartInfo> bodyPartInfos) {
//        MultivaluedMap paramValuesMap = new NullableMultivaluedHashMap();
//        paramValuesMap.addAll(paramName, bodyPartInfos.stream().map(b -> {
//            // we submit to extractor only values that were not fetched by readers (ie readerValue == null)
//            // and that are not AttachementParts (too heavy)
//            if (b.readerValue == null && b.bodyPart instanceof FormBodyPart ) {
//                return ((FormBodyPart) b.bodyPart).getDataContainer();
////            } else if (b.readerValue.isPresent() && String.class.isAssignableFrom(b.readerValue.get().getClass())) {
////                // if a messageReader returned a String we tolerate it
////                return (String) b.readerValue.get();
//            }
//            return null;
//        }).collect(Collectors.toList()));
//        return (List<Object>) extractor.extract(paramValuesMap);
//
//    }
//
//    private Optional<Object> fetchValueFromBodyWorkers(BodyPartInfo bi, MessageBodyWorkers messageBodyWorkers) {
//
//        MessageBodyReader bodyReader = messageBodyWorkers.getMessageBodyReader(
//                targetClass,
//                ReflectionHelper.getTypeArgument(targetClass, 0),
//                parameter.getAnnotations(),
//                bi.bodyPart.getMediaType());
//
//        if (bodyReader != null) {
//            try {
//                return Optional.ofNullable(bodyReader.readFrom(
//                        targetClass,
//                        ReflectionHelper.getTypeArgument(targetClass, 0),
//                        parameter.getAnnotations(),
//                        bi.bodyPart.getMediaType(),
//                        bi.bodyPart.getHeaders(),
//                        bi.bodyPart.getInputStream()));
//            } catch (final IOException e) {
//                throw new FormDataParamException(e, paramName, parameter.getDefaultValue());
//            }
//        }
//
//
//        return null;
//
//    }
//
//
//
//
//}
