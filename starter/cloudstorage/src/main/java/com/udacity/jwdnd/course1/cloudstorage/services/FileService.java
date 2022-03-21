package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;

@Service
public class FileService {

    private final FilesMapper filesMapper;

    public FileService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public int addFile(int userId, Files file) {
    	file.setUserId(userId);
    	
    	return filesMapper.addFile(file);
    }
    
    public void deleteFile(int userId, int fileId) {
    	if(filesMapper.getFileById(fileId).getUserId() != userId) {
    		System.out.println("Not the current user's file to delete!");
    	}
    	else {
    		filesMapper.deleteFile(fileId);
    		System.out.println("File deleted successfully!");
    	}
    }
    
    public List<Files> getUserFiles(int userId) {
    	return filesMapper.getFilesByUser(userId);
    }
    
    public Files getFile(int userId, int fileId) {
    	Files currFile = filesMapper.getFileById(fileId); 
    	if(currFile.getUserId() != userId) {
    		System.out.println("Not the current user's file to download!");
    		return null;
    	}
    	else {
    		return currFile;
    	}
    }
}