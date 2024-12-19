package com.car.rental.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "returns")
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    @OneToOne
    @JoinColumn(name = "rentalId", nullable = false)
    private Rental rental;

    private Date returnDate;

    private double additionalCharges;

    private double totalReturnAmount; // totalAmount + additionalCharges

    @OneToMany(mappedBy = "returnRecord", cascade = CascadeType.ALL)
    private List<ReturnDetail> details;


}