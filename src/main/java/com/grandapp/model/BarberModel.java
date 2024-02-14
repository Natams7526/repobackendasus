package com.grandapp.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "barbers")
@Data @AllArgsConstructor @NoArgsConstructor
public class BarberModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(nullable = true, length = 1500)
	private String image_url;
	
	@Column(nullable = true)
	private String color;
	
}
