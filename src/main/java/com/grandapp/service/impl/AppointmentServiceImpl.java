package com.grandapp.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.grandapp.dto.AppointmentDto;
import com.grandapp.exceptions.OperationNotAllowedException;
import com.grandapp.model.AppointmentModel;
import com.grandapp.model.BarberModel;
import com.grandapp.model.ClientModel;
import com.grandapp.repository.AppointmentRepository;
import com.grandapp.requestDto.AppointmentRequestDto;
import com.grandapp.response.AppointmentResponseDto;
import com.grandapp.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private BarberServiceImpl barberService;

	@Autowired
	private ClientServiceImpl clientService;

	@Override
	public Optional<AppointmentModel> findById(Long id) {
		return null;
		// appointmentRepository.findById(id);
	}

	@Override
	public AppointmentModel save(AppointmentRequestDto appointment) throws Exception {

		if (validateSchedule(appointment)) {
			throw new Exception("Cita esta reservada");
		} else {
			BarberModel barber = barberService.findById(appointment.getBarberId()).get();

			AppointmentModel data = new AppointmentModel();
	

			ClientModel clientExist = clientService.existsByPhone(appointment.getClient().getPhone());
			if (clientExist == null) {
				ClientModel clientSave = clientService.save(appointment.getClient());
				data.setClient(clientSave);
			} else {
				data.setClient(clientExist);
			}

			data.setBarber(barber);
			data.setTimeStart(appointment.getTimeStart());
//		data.setTimeEnd(appointment.getTimeStart());
			return appointmentRepository.save(data);

		}

	}

	@Override
	public AppointmentModel update(AppointmentRequestDto appointment, Long id) throws Exception {

		Optional<AppointmentModel> citaold = appointmentRepository.findById(id);
		
		if (citaold.get() != null) {
			AppointmentModel data = new AppointmentModel();
			data.setId(id);// seteamos el id que nos envian
			// validamos que el barbero exista
			Optional<BarberModel> barber = barberService.findById(appointment.getBarberId());
			data.setBarber(barber.get());

			// se valida si el cliente existe o debemos crearlo
			ClientModel clientExist = clientService.existsByPhone(appointment.getClient().getPhone());
			if (clientExist == null) {// si el cliente no existe debemos llamar a Save para crearlo
				ClientModel clientSave = clientService.save(appointment.getClient());
				data.setClient(clientSave);
			} else {// si ya existe, simplemente lo cargamos al nuevo set para actualizar el
					// appointment
				data.setClient(clientExist);
			}

			data.setTimeStart(appointment.getTimeStart());
			return appointmentRepository.save(data);

		}else {
			throw new Exception("no se pudo actualizar cita, no se encontro cita");
		}

	}

	/*
	 * Metodo para Validar que no exista duplicidad en cita con Dia-hora y barbero
	 * ir asignando
	 */
	private Boolean validateSchedule(AppointmentRequestDto appointment) {
		List<AppointmentModel> appointmentList = listAppointmentsByBarberAndDate(appointment);

		for (AppointmentModel appointmentBusy : appointmentList) {
			if (appointment.getBarberId().equals(appointmentBusy.getBarber().getId())) {
				if (appointment.getTimeStart().equals(appointmentBusy.getTimeStart())) {

					log.info("Agenda ya existe");
					return true;

				}
			}
		}
		return false;

	}

	@Override
	public Page<AppointmentModel> findAll(Pageable pageable) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	// Método para obtener citas de un barbero específico
	@Override
	public AppointmentResponseDto getAppointmentsByBarberId(AppointmentRequestDto appointment) throws Exception {
		try {
			
			List<AppointmentModel> appointmentList = listAppointmentsByBarberAndDate(appointment);

			List<AppointmentDto> scheduleList = generateEmptySchedule(appointment.getTimeStart());

			for (AppointmentDto appointmentNew : scheduleList) {
				for (AppointmentModel appointmentBusy : appointmentList) {
					if (appointmentNew.getTimeStart().equals(appointmentBusy.getTimeStart())) {

						appointmentNew.setClient(true);
					}
				}  
			}

			AppointmentResponseDto schedule = new AppointmentResponseDto();
			schedule.setBarber(appointmentList.get(0).getBarber());

			schedule.setAvailabilitySchedule(scheduleList);

			return schedule;
		} catch (Exception e) {
			throw new Exception("error en metodo getAppointmentsByBarber - Barbero no existe");
		}
	}

	/*
	 * Metodo para generar la lista con todas los turnos vacios para posteriormente
	 * ir asignando
	 */
	private List<AppointmentDto> generateEmptySchedule(LocalDateTime appointment) {
		LocalDateTime firstHour = appointment.withHour(9);
		return IntStream.range(0, 10).mapToObj(i -> new AppointmentDto(firstHour.withHour(9 + i), null))
				.collect(Collectors.toList());
	}

	/*
	 * Metodo para traer la lista de agendamientos asignados
	 * 
	 */
	private List<AppointmentModel> listAppointmentsByBarberAndDate(AppointmentRequestDto appointment) {

		return appointmentRepository.getAppointmentsByBarberId(appointment.getBarberId(), appointment.getTimeStart());
	}

	// Método para obtener citas de un Dia en especifico
		@Override
		public List<AppointmentModel> getAppointmentsByDate(String timeStart, Long barberId, String phone)
				throws Exception {

			Specification<AppointmentModel> customQuery = Specification.where(null);
			
			ClientModel clientExist = clientService.existsByPhone(phone);
			
			
			if (timeStart != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(timeStart, formatter);
				customQuery = customQuery.and((root, query, builder) -> clientExist != null
		                ? builder.greaterThanOrEqualTo(builder.function("date", LocalDate.class, root.get("timeStart")), date)
		                : builder.equal(builder.function("date", LocalDate.class, root.get("timeStart")), date));
			}
			if (barberId != null && barberId != 0) {
				customQuery = customQuery.and((root, query, builder) -> builder.equal(root.get("barber"), barberId));
			}

			if (phone != null) {
				
				if (clientExist!= null) {
					customQuery = customQuery
							.and((root, query, builder) -> builder.equal(root.get("client"), clientExist.getId()));
				}else {
					throw new Exception("Error getAppointmentsByDate : Client does not exist" );
				}
			}

			Sort sortByTimeStart = Sort.by(Sort.Direction.ASC, "timeStart");

			try {
				return appointmentRepository.findAll(customQuery, sortByTimeStart);
			} catch (Exception e) {
				throw new Exception("error en metodo getAppointmentsByDate" + e.getMessage());
			}
		}

	@Override
	public void deleteById(Long id) {

		try {
			appointmentRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new OperationNotAllowedException("No se encontro Appointment!" + e);
		}

	}

}
