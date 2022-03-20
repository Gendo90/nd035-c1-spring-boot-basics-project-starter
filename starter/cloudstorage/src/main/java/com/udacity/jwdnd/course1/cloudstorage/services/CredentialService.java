package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;

@Service
public class CredentialService {

    private final CredentialsMapper credentialsMapper;

    public CredentialService(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
    }

    public int addCredential(int userId, Credentials credential) {
    	credential.setUserId(userId);
    	
    	return credentialsMapper.addCredential(credential);
    }
    
    public void deleteCredential(int userId, int credentialId) {
    	if(credentialsMapper.getCredentialById(credentialId).getUserId() != userId) {
    		System.out.println("Not the current user's credential to delete!");
    	}
    	else {
    		System.out.println("Note deleted successfully!");
    		credentialsMapper.deleteCredential(credentialId);
    	}
    }
    
    public List<Credentials> getUserCredentials(int userId) {
    	return credentialsMapper.getCredentialsByUser(userId);
    }
}