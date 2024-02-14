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
	    

	    // Constructores, getters y setters Son realizados por lombok



}
