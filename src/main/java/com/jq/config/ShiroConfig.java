package com.jq.config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jq.common.shiro.ShiroSessionListener;
import com.jq.common.shiro.UserRealm;
import com.jq.service.user.UserService;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class ShiroConfig {

	@Autowired
    private UserService userService;
	
	private RedisProperties redisProperties;
	
/*	@Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;*/
	
	public ShiroConfig(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	// @Bean
	public JdbcRealm jdbcRealm(DataSource dataSource) {
		JdbcRealm realm = new JdbcRealm();
		realm.setName("jdbcRealm");
		realm.setDataSource(dataSource);
		return realm;
	}

	// @Bean
	public IniRealm iniRealm() {
		IniRealm realm = new IniRealm("classpath:shiro.ini");
		realm.setName("iniRealm");
		return realm;
	}

	@Bean
	public UserRealm userRealm() {
		UserRealm userRealm = new UserRealm();
		userRealm.setName("userRealm");
		return userRealm;
	}

	@Bean
	public ModularRealmAuthenticator modularRealmAuthenticator() {
		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	@Bean
	public SecurityManager securityManager(List<Realm> realmList) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setAuthenticator(modularRealmAuthenticator());
		securityManager.setRealms(realmList);
		securityManager.setSessionManager(sessionManager());
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setLoginUrl("/login");
		factoryBean.setSuccessUrl("/index.html");
		factoryBean.setUnauthorizedUrl("docc/error/403.html");
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("/doLogin.html", "anon");
		map.put("/login", "authc");
		map.put("/js/**", "anon");
		map.put("/css/**", "anon");
		map.put("/images/**", "anon");
	    userService.getAllPermission().forEach(p->{
	    	 map.put(p, "perms["+p+"]");
	    });
	    map.put("/**", "authc");
		factoryBean.setFilterChainDefinitionMap(map);
		return factoryBean;
	}




	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
/*		redisManager.setHost(redisProperties.getHost());
        redisManager.setPort(redisProperties.getPort());
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(redisProperties.getTimeout());*/ 
/*        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(timeout);*/    
		return redisManager;
	};
	
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager();
		cacheManager.setRedisManager(redisManager());
		return cacheManager;
	}
	
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}
	
	@Bean
	public SessionListener sessionListener() {
		ShiroSessionListener sessionListener = new ShiroSessionListener();
		return sessionListener;
	};
	
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO());
		sessionManager.setSessionListeners(Arrays.asList(sessionListener()));
		return sessionManager;
	}
}
