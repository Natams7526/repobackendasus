package com.grandapp.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class AppointmentRequestDto {
	

	    private Date timeStart;
	    private Date timeEnd;
	    private Long barberId;
	    private Long clientId;

	    // Constructores, getters y setters Son realizados por lombok



}
