/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryOrderDaoFileImpl;

/**
 *
 * @author The Code Warriors
 */
public class FlooringMasteryOrderDaoStubImpl extends 
    FlooringMasteryOrderDaoFileImpl {
    
    public FlooringMasteryOrderDaoStubImpl() {
        this.ORDERS_FOLDER = "src/main/resources/TestOrders";
        this.ORDERS_FILE_PREFIX = "src/main/resources/TestOrders/TestOrders_";
        this.EXPORT_FILE = "src/main/resources/Backup/TestDataExport.txt";
    }
}
