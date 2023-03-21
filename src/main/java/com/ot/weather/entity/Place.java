package com.ot.weather.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor

@Table(name = "places")
public class Place {

    public Place(String name, String phenomenon, double tempMin, double tempMax) {
        this.name = name;
        this.phenomenon = phenomenon;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "phenomenon")
    private String phenomenon;

    @Column(name = "temp_min")
    private double tempMin;

    @Column(name = "temp_max")
    private double tempMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_forecast_id")
    private GeneralForecast generalForecast;
}
