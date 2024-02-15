package com.grandapp.requestDto;

import java.time.LocalDateTime;

import com.grandapp.model.ClientModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class AppointmentRequestDto {
	

	    private LocalDateTime timeStart;
	    private Long barberId;
	    private ClientModel client;
		public LocalDateTime getTimeStart() {
			return timeStart;
		}
		public void setTimeStart(LocalDateTime timeStart) {
			this.timeStart = timeStart;
		}
		public Long getBarberId() {
			return barberId;
		}
		public void setBarberId(Long barberId) {
			this.barberId = barberId;
		}
		public ClientModel getClient() {
			return client;
		}
		public void setClient(ClientModel client) {
			this.client = client;
		} 
	    

	    // Constructores, getters y setters Son realizados por lombok



}
