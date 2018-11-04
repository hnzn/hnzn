package com.jq.service.user.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jq.dao.UserDAO;
import com.jq.dto.UserDTO;
import com.jq.service.user.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDAO userDAO;
	
	
	public String getPassWord(String name) {
	   return userDAO.getPassword(name)	;
	}
	
	public UserDTO getUserInfo(String name) {
		   return userDAO.getUserDTO(name);
	}
	
	public Set<String> getUserRoles(String name){
		return userDAO.findRoleByUserId(name);
	}
	
	public Set<String> getUserPermission(String name){
		return userDAO.findPermissionByUserId(name);
	}

	@Override
	public Set<String> getAllPermission() {
		return userDAO.findAllPermission();
	}
	
}
