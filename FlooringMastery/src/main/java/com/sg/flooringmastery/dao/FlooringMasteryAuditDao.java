/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.exceptions.PersistenceException;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryAuditDao {
    void writeAuditEntry(String entry) throws PersistenceException;
}
