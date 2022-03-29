package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;

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
    
    public Credentials getCredential(int userId, int credentialId) {
    	Credentials retrievedCredential = credentialsMapper.getCredentialById(credentialId);
    	if(retrievedCredential.getUserId() != userId) {
    		System.out.println("Not the current user's credential to retrieve!");
    		
    		return null;
    	}
    	else {    		
    		return retrievedCredential;
    	}
    }
    
    public void updateCredential(int userId, Credentials credential) {
    	int credentialId = credential.getId();
    	if(credentialsMapper.getCredentialById(credentialId).getUserId() != userId) {
    		System.out.println("Not the current user's credential to update!");
    	}
    	else {
    		
    		credentialsMapper.changeUsername(credentialId, credential.getUsername());
    		credentialsMapper.changePassword(credentialId, credential.getPassword());
    		System.out.println("Credential updated successfully!");
    	}
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