package com.jq.action;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jq.util.TokenManager;

@RestController
public class UserLoginController {

	@GetMapping("/login")
	public String login(String username, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
    		TokenManager.login(token);
    	} catch (UnknownAccountException uae) {
    		return("There is no user with username of " + token.getPrincipal());
		} catch (IncorrectCredentialsException ice) {
			return("Password for account " + token.getPrincipal() + " was incorrect!");
		} catch (LockedAccountException lae) {
			return("The account for username " + token.getPrincipal() + " is locked. "
					+ "Please contact your administrator to unlock it.");		
		} catch (AuthenticationException e) {
			return"请登录";
		}
		return "登录成功";
	}
	
    @GetMapping("/logout")
    public String logout() {
    	TokenManager.logout();
    	return "注销";
    }
	
}
