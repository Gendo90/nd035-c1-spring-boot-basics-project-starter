package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.sql.Blob;
import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;

@Mapper
public interface FilesMapper {
	@Select("SELECT * FROM FILES WHERE USERID = #{userId}")
	@Results({
        @Result(property = "id", column = "fileid")
    })
	public List<Files> getFilesByUser(Integer userId);
	
	@Select("SELECT * FROM FILES WHERE FILEID = #{fileId}")
	@Results({
        @Result(property = "id", column = "fileid")
    })
	public Files getFileById(Integer fileId);
	
	@Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES(#{filename}, #{contentType}, #{filesize}, #{filedata}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int addFile(Files newFile);
	
	@Update("UPDATE FILES SET FILENAME = ${newFilename} WHERE NOTEID = #{id}")
	public Files changeFilename(Integer id, String newFilename);
	
	@Delete("DELETE FROM FILES WHERE FILEID = #{fileId}")
	public void deleteFile(Integer fileId);
}
