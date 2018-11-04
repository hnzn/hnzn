package com.jq.common.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jq.dto.UserDTO;
import com.jq.service.user.UserService;

public class UserRealm extends AuthorizingRealm  {
	
	@Autowired
	private UserService userService;
	
	@Override
	public String getName() {
		return "userRealm";
	}

	// 支持什么类型的token
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userId = (String) getAvailablePrincipal(principals);
		UserDTO userDTO = userService.getUserInfo(userId);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(userService.getUserRoles(userDTO.getUserId()));
		authorizationInfo.setStringPermissions(userService.getUserPermission(userDTO.getUserId()));
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken token2 = (UsernamePasswordToken) token;
		String name =  (String) token2.getPrincipal();
		if(StringUtils.isEmpty(name) ) {
			throw new UnknownAccountException();
		}
		if(null == token2.getPassword()) {
			throw new IncorrectCredentialsException();
		}
		String pwd =  String.valueOf(token2.getPassword());
		if(StringUtils.isEmpty(pwd)) {
			throw new IncorrectCredentialsException();
		}
		UserDTO userDTO = userService.getUserInfo(name);
		if(userDTO == null) {
			throw new UnknownAccountException();
		}
		if(!pwd.equals(userDTO.getPassword())) {
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(name, userDTO.getPassword(), this.getName());
	}

}
