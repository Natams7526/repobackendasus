package com.grandapp.response;


import java.util.List;

import com.grandapp.dto.AppointmentDto;
import com.grandapp.model.BarberModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppointmentResponseDto {

	
	private List<AppointmentDto> AvailabilitySchedule;
    private BarberModel barber;

}
