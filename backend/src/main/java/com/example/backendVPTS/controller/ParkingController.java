//ParkingController.java
package com.example.backendVPTS.controller;

import com.example.backendVPTS.model.Vehicle;
import com.example.backendVPTS.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    // Create parking entry
    private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);

    @PostMapping
    public ResponseEntity<Vehicle> createParkingEntry(@RequestBody Vehicle vehicle) {
        try {
            Vehicle createdVehicle = parkingService.createParkingEntry(vehicle);
            return ResponseEntity.status(201).body(createdVehicle);
        } catch (Exception e) {
            // Log the error with detailed information
            logger.error("Error creating parking entry for vehicle: {}", vehicle, e);
            return ResponseEntity.status(500).body(null);
        }
    }


    // Get parking details by token
    @GetMapping("/{token}")
    public ResponseEntity<Vehicle> getParkingDetails(@PathVariable String token) {
        try {
            Vehicle vehicle = parkingService.getParkingDetails(token);
            if (vehicle != null) {
                return ResponseEntity.ok(vehicle);
            } else {
                return ResponseEntity.status(404).body(null); // Not Found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Exit parking by token
    @PostMapping("/exit/{token}")
    public ResponseEntity<Vehicle> exitParking(@PathVariable String token) {
        try {
            Vehicle vehicle = parkingService.exitParking(token);
            if (vehicle != null) {
                return ResponseEntity.ok(vehicle);
            } else {
                return ResponseEntity.status(404).body(null); // Not Found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
