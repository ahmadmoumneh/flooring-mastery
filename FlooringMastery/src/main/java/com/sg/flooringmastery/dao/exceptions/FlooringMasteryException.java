/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.dao.exceptions;

/**
 *
 * @author The Code Warriors
 */
public class FlooringMasteryException extends Exception {
    public FlooringMasteryException(String error) {
        super(error);
    }
    
    public FlooringMasteryException(String error, Throwable cause) {
        super(error);
    }
}
