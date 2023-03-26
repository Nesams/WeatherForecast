package com.ot.weather.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor

@Table(name = "wind")
public class Wind {

    public Wind(String name, String nightOrDay, String direction, double speedMin, double speedMax, String gust) {
        this.name = name;
        this.nightOrDay = nightOrDay;
        this.direction = direction;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.gust = gust;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "night_or_day")
    private String nightOrDay;

    @Column(name = "direction")

    private String direction;

    @Column(name = "speed_min")

    private double speedMin;

    @Column(name = "speed_max")
    private double speedMax;

    @Column(name = "gust")
    private String gust;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "general_forecast_id")
    private GeneralForecast generalForecast;
}
