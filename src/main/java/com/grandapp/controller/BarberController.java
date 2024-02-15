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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grandapp.exceptions.OperationNotAllowedException;
import com.grandapp.model.BarberModel;
import com.grandapp.response.GeneralResponse;
import com.grandapp.service.BarberService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/barbers")
@Slf4j
public class BarberController {

	@Autowired
	private BarberService barberService;

	/**
	 * Metodo para crear Barbero 
	 * 
	 * @param BarberModel client - Es el objeto barber
	 * @return <BarberModel>
	 */
	@PostMapping
	private ResponseEntity<GeneralResponse<BarberModel>> save(@RequestBody BarberModel client) {
		GeneralResponse<BarberModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {

			BarberModel cliente = barberService.save(client);
			response.setMessage("Registro creado!");
			response.setSuccess(true);
			response.setData(cliente);
			status = HttpStatus.OK;
			log.info("save Ejecutado con Éxito " + cliente);

		} catch (Exception e) {

			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("error generado " + e.getMessage());
		}
		return new ResponseEntity<>(response, status);
	}

	
	/**
	 * Este método se encarga de traer un cliente buscado por ID
	 * 
	 * @param id int Es el identificador del registro para cada usuario
	 * @return BarberModel
	 * @param <T>
	 * @return
	 */
	@GetMapping("/{id}")
	private ResponseEntity<GeneralResponse<BarberModel>> findById(@PathVariable("id") Long id) {
		GeneralResponse<BarberModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			BarberModel data = barberService.findById(id).get();
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			if (data == null) {
				response.setMessage("El Profesional " + id + " no existe");
				log.info("El Staff " + id + " no Existe!");

			}
			log.info("findByid Ejecutado con Éxito >>" + id);

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("findByid Fallido >>" + e);
		}

		return new ResponseEntity<>(response, status);
	}

	/**
	 * Este método se encarga de traer la lista de barberos
	 * 
	 * @return List<BarberModel>
	 */
	@GetMapping
	private ResponseEntity<GeneralResponse<List<BarberModel>>> findAll() {
		GeneralResponse<List<BarberModel>> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			List<BarberModel> data = barberService.findAll();
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			log.info("findAll Ejecutado con Éxito");
		} catch (Exception e) {
			response.setMessage("Error al obtener la lista de barberos: " + e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("findAll Fallido", e);
		}

		return new ResponseEntity<>(response, status);
	}

	//metodo para eliminar registro barbero
	
		@DeleteMapping("/{id}")
		private ResponseEntity<GeneralResponse<BarberModel>> delete(@PathVariable("id") Long id) {

			GeneralResponse<BarberModel> response = new GeneralResponse<>();
			HttpStatus status = null;
			try {
				BarberModel data = barberService.deleteById(id).get();
				response.setMessage("Barbero eliminado correctamente");
				response.setData(data);
				response.setSuccess(true);
				status = HttpStatus.OK;
				log.info("Delete barber executed successfully");

			} catch (OperationNotAllowedException e) {
				response.setMessage(e.getMessage());
				response.setSuccess(false);
				status = HttpStatus.NOT_FOUND;
			} catch (Exception e) {
				log.info("Error delete client" + e);
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
		log.info("estamos en el test del cotrolador de Barbers");
		return "Hello! this service is Run";
	}

}
