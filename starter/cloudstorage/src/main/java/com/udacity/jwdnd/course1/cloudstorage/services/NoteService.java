package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;

@Service
public class NoteService {

    private final NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public int addNote(int userId, Notes note) {
    	note.setUserId(userId);
    	
    	System.out.println(note.getTitle());
    	System.out.println(note.getDescription());
    	
    	return this.notesMapper.addNote(note);
    }
    
    public List<Notes> getUserNotes(int userId) {
    	return this.notesMapper.getNotesByUser(userId);
    }
}