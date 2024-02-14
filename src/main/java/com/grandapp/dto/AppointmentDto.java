package com.grandapp.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppointmentDto {
	

    private LocalDateTime timeStart;
     private Boolean client;


}
