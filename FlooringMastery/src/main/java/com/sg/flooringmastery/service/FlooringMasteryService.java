/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryService {
    
    List<Order> getOrdersByDate(LocalDate date) throws
            OrderNotFoundException;
    
    List<Order> getOrdersByDate(String date) throws
            OrderNotFoundException;
    
    Order addOrder(Order order) throws 
            DataValidationException,
            PersistenceException;
    
    Order editOrder(Order order) throws
            DataValidationException,
            OrderNotFoundException,
            PersistenceException;
    
    Order removeOrder(Order order) throws 
            OrderNotFoundException,
            PersistenceException;
    
    List<Order> exportAllOrders() throws 
            OrderNotFoundException,
            PersistenceException;
    
    Order getOrder(LocalDate date, int orderNumber) throws 
            OrderNotFoundException;
    
    Order getOrder(String orderDate, int orderNumber) throws
            OrderNotFoundException;
    
    List<Order> getAllOrders() throws OrderNotFoundException;
    
    Tax getTaxInfo(String state);
    
    Product getProductInfo(String productType);
    
    HashMap<String, Tax> getAllTaxes();
    
    HashMap<String, Product> getAllProducts();
    
    void validateOrderData(Order order) throws
            DataValidationException;
}
