package com.example.backendVPTS.repository;

import com.example.backendVPTS.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByToken(String token);
}
