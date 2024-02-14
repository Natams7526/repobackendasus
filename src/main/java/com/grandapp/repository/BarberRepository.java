package com.grandapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.grandapp.model.BarberModel;

public interface BarberRepository extends JpaRepository<BarberModel, Long> {

}
