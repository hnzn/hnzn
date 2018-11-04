package com.jq.service.user;

import java.util.Set;

import com.jq.dto.UserDTO;

public interface UserService {

	public String getPassWord(String name);

	public UserDTO getUserInfo(String name);

	public Set<String> getUserRoles(String name);

	public Set<String> getUserPermission(String name);

	public Set<String> getAllPermission();
}
