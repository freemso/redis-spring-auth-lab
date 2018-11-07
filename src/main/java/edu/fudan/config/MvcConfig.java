package edu.fudan.config;

import edu.fudan.repository.UserRepository;
import edu.fudan.rest.AuthorizationInterceptor;
import edu.fudan.rest.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    private final AuthorizationInterceptor authorizationInterceptor;

    @Autowired
    public MvcConfig(UserRepository userRepository, AuthorizationInterceptor authorizationInterceptor) {
        this.userRepository = userRepository;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserMethodArgumentResolver(userRepository));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor);
    }

}
