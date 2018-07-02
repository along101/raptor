package com.ppdai.framework.raptor.annotation;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RaptorField {

    int order();

    String name() default "";

    String fieldType();

    String keyType() default "string";

    boolean repeated() default false;

    boolean isMap() default false;

    String oneof() default "";
    
}
