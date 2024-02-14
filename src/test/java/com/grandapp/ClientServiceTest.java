package com.grandapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.grandapp.model.ClientModel;
import com.grandapp.repository.ClientRepository;
import com.grandapp.service.impl.ClientServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private ClientServiceImpl clientService;

	@Test
	public void testGetClientsPageable() {
		when(clientRepository.findAll(any(Pageable.class)))
				.thenReturn(new PageImpl<>(Arrays.asList(new ClientModel(), new ClientModel(), new ClientModel())));

		// Llamada al método que queremos probar
		Page<ClientModel> result = clientService.findAll(PageRequest.of(0, 10, Sort.by("id").ascending()));

		// Verificación de los resultados
		assertEquals(3, result.getContent().size());

	}

	@Test
	public void testSaveClient() throws Exception {
		// Datos de prueba
		ClientModel clientMock = new ClientModel();

		clientMock.setName("John Doe");
		clientMock.setEmail("john.doe@example.com");
		clientMock.setPhone("12345");

		// Mock del repositorio para simular el comportamiento de guardar
		when(clientRepository.save(any(ClientModel.class))).thenAnswer(invocation -> {
			ClientModel ClientSaved = invocation.getArgument(0);
			// Simulamos la asignación de un ID al Client guardado
			ClientSaved.setId(1L);
			return ClientSaved;
		});

		// Llamada al método que queremos probar
		ClientModel ClientCreated = clientService.save(clientMock);

		// Verificación de que se llamó al método save del repositorio
		verify(clientRepository, times(1)).save(any(ClientModel.class));

		// Verificación de los resultados
		assertEquals(1L, ClientCreated.getId()); // Asumimos que el ID debería ser 1
		assertEquals(clientMock.getName(), ClientCreated.getName());
		assertEquals(clientMock.getEmail(), ClientCreated.getEmail());
		assertEquals(clientMock.getPhone(), ClientCreated.getPhone());
	}

	@Test
	public void testUpdateClient() throws Exception {
		// Datos de prueba
		Long clientId = 1L;
		ClientModel clientMock = new ClientModel();
		clientMock.setName("John Doe");
		clientMock.setEmail("john.doe@example.com");
		clientMock.setPhone("123456789");
		clientMock.setStatus(true);

		// Crear un objeto Usuario existente
		ClientModel clientExist = new ClientModel(clientId, "Nombre Existente", "987654321",
				"email.existente@example.com", true);

		// Mock del repositorio para simular el comportamiento de obtener un usuario
		// existente
		when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientExist));

		// Configurar el mock del repositorio para simular el comportamiento de
		// actualizar (save)
		when(clientRepository.save(any(ClientModel.class))).thenAnswer(invocation -> {
			ClientModel clientUpdated = invocation.getArgument(0);

			// Simular la actualización asignando valores al usuario existente
			clientExist.setName(clientUpdated.getName());
			clientExist.setEmail(clientUpdated.getEmail());
			clientExist.setPhone(clientUpdated.getPhone());

			return clientExist;
		});

		// Llamada al método que queremos probar
		ClientModel clientUpdated = clientService.update(1L, clientMock);

		// Verificación de que se llamó al método findById del repositorio
		verify(clientRepository, times(1)).findById(1L);

		// Verificación de que se llamó al método save del repositorio
		verify(clientRepository, times(1)).save(any(ClientModel.class));

		// Verificación de los resultados
		assertEquals("John Doe", clientUpdated.getName());
		assertEquals("john.doe@example.com", clientUpdated.getEmail());
		assertEquals("123456789", clientUpdated.getPhone());
		assertEquals(true, clientUpdated.isStatus());

	}

	@Test
	public void testUpdateClienteNoExistente() throws Exception {
		// Datos de prueba
		Long clientId = 1L;
		ClientModel clientMock = new ClientModel(1L, "Nombre Existente", "987654321", "email.existente@example.com",
				false);

		// Mock del repositorio para simular que el usuario no se encuentra
		when(clientService.findById(clientId)).thenReturn(Optional.empty());

		// Llamada al método que queremos probar
		Exception exception = assertThrows(Exception.class, () -> clientService.update(clientId, clientMock));

		// Verificación de que se llamó al método findById del repositorio
		verify(clientRepository, times(1)).findById(clientId);

		// Verificación de que no se llamó al método save del repositorio
		verify(clientRepository, never()).save(any(ClientModel.class));

		// Verificación del mensaje de excepción
		assertEquals("Client doesn`t exist ID: " + clientId, exception.getMessage());
	}

}
