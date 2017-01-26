package com.stormpath.shiro.samples.springboot;

import com.stormpath.shiro.samples.springboot.common.dao.DefaultStormtrooperDao;
import com.stormpath.shiro.samples.springboot.common.dao.StormtrooperDao;
import com.stormpath.shiro.samples.springboot.common.model.ErrorMessage;
import com.stormpath.shiro.samples.springboot.controllers.NotFoundException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@SpringBootApplication
public class SpringBootApp {

    private static Logger log = LoggerFactory.getLogger(SpringBootApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleException(UnauthenticatedException e) {
        log.debug("{} was thrown", e.getClass(), e);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleException(AuthorizationException e) {
        log.debug("{} was thrown", e.getClass(), e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMessage handleException(NotFoundException e) {
        String id = e.getMessage();
        return new ErrorMessage("Trooper Not Found: "+ id +", why aren't you at your post? "+ id +", do you copy?");
    }

    @Bean
    protected StormtrooperDao stormtrooperDao() {
        return new DefaultStormtrooperDao();
    }

    @Bean
    public Realm realm() {

        // uses 'classpath:shiro-users.properties' by default
        PropertiesRealm realm = new PropertiesRealm();

        // Caching isn't needed in this example, but we can still turn it on
        realm.setCachingEnabled(true);
        return realm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // use permissive to NOT require authentication, our controller Annotations will decide that
//        chainDefinition.addPathDefinition("/**", "noSessionCreation, authcBasic[permissive]");
        chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");
        return chainDefinition;
    }

    @Bean
    public CacheManager cacheManager() {
        // Caching isn't needed in this example, but we will use the MemoryConstrainedCacheManager for this example.
        return new MemoryConstrainedCacheManager();
    }

}
