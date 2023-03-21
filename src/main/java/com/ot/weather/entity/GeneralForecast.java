package com.ot.weather.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "general_forecast")
@Setter @Getter @NoArgsConstructor
public class GeneralForecast {

    public GeneralForecast(String date, String nightPhenomenon, double nightTempMin, double nightTempMax, String nightText,
                           String dayPhenomenon, double dayTempMin, double dayTempMax, String dayText, String seaOverView, String peipsi) {
        this.date = date;
        this.nightPhenomenon = nightPhenomenon;
        this.nightTempMin = nightTempMin;
        this.nightTempMax = nightTempMax;
        this.nightText = nightText;
        this.dayPhenomenon = dayPhenomenon;
        this.dayTempMin = dayTempMin;
        this.dayTempMax = dayTempMax;
        this.dayText = dayText;
        this.seaOverView = seaOverView;
        this.peipsi = peipsi;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "night_phenomenon")
    private String nightPhenomenon;

    @Column(name = "night_temp_min")
    private double nightTempMin;

    @Column(name = "night_temp_max")
    private double nightTempMax;

    @Column(name = "night_text")
    private String nightText;

    @Column(name = "day_phenomenon")
    private String dayPhenomenon;

    @Column(name = "day_temp_min")
    private double dayTempMin;

    @Column(name = "day_temp_max")
    private double dayTempMax;

    @Column(name = "day_text")
    private String dayText;

    @Column(name = "sea_overview")
    private String seaOverView;

    @Column(name = "peipsi")
    private String peipsi;
}
