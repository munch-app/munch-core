package com.munch.core.struct.rdbms.place;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 5:26 PM
 * Project: struct
 */
@Entity
public class ReviewSummary {

    private String id;

    // Ratings
    // From Null and (0.0 to 5.0)
    private Double overallExperience;
    private Double qualityOfFood;
    private Double qualityOfService;
    private Double atmosphere;
    private Double price;

    // Count
    private Integer reviewCount;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false)
    public Double getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(Double overallExperience) {
        this.overallExperience = overallExperience;
    }

    @Column(nullable = true)
    public Double getQualityOfFood() {
        return qualityOfFood;
    }

    public void setQualityOfFood(Double qualityOfFood) {
        this.qualityOfFood = qualityOfFood;
    }

    @Column(nullable = true)
    public Double getQualityOfService() {
        return qualityOfService;
    }

    public void setQualityOfService(Double qualityOfService) {
        this.qualityOfService = qualityOfService;
    }

    @Column(nullable = true)
    public Double getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Double atmosphere) {
        this.atmosphere = atmosphere;
    }

    @Column(nullable = true)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(nullable = true)
    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
}
