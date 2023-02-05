/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.dto;

import java.time.LocalDate;
import com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting;

/**
 *
 * @author The Code Warriors
 */
public abstract class Transaction implements FlooringMasteryDateFormatting {
    protected int number;
    
    protected final LocalDate date;
    
    public static int counter = 1;
    
    public Transaction(LocalDate date) {
        this.number = counter++;
        this.date = date;
    }
    
    public Transaction(int number, LocalDate date) {
        this.number = number;
        this.date = date;
    }
    
    public int getNumber() {
        return number;
    }
    
    public LocalDate getDate() {
        return date;
    }
}
