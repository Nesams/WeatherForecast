package com.ot.weather.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "general_forecast")
@Setter @Getter @NoArgsConstructor
public class GeneralForecast {

    public GeneralForecast(String date, String nightPhenomenon, double nightTempMin, double nightTempMax,
                           String nightText, String dayPhenomenon, double dayTempMin, double dayTempMax, String dayText,
                           String seaNightOverView, String seaDayOverView, String peipsiNight, String peipsiDay) {
        this.date = date;
        this.nightPhenomenon = nightPhenomenon;
        this.nightTempMin = nightTempMin;
        this.nightTempMax = nightTempMax;
        this.nightText = nightText;
        this.dayPhenomenon = dayPhenomenon;
        this.dayTempMin = dayTempMin;
        this.dayTempMax = dayTempMax;
        this.dayText = dayText;
        this.seaNightOverView = seaNightOverView;
        this.seaDayOverView = seaDayOverView;
        this.peipsiNight = peipsiNight;
        this.peipsiDay = peipsiDay;
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

    @Column(name = "night_text", length = 1000)
    private String nightText;

    @Column(name = "day_phenomenon")
    private String dayPhenomenon;

    @Column(name = "day_temp_min")
    private double dayTempMin;

    @Column(name = "day_temp_max")
    private double dayTempMax;

    @Column(name = "day_text", length = 1000)
    private String dayText;

    @Column(name = "sea_night_overview", length = 10000)
    private String seaNightOverView;

    @Column(name = "sea_day_overview", length = 10000)
    private String seaDayOverView;

    @Column(name = "peipsiNigth", length = 10000)
    private String peipsiNight;

    @Column(name = "peipsiDay", length = 10000)
    private String peipsiDay;

    @JsonManagedReference
    @OneToMany(mappedBy = "generalForecast")
    private List<Place> places;

    @JsonManagedReference
    @OneToMany(mappedBy = "generalForecast")
    private List<Wind> wind;

    public void setPhenomenon(String timePeriod, String phenomenon) {
        if (timePeriod.equals("night")) {
            this.nightPhenomenon = phenomenon;
        } else if (timePeriod.equals("day")) {
            this.dayPhenomenon = phenomenon;
        }
    }

    public void setText(String timePeriod, String text) {
        if (timePeriod.equals("night")) {
            this.nightText = text;
        } else if (timePeriod.equals("day")) {
            this.dayText = text;
        }
    }

    public void setTempMin(String timePeriod, int tempmin) {
        if (timePeriod.equals("night")) {
            this.nightTempMin = tempmin;
        } else if (timePeriod.equals("day")) {
            this.dayTempMin = tempmin;
        }
    }

    public void setTempMax(String timePeriod, int tempmax) {
        if (timePeriod.equals("night")) {
            this.nightTempMax = tempmax;
        } else if (timePeriod.equals("day")) {
            this.dayTempMax = tempmax;
        }
    }
}
