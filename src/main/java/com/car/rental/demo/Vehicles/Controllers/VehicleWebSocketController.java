package com.car.rental.demo.Vehicles.Controllers;

import com.car.rental.demo.Vehicles.Services.VehicleService;
import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VehicleWebSocketController {

    @Autowired
    private VehicleService vehicleService;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

     @MessageMapping("/vehicles/available")
    @SendTo("/topic/available-vehicles")
    public List<VehicleGet> getAvailableVehicles() {
        try {
            return vehicleService.getAllAvailableVehicles();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void notifyVehicleStatusChange() {
        List<VehicleGet> availableVehicles = vehicleService.getAllAvailableVehicles();
        messagingTemplate.convertAndSend("/topic/available-vehicles", availableVehicles);
    }
}