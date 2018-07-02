package com.ppdai.framework.raptor.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RaptorInterface {

    String appId() default "";

    String appName() default "";

    String version() default "";

    String protoFile() default "";

    String library() default "";

    String crc32() default "";

}
