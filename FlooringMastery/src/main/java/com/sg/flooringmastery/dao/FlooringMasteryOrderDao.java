package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryOrderDao {
    
    Order addOrder(Order order) throws 
            PersistenceException;
    
    Order removeOrder(Order order) throws 
            OrderNotFoundException,
            PersistenceException;
    
    Order editOrder(Order order) throws 
            OrderNotFoundException, 
            PersistenceException;
    
    Order getOrder(LocalDate orderDate, int orderNumber) throws
            OrderNotFoundException;
    
    List<Order> getOrdersByDate(LocalDate date) throws
            OrderNotFoundException;
    
    List<Order> getAllOrders() throws 
            OrderNotFoundException;
    
    List<Order> writeAllOrders() throws 
            OrderNotFoundException,
            PersistenceException;
}
