package edu.fudan.annotation;

import edu.fudan.rest.CurrentUserMethodArgumentResolver;

import java.lang.annotation.*;

/**
 * User this annotation on parameter(User type to be exact) of methods in controller.
 * It will inject current user object.
 * @see CurrentUserMethodArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CurrentUser {
}
