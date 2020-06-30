package io.lombocska.app.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockSecurityContextFactory.class)
public @interface WithMockAppUser {

	String userId() default "testUserId";

	String[] authorities() default {"USER"};
}
