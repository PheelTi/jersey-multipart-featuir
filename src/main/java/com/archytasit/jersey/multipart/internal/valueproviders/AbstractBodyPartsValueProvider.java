package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.BodyPart;
import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.MultiPart;
import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Abstract body parts value provider.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractBodyPartsValueProvider<T> extends AbstractMultiPartValueProvider<T> {


    private String paramName;
    /**
     * The Marker.
     */
    protected FormDataParam marker;
    /**
     * The Parameter.
     */
    protected Parameter parameter;
    private Predicate<FormDataBodyPart> partNatureFilter;

    /**
     * Instantiates a new Abstract body parts value provider.
     *
     * @param parameter the parameter
     */
    public AbstractBodyPartsValueProvider(Parameter parameter) {
        this.parameter = parameter;
        if (parameter.getSourceAnnotation().annotationType() == FormDataParam.class) {
            this.marker = (FormDataParam) parameter.getSourceAnnotation();
        } else {
            this.marker = parameter.getAnnotation(FormDataParam.class);
        }
        this.paramName = this.marker.value();
        this.partNatureFilter = preparePartNatureFilter(this.marker.formDataType());
    }


    @Override
    protected final T apply(ContainerRequest request, MultiPart entity) {


        List<FormDataBodyPart> enhancedBodyParts = findFields(entity, paramName).stream()
                .filter(this.partNatureFilter)
                .collect(Collectors.toList());
        return applyToBodyParts(request, enhancedBodyParts);
    }

    /**
     * as stated in https://tools.ietf.org/html/rfc7578#section-4.3
     * @param part
     * @return
     */
    private List<FormDataBodyPart> getNestedParts(BodyPart part) {
        List<FormDataBodyPart> result = new ArrayList<>();
        if (FormDataBodyPart.class.isAssignableFrom(part.getClass())) {
            result.add((FormDataBodyPart) part);
        } else if (MultiPart.class.isAssignableFrom(part.getClass())) {
            ((MultiPart)part).getBodyParts().stream().map(this::getNestedParts).forEach(result::addAll);
        }
        return result;

    }

    private List<FormDataBodyPart> findFields(MultiPart entity, String... names) {


        return entity.getBodyParts().stream().filter(Objects::nonNull)
                .filter(p -> paramName.equals(p.getName()))
                .filter(f -> FormDataBodyPart.class.isAssignableFrom(f.getClass()))
                .map(this::getNestedParts).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * Apply to body parts t.
     *
     * @param request           the request
     * @param enhancedBodyParts the enhanced body parts
     * @return the t
     */
    protected abstract T applyToBodyParts(ContainerRequest request, List<FormDataBodyPart> enhancedBodyParts);


    /**
     * Gets parameter name.
     *
     * @return the parameter name
     */
    protected String getParameterName() {
        return paramName;
    }


    private Predicate<FormDataBodyPart> preparePartNatureFilter(FormDataParam.FormDataType filter) {
        switch (filter) {
            case FORMFIELD:
                return FormDataBodyPart::isFormField;
            case ATTACHMENT:
                Predicate<FormDataBodyPart> predicate = FormDataBodyPart::isFormField;
                return predicate.negate();
            default :
                return (b) -> true;
        }
    }


}

