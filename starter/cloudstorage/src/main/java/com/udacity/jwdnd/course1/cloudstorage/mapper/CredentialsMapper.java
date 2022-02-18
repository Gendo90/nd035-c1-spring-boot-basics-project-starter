package com.udacity.jwdnd.course1.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;

@Mapper
public interface CredentialsMapper {
	@Select("SELECT * FROM CREDENTIALS WHERE USERID = #{userId}")
	public List<Credentials> getCredentialsByUser(Integer userId);
	
	@Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password), #{userid}")
	@Options(useGeneratedKeys = true, keyProperty = "credentialid")
	public int addCredential(Credentials newCredential);
	
	@Update("UPDATE CREDENTIALS SET PASSWORD = ${newPassword} WHERE CREDENTIALID = #{id}")
	public Credentials changePassword(Integer id, String newPassword);
	
	@Delete("DELETE FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId}")
	public void deleteCredential(Integer credentialId);
}
