package com.jq.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jq.common.shiro.TokenManager;

@RestController
public class UserLoginController {

	@RequestMapping(value= "/login" ,method= {RequestMethod.POST,RequestMethod.GET})
	public String login(HttpServletRequest servletRequest,HttpServletResponse servletResponse) throws IOException {
		String exceptionClassName  = (String) servletRequest.getAttribute("shiroLoginFailure");
		String msg = null;
		String code = "0";
		if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
			 msg = "用户名不存在!";
    		 code = "-1";
    	//	 return bulidMap(msg, code);
		}
		if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			msg =("密码错误!");
			code = "-1";
			//return bulidMap(msg, code);		
		}
		if(LockedAccountException.class.getName().equals(exceptionClassName)) {
			msg =("用户锁住!");
			code = "-1";
		//	return bulidMap(msg, code);
		}
		if(AuthenticationException.class.getName().equals(exceptionClassName)) {
			msg ="请登录!";
			code = "-1";
		//	return bulidMap(msg, code);
		}
		if (SecurityUtils.getSubject().isAuthenticated()) {
			WebUtils.redirectToSavedRequest(servletRequest, servletResponse, "/index.html");
		} else {
			WebUtils.redirectToSavedRequest(servletRequest, servletResponse, "/doLogin.html");
		}
		 return  null;
	}
	
	//@GetMapping("/login")
	public String login(@RequestParam String username,@RequestParam String password) {
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
	
	private Map<String, String> bulidMap(String msg, String code){
		Map<String, String> reMap = new HashMap<String, String>();
		reMap.put("code", code);
		 reMap.put("msg", msg);
		return reMap ;
	};
	
    @GetMapping("/logout")
    public String logout() {
    	TokenManager.logout();
    	return "注销";
    }
	
}
