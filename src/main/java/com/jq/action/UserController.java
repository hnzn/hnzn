package com.jq.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jq.dto.UserDTO;
import com.jq.service.user.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/getUser")
	public UserDTO geUserInfo(String name) {
		return userService.getUserInfo(name);
	}
	
	@GetMapping("/getPassword")
	public String getPassword(String name) {
		return userService.getPassWord(name);
	}
}
