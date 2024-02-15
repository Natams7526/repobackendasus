package com.grandapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.grandapp.model.AppointmentModel;
import com.grandapp.requestDto.AppointmentRequestDto;
import com.grandapp.response.AppointmentResponseDto;



public interface AppointmentService {

	public AppointmentModel save(AppointmentRequestDto appointment) throws Exception;
	
	AppointmentModel update(AppointmentRequestDto appointment, Long id) throws Exception;
	
	public Optional<AppointmentModel> findById(Long id) throws Exception;
	
	public Page<AppointmentModel> findAll(Pageable pageable) throws Exception;

	AppointmentResponseDto getAppointmentsByBarberId(AppointmentRequestDto appointment) throws Exception;
	
	List<AppointmentModel> getAppointmentsByDate(String timeStart, Long barberId, String phone) throws Exception;

	void deleteById(Long id);

	
	

	
}
