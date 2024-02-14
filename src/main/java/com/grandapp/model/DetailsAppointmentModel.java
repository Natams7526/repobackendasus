package com.grandapp.model;

import java.io.Serializable;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments_det")
@Data @AllArgsConstructor @NoArgsConstructor
public class DetailsAppointmentModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "client_id")
    private ClientModel client;
	
	@ManyToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentModel appointment;

}
