package com.araujo.notificationservice.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderModel implements Serializable {

    private BigDecimal value;
    private String productName;

}
