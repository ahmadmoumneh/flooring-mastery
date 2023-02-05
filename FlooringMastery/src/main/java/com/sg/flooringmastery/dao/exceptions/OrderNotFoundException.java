/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.dao.exceptions;

/**
 *
 * @author The Code Warriors
 */
public class OrderNotFoundException extends FlooringMasteryException {

    public OrderNotFoundException(String error) {
        super(error);
    }

    public OrderNotFoundException(String error, Throwable cause) {
        super(error, cause);

    }
    
}
