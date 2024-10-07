package com.example.daobe.common.throttling.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

    String name() default "defaultName";

    int capacity() default 5;

    int refillSeconds() default 1;

    int refillTokens() default 1;
}
