package com.jamie.metro.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

    private int transId;
    private int userId;
    private int startStation;
    private int destStation;
    private java.sql.Timestamp swipeInTime;
    private java.sql.Timestamp swipeOutTime;
    private double fee;
    private double balanceBefore;
    private double balanceAfter;
}