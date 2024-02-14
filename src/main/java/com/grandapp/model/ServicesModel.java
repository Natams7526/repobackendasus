package com.grandapp.model;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Data @AllArgsConstructor @NoArgsConstructor
public class ServicesModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double precio;

    @Column
    private boolean status;
    
   //Esta columna se usara para determinar cuanto tiempo toma en promedio un servicio.
    @Column
    private int time;
   
}
