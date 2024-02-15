package com.grandapp.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.grandapp.model.ClientModel;
import com.grandapp.response.GeneralResponse;
import com.grandapp.service.ClientService;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clients")
@Slf4j
public class ClientController {

	@Autowired
	private ClientService clientService;

	@ApiOperation(value = "Listar Clientes", notes = "listamos los clientes de forma paginada")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = ClientModel.class),
			@ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
			@ApiResponse(code = 500, message = "Error inesperado del sistema") })
	@GetMapping
	private ResponseEntity<GeneralResponse<Page<ClientModel>>> findAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String[] sort) {
		GeneralResponse<Page<ClientModel>> response = new GeneralResponse<>();
		HttpStatus status = null;
		Page<ClientModel> data = null;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
		try {
			data = clientService.findAll(pageable);
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			log.info("findAll Ejecutado con Exito");

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.warn("findAll Fallido >>" + e);
		}

		return new ResponseEntity<>(response, status);
	}

	/**
	 * Este método se encarga de traer un cliente buscado por ID
	 * 
	 * @param id int Es el identificador del registro para cada usuario
	 * @return Clientmodel
	 * @param <T>
	 * @return
	 */
	@GetMapping("/{id}")
	private ResponseEntity<GeneralResponse<ClientModel>> findById(@PathVariable("id") Long id) {
		GeneralResponse<ClientModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			ClientModel data = clientService.findById(id).get();
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			if (data == null) {
				response.setMessage("El cliente " + id + " no existe");
				log.info("El Cliente " + id + " no Existe!");

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
	 * Este método se encarga de traer un cliente buscado por Phone
	 * 
	 * @param phone String Es el parametro que pertenece al cliente y debe ser unico.
	 * @return Clientmodel
	 */
	@GetMapping("/find-client-x-Phone/{phone}")
	private ResponseEntity<GeneralResponse<ClientModel>> findByPhone(@PathVariable("phone") String phone) {
		GeneralResponse<ClientModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {
			ClientModel data = clientService.existsByPhone(phone);
			response.setSuccess(true);
			response.setData(data);
			status = HttpStatus.OK;
			if (data == null) {
				response.setMessage("El cliente con telefono" + phone + " no existe");
				log.info("El Cliente con telefono: " + phone + " no Existe!");

			}
			log.info("findByPhone Ejecutado con Éxito >>" + phone);

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("findByPhone Fallido >>" + e);
		}

		return new ResponseEntity<>(response, status);
	}

	/**
	 * Metodo para crear Cliente
	 * 
	 * @param ClientModel client - Es el objeto cliente
	 * @return <Clientmodel>
	 
	 */
	@PostMapping
	private ResponseEntity<GeneralResponse<ClientModel>> save(@RequestBody ClientModel client) {
		GeneralResponse<ClientModel> response = new GeneralResponse<>();
		HttpStatus status = null;

		try {

			ClientModel cliente = clientService.save(client);
			response.setMessage("Cliente creado!");
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
	 * Metodo para Actualizar Cliente
	 * 
	 * @param Long id - ClientModel client - Es el objeto cliente
	 * @return <Clientmodel>
	 
	 */
	@PutMapping("/{id}")
	private ResponseEntity<GeneralResponse<ClientModel>> update(@PathVariable("id") Long id,
			@RequestBody ClientModel client) {
		GeneralResponse<ClientModel> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			ClientModel clientUpdated = clientService.update(id, client);
			response.setMessage("Cliente actualizado!");
			response.setSuccess(true);
			response.setData(clientUpdated);
			status = HttpStatus.OK;
			log.info("Update Ejecutado con Éxito " + clientUpdated);

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			status = HttpStatus.NOT_FOUND;
			log.error("error generado " + e.getMessage());
		}

		return new ResponseEntity<>(response, status);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<GeneralResponse<Boolean>> delete(@PathVariable("id") Long id) {

		GeneralResponse<Boolean> response = new GeneralResponse<>();
		HttpStatus status = null;
		try {
			clientService.deleteById(id);
			response.setMessage("Cliente eliminado correctamente");
			response.setSuccess(true);
			status = HttpStatus.OK;
			log.info("Delete client ejecuted successfully");

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
		log.info("Service clients is running!");
		return "Hello! this service is Run";
	}

}
