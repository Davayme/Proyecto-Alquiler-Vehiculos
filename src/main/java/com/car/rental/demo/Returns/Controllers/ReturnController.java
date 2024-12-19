package com.car.rental.demo.Returns.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Models.Return;
import com.car.rental.demo.Returns.Dtos.CreateReturnDTO;
import com.car.rental.demo.Returns.Dtos.CreateReturnWithoutDamageDTO;
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

    @PostMapping("/without-damage")
    public Return createReturnWithoutDamage(@RequestBody CreateReturnWithoutDamageDTO createReturnWithoutDamageDTO) {
        return returnService.createReturnWithoutDamage(createReturnWithoutDamageDTO);
    }

    @PostMapping("/with-damage")
    public Return createReturnWithDamage(@RequestBody CreateReturnDTO createReturnDTO) {
        return returnService.createReturnWithDamage(createReturnDTO);
    }
}
