package com.grandapp.service;

import java.util.List;
import java.util.Optional;

import com.grandapp.model.BarberModel;



public interface BarberService {

	public BarberModel save(BarberModel barber) throws Exception;

	public Optional<BarberModel> findById(Long id) throws Exception;
	
	List<BarberModel> findAll() throws Exception;

	Optional<BarberModel> deleteById(Long id) throws Exception;
}
