/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import java.util.HashMap;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryTaxDao {
    Tax getTaxInfoByState(String state);
    HashMap<String, Tax> getAllTaxes();
}
