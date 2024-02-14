package com.grandapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data @AllArgsConstructor @NoArgsConstructor
public class AppointmentModel implements Serializable {
	
	
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 */

		@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private BarberModel barber;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientModel client;

}
