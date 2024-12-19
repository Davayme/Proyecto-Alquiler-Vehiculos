package com.car.rental.demo.Returns.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Returns.Dtos.RentalDtoReturns;
import com.car.rental.demo.Returns.Services.ReturnService;

@RestController
@RequestMapping("/returns")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    @GetMapping
    public List<RentalDtoReturns> getAllRentals() {
        return returnService.getAllRentals();
    }
}
