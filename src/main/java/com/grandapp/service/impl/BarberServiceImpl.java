package com.grandapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.grandapp.exceptions.OperationNotAllowedException;
import com.grandapp.model.BarberModel;
import com.grandapp.repository.BarberRepository;
import com.grandapp.service.BarberService;


@Service
public class BarberServiceImpl implements BarberService {

	@Autowired
	private BarberRepository barberRepository;



	@Override
	public BarberModel save(BarberModel client) throws Exception {
	
		return barberRepository.save(client);
	}
	
	@Override
	public Optional<BarberModel> findById(Long id) {
		return barberRepository.findById(id);
	}
	
	@Override
	public List<BarberModel> findAll() {
		return barberRepository.findAll();
	}
	
	@Override
	public Optional<BarberModel> deleteById(Long id) throws Exception {
		try {
			Optional<BarberModel> BarberDeleted = barberRepository.findById(id);
			barberRepository.deleteById(id);
			return BarberDeleted;
		} catch (EmptyResultDataAccessException e) {
			throw new OperationNotAllowedException("No se encontro Barber!");
		}
		
	}



}
