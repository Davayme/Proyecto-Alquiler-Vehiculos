package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String reportType; // Vehicle Usage, Revenue, etc.

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Lob
    private String content;

    // Getters and setters
}