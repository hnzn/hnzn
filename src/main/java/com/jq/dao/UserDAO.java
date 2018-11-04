package com.jq.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.jq.dto.UserDTO;

@Mapper
public interface UserDAO {

	@Select("select password from user_info where user_id=#{userId}")
	public String getPassword(String userId);
	
	@Select("select id_user_info  idUserInfo," + 
			"  user_id userId," + 
			"  user_name username," + 
			"  password  password," + 
			"  status status from user_info where user_id=#{userId}")
	public UserDTO getUserDTO(String userId);
	
	@Select("select ur.role_code from user_info r,user_role_rel ur where ur.user_id = r.user_id and r.user_id = #{userId}")
	public Set<String> findRoleByUserId(String userId);
	
	@Select("select rp.permission_url from role_perm_rel rp, user_role_rel ur where ur.user_id = #{userId} and rp.role_code = ur.role_code")
	public Set<String> findPermissionByUserId(String userId);
	
	@Select("select permission_url from permission_info")
	public Set<String> findAllPermission();
}
