package com.grandapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grandapp.exceptions.OperationNotAllowedException;
import com.grandapp.model.AppointmentModel;
import com.grandapp.requestDto.AppointmentRequestDto;
import com.grandapp.response.AppointmentResponseDto;
import com.grandapp.response.GeneralResponse;
import com.grandapp.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	/**
	 * Este método se encarga de traer un appointment
	 * @param id int Es el identificador del registro para cada Barbero
	 * @return AppointmentModel
	 * @return
	 */
	@PostMapping("/getAppointmentsByBarberId")
	private ResponseEntity<GeneralResponse<AppointmentResponseDto>> getAppointmentsByBarberId(
			@RequestBody AppointmentRequestDto appointment) {
		GeneralResponse<AppointmentResponseDto> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			AppointmentResponseDto data = appointmentService.getAppointmentsByBarberId(appointment);
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			if (data == null) {
				response.setMessage("La Cita no existe");

			}
			log.info("getAppointmentsByBarberId Ejecutado con Éxito >>");

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("findByid Fallido >>" + e);
		}

		return new ResponseEntity<>(response, status);
	}

	/**
	 * Este método se encarga de traer un appointmente por Barbero
	 * 
	 * @param id int Es el identificador del registro para cada Barbero
	 * @return AppointmentModel
	 * @return
	 */
	@GetMapping("/BookingDate")
	private ResponseEntity<GeneralResponse<List<AppointmentModel>>> getAppointmentsByDate(
			@RequestParam(required = false) String timeStart, @RequestParam(required = false) Long barberId,
			@RequestParam(required = false) String phone) {
		GeneralResponse<List<AppointmentModel>> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			List<AppointmentModel> data = appointmentService.getAppointmentsByDate(timeStart, barberId, phone);
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			if (data == null) {
				response.setMessage("La Cita no existe");

			}
			log.info("getAppointmentsByBarberId Ejecutado con Éxito >>");

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("findByid Fallido >>" + e);
		}

		return new ResponseEntity<>(response, status);
	}

	/**
	 * Metodo para crear appointmente
	 * 
	 * @param AppointmentModel appointment - Es el objeto appointmente
	 * @return <AppointmentModel>
	 *
	 */
	@PostMapping
	private ResponseEntity<GeneralResponse<AppointmentModel>> save(@RequestBody AppointmentRequestDto data) {
		GeneralResponse<AppointmentModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			AppointmentModel appointment = appointmentService.save(data);
			response.setMessage("appointmente creado!");
			response.setSuccess(true);
			response.setData(appointment);
			status = HttpStatus.OK;
			log.info("save Ejecutado con Éxito " + appointment);

		} catch (Exception e) {

			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
	}

	/**
	 * Metodo para Editar appointment
	 * 
	 * @param AppointmentModel appointment - Es el objeto appointmente
	 * @return <AppointmentModel>
	 */
	@PutMapping("/{id}")
	private ResponseEntity<GeneralResponse<AppointmentModel>> update(@RequestBody AppointmentRequestDto data,
			@PathVariable("id") Long id) {
		GeneralResponse<AppointmentModel> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			AppointmentModel appointment = appointmentService.update(data, id);
			response.setMessage("appointmente creado!");
			response.setSuccess(true);
			response.setData(appointment);
			status = HttpStatus.OK;
			log.info("save Ejecutado con Éxito " + appointment);

		} catch (Exception e) {

			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<GeneralResponse<Boolean>> deleteCita(@PathVariable("id") Long id) {

		GeneralResponse<Boolean> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			appointmentService.deleteById(id);
			response.setMessage("Cita Eliminada Correctamente");
			response.setSuccess(true);
			status = HttpStatus.OK;
			log.info("se ejecuto DeleteCita correctamente");

		} catch (OperationNotAllowedException e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			log.info("Error delete Cita" + e);
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);

	}

	// metodo para validar si el servicio esta arriba
	@GetMapping("/test")
	private String saludar() {
		log.info("Service Appointment is running!");
		return "Hello! this service is Run";
	}

}
