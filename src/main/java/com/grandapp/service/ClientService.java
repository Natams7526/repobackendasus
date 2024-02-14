package com.grandapp.service;

import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.grandapp.model.ClientModel;

public interface ClientService {

	
	public Page<ClientModel> findAll(Pageable pageable) throws Exception;
	
	public ClientModel save(ClientModel client) throws Exception;

	public ClientModel update(Long id, ClientModel client) throws Exception;
	
	public void deleteById(Long id) throws Exception;
	
	public Optional<ClientModel> findById(Long id) throws Exception;
	
	public ClientModel existsByPhone(String phone) throws Exception;
	

}
