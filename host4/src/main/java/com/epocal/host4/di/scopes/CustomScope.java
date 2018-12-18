package com.epocal.host4.di.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by bmate on 3/21/2017.
 * Example of custom scope
 */


@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomScope {

}

