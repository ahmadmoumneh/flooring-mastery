/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.flooringmastery.common.values;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryCommonDaoValues {
    String DELIMITER = ",";
    
    String ORDER_FILE_HEADER = 
            "OrderNumber,"
            + "CustomerName,"
            + "State,"
            + "TaxRate,"
            + "ProductType,"
            + "Area,"
            + "CostPerSquareFoot,"
            + "LaborCostPerSquareFoot,"
            + "MaterialCost,"
            + "LaborCost,"
            + "Tax,"
            + "Total";
    
    String EXPORT_FILE_HEADER = ORDER_FILE_HEADER + ",OrderDate";
}
