package com.archytasit.jersey.multipart.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To annotate Resource method parameters, or BeanParam fields
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormDataParam {

    /**
     * Value string.
     *
     * @return name of the form field
     */
    String value();

    /**
     * Filter filter.
     *
     * @return restrict field to some kind (form field, attachment field or both
     */
    FormDataType formDataType() default FormDataType.BOTH;

    /**
     * Map content type as map [ ].
     *
     * @return apply custom content type mapping if the sender is unable to set content type correctly
     */
    Map[] mapContentTypeAs() default {};

    /**
     * Filter content type string [ ].
     *
     * @return the string [ ]
     */
    String[] filterContentType() default {};

    /**
     * The enum Filter.
     */
    public enum FormDataType {
        /**
         * Both filter.
         */
        BOTH,
        /**
         * Formfield filter.
         */
        FORMFIELD,
        /**
         * Attachment filter.
         */
        ATTACHMENT
    }



}
