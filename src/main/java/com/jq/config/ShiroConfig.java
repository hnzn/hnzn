package com.jq.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

	@Bean
	public JdbcRealm jdbcRealm(DataSource dataSource) {
		JdbcRealm realm = new JdbcRealm();
		realm.setName("jdbcRealm");
		realm.setDataSource(dataSource);
		return realm;		
	}
	
	@Bean
	public IniRealm iniRealm() {
		IniRealm realm = new IniRealm("classpath:shiro.ini");
		realm.setName("iniRealm");
		return realm;		
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
		return securityManager;	
	}
	
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setLoginUrl("/login");
		factoryBean.setSuccessUrl("/wel");
		factoryBean.setUnauthorizedUrl("/test");
		Map<String, String> map = new HashMap<String, String>();
		map.put("/**", "authc");
		factoryBean.setFilterChainDefinitionMap(map);
		return factoryBean;	
	}
}
