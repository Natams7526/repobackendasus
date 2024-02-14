package com.grandapp.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.grandapp.exceptions.OperationNotAllowedException;
import com.grandapp.model.ClientModel;
import com.grandapp.repository.ClientRepository;
import com.grandapp.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public Page<ClientModel> findAll(Pageable pageable) {
		return clientRepository.findAll(pageable);

	}

	@Override
	public ClientModel save(ClientModel client) throws Exception {
		if ( existsByPhone(client.getPhone())!= null) {
			throw new Exception("El numero de contacto ya esta en uso: " + client.getPhone());
		}
		client.setStatus(true);
		return clientRepository.save(client);
	}

	@Override
	public ClientModel update(Long id, ClientModel client) throws Exception {

		ClientModel clientExist = clientRepository.findById(id)
				.orElseThrow(() -> new Exception("Client doesn`t exist ID: " + id));
		if (clientExist != null) {
			clientExist.setName(client.getName());
			clientExist.setPhone(client.getPhone());
			clientExist.setEmail(client.getEmail());
			clientExist.setStatus(client.isStatus());
		}

		return clientRepository.save(clientExist);
	}

	@Override
	public void deleteById(Long id) {

		try {
			clientRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new OperationNotAllowedException("No se encontro Client!" + e);
		}

	}

	public ClientModel existsByPhone(String phone) throws Exception {
		try {
			return clientRepository.findByPhone(phone);

		} catch (Exception e) {
			throw new Exception("Error en buscar cliente por phone: " + e);
		}
	}

	@Override
	public Optional<ClientModel> findById(Long id) throws Exception {
		try {
			return clientRepository.findById(id);

		} catch (Exception e) {
			throw new Exception("Error en buscar cliente por Id: " + e);
		}
	}

}
