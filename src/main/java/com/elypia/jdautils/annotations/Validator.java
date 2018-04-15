package com.elypia.jdautils.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class Validator {

    @Before("@annotation(Limit)")
    public void limit(JoinPoint point) {
        System.out.println(point);
    }
}
