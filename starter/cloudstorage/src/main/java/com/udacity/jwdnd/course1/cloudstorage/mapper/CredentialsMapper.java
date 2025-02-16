package com.udacity.jwdnd.course1.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;

@Mapper
public interface CredentialsMapper {
	@Select("SELECT * FROM CREDENTIALS WHERE USERID = #{userId}")
	@Results({
        @Result(property = "id", column = "credentialid")
    })
	public List<Credentials> getCredentialsByUser(Integer userId);
	
	@Select("SELECT * FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId}")
	@Results({
        @Result(property = "id", column = "credentialid")
    })
	public Credentials getCredentialById(Integer credentialId);
	
	@Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int addCredential(Credentials newCredential);
	
	@Update("UPDATE CREDENTIALS SET PASSWORD = '${newPassword}' WHERE CREDENTIALID = #{id}")
	public void changePassword(Integer id, String newPassword);
	
	@Update("UPDATE CREDENTIALS SET USERNAME = '${newUsername}' WHERE CREDENTIALID = #{id}")
	public void changeUsername(Integer id, String newUsername);
	
	@Delete("DELETE FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId}")
	public void deleteCredential(Integer credentialId);
}
