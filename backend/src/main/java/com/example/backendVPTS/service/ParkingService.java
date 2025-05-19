//parkingService.java
package com.example.backendVPTS.service;

import com.example.backendVPTS.model.Vehicle;
import com.example.backendVPTS.repository.ParkingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    public Vehicle createParkingEntry(Vehicle vehicle) {
        vehicle.setEntryTime(LocalDateTime.now());
        vehicle.setToken(generateToken(vehicle.getVehicleRegistrationNumber()));
        return parkingRepository.save(vehicle);
    }



    public Vehicle getParkingDetails(String token) {
        System.out.println("Token received: " + token);
        Vehicle vehicle = parkingRepository.findByToken(token);
        System.out.println("Vehicle fetched: " + vehicle);
        return vehicle;
    }


    public Vehicle exitParking(String token) {
        Vehicle vehicle = parkingRepository.findByToken(token);
        if (vehicle == null) {
            throw new RuntimeException("Invalid Token");
        }
        vehicle.setExitTime(LocalDateTime.now());
        vehicle.setParkingFee(calculateParkingFee(vehicle));
        return parkingRepository.save(vehicle);
    }



    private long calculateParkingDuration(LocalDateTime entryTime, LocalDateTime exitTime) {
        return Duration.between(entryTime, exitTime).toHours();
    }


    public Double calculateParkingFee(Vehicle vehicle) {
        if (vehicle.getExitTime() == null) {
            return null; // Parking still in progress
        }
        long minutes = Duration.between(vehicle.getEntryTime(), vehicle.getExitTime()).toMinutes();
        long hours = (minutes + 59) / 60; // Round up to next hour
        return hours * 20.0; // ₹20 per hour
    }

    private String generateToken(String vehicleRegistrationNumber) {
        return vehicleRegistrationNumber.substring(0, 4).toUpperCase() + System.currentTimeMillis() % 10000;
    }


    private Vehicle validateToken(String token) {
        Vehicle vehicle = parkingRepository.findByToken(token);
        if (vehicle == null) {
            throw new RuntimeException("Token not found!");
        }
        return vehicle;
    }
    @PostConstruct
    public void checkDatabaseConnection() {
        System.out.println("Connected to MySQL successfully");
    }

}
