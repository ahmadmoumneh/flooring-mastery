/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.service;

import static com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting.parseInputDate;
import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import static com.sg.flooringmastery.dto.Transaction.counter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryServiceImpl implements FlooringMasteryService {
    private final FlooringMasteryOrderDao orderDao;
    private final FlooringMasteryTaxDao taxDao;
    private final FlooringMasteryProductDao productDao;
    
    private final FlooringMasteryAuditDao auditDao;
    
    @Autowired
    public FlooringMasteryServiceImpl(
            FlooringMasteryOrderDao orderDao, 
            FlooringMasteryTaxDao taxDao, 
            FlooringMasteryProductDao productDao,
            FlooringMasteryAuditDao auditDao) throws
            
            OrderNotFoundException {
        
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.auditDao = auditDao;
        
        counter = findMaxOrderNumber();
    }
    
    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderNotFoundException {
        return orderDao.getOrdersByDate(date);
    }
    
    @Override
    public List<Order> getOrdersByDate(String orderDate) throws OrderNotFoundException {
        LocalDate date = parseInputDate(orderDate);
        
        return orderDao.getOrdersByDate(date);
    }

    @Override
    public Order addOrder(Order order) throws 
            DataValidationException,
            PersistenceException {
        
        order.calculateOrder();
        
        orderDao.addOrder(order);
        
        auditDao.writeAuditEntry(
                "Order " + order.getNumber() + " CREATED.");
        
        return order;
    }
    
    private int findMaxOrderNumber() throws OrderNotFoundException {
        List<Order> orders = orderDao.getAllOrders();
        
        return orders.stream().mapToInt(order -> 
                order.getNumber()).max().getAsInt() + 1;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws
            OrderNotFoundException {
        
        return orderDao.getOrder(date, orderNumber);
    }
    
    @Override
    public Order getOrder(String orderDate, int orderNumber) throws
            OrderNotFoundException {
        
        LocalDate date = parseInputDate(orderDate);
        
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public Order removeOrder(Order order) throws 
            OrderNotFoundException,
            PersistenceException {
        
        Order removed = orderDao.removeOrder(order);

        if (removed != null) {
            auditDao.writeAuditEntry(
                "Order " + order.getNumber() + " REMOVED.");
        }
            
        return removed;
    }

    @Override
    public List<Order> exportAllOrders() throws 
            OrderNotFoundException,
            PersistenceException {
        
        List<Order> orders = orderDao.writeAllOrders();
        
        if (!orders.isEmpty()) {
            auditDao.writeAuditEntry(
                "All Orders EXPORTED.");
        }
        
        return orders;
    }
    
    @Override    
    public void validateOrderData(Order order) throws
            DataValidationException {
        
        if (order.getTaxInfo() == null) {
            
            throw new DataValidationException(
                    "Error: We cannot sell in this state.");
        }
           
        if (order.getProductInfo() == null) {
            
            throw new DataValidationException(
                    "Error: We don't have this product in our inventory.");
        }
        
        if (order.getCustomerName() == null
                || order.getCustomerName().trim().length() == 0
                || order.getTaxInfo().getStateName().trim().length() == 0
                || order.getProductInfo().getProductType() == null
                || order.getProductInfo().getProductType().trim().length() == 0
                || order.getArea() == null) {
            throw new DataValidationException(
                    "Error: All fields[Customer Name, State, Product Type, "
                            + "Area] are required");
        }
            
        if (!order.getDate().isAfter(LocalDate.now())) {
            
            throw new DataValidationException(
                    "Error: Date should be in the future");
        }
        
        if (!order.getCustomerName().matches("[a-zA-Z0-9,.\\s]++")) {
            throw new DataValidationException(
                    "Error: Customer name should be alphanumeric and may "
                            + "contain \",\" and \".\".");
        }
        
        if (order.getArea().compareTo(new BigDecimal(100)) == -1) {
            
            throw new DataValidationException(
                    "Area must be at least 100 sq ft.");
        }
    }
    
    @Override
    public HashMap<String, Tax> getAllTaxes() {
        return taxDao.getAllTaxes();
    }

    @Override
    public HashMap<String,Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public Tax getTaxInfo(String state) {
        return taxDao.getTaxInfoByState(state);
    }

    @Override
    public Product getProductInfo(String productType) {
        return productDao.getProductByProductType(productType);
    }

    @Override
    public Order editOrder(Order newOrder) throws 
            DataValidationException,
            OrderNotFoundException,
            PersistenceException {
        
        Order oldOrder = orderDao.getOrder(newOrder.getDate(), newOrder.getNumber());
        
        Order updatedOrder = updateCheck(oldOrder, newOrder);
        
        if (updatedOrder != null) {
            auditDao.writeAuditEntry(
                "Order " + updatedOrder.getNumber() + " UPDATED.");
        }
        
        return orderDao.editOrder(updatedOrder);
    }
    
    private Order updateCheck(Order oldOrder, Order newOrder) throws
            DataValidationException {
        
        String oldState = oldOrder.getTaxInfo().getStateAbbr();
        String newState = newOrder.getTaxInfo().getStateAbbr();
        
        String oldProductType = oldOrder.getProductInfo().getProductType();
        String newProductType = newOrder.getProductInfo().getProductType();
        
        BigDecimal oldArea = oldOrder.getArea();
        BigDecimal newArea = newOrder.getArea();
        
        if (!oldState.equals(newState) || 
               !oldProductType.equals(newProductType) ||
               oldArea.equals(newArea)) {
            
            newOrder.calculateOrder();
        }
        
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders() throws OrderNotFoundException {
        return orderDao.getAllOrders();
    }
}
