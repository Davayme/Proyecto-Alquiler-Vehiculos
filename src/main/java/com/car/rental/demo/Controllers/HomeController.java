package com.car.rental.demo.Controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Como tan muchacho, yo a utede lo veo muy bien";
    }
}
