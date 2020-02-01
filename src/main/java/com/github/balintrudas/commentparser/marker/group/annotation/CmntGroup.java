package com.github.balintrudas.commentparser.marker.group.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CmntGroup {
    String[] value() default {""};
    String[] inherit() default {};
}
