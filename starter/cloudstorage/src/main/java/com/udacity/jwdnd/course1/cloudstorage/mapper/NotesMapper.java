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

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;

@Mapper
public interface NotesMapper {
	@Select("SELECT * FROM NOTES WHERE USERID = #{userId}")
	@Results({
        @Result(property = "title", column = "notetitle"),
        @Result(property = "description", column = "notedescription")
    })
	public List<Notes> getNotesByUser(Integer userId);
	
	@Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{title}, #{description}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int addNote(Notes newNote);
	
	@Update("UPDATE NOTES SET NOTETITLE = ${newTitle} WHERE NOTEID = #{id}")
	public Notes changeNoteTitle(Integer id, String newTitle);
	
	@Update("UPDATE NOTES SET NOTEDESCRIPTION = ${newDescription} WHERE NOTEID = #{id}")
	public Notes changeNoteDescription(Integer id, String newDescription);
	
	@Delete("DELETE FROM NOTES WHERE NOTEID = #{noteId}")
	public void deleteNote(Integer noteId);
}