package com.jq.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;

public class TokenManager {

	public static void login(AuthenticationToken token) {
		SecurityUtils.getSubject().login(token);
	}
	
	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}
	
}
