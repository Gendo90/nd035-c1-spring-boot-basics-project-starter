package com.udacity.jwdnd.course1.cloudstorage.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.model.User;

@Mapper
public interface UserMapper {
	@Select("SELECT * FROM USERS WHERE USERID = #{userId}")
	public User getUser(Integer userId);
	
	@Select("SELECT * FROM USERS WHERE USERNAME = #{username}")
	public User getUserByName(String username);
	
	@Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int addUser(User newUser);
	
	@Update("UPDATE USERS SET PASSWORD = ${newPassword} WHERE USERID = #{id}")
	public User changePassword(Integer id, String newPassword);
}