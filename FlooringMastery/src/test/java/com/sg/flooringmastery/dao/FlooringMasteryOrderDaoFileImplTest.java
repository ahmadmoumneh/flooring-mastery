/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryOrderDaoStubImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author The Code Warriors
 */
public class FlooringMasteryOrderDaoFileImplTest {
    
    static FlooringMasteryOrderDao testDao;
    static Order order1, order2, order3;
    static LocalDate orderDate;
    String date;
    
    static FlooringMasteryProductDao testProductDao;
    static FlooringMasteryTaxDao testTaxDao;
    
    public FlooringMasteryOrderDaoFileImplTest() throws 
            DataValidationException,
            PersistenceException {
        
        testProductDao = 
                new FlooringMasteryProductDaoFileImpl();
        testTaxDao = 
                new FlooringMasteryTaxDaoFileImpl();
        
        testProductDao.getAllProducts();
        testTaxDao.getAllTaxes();
        
        testDao = new FlooringMasteryOrderDaoStubImpl(); 
        //adding order info:        
        date= "08/25/2025";
        String stateAbbr = "TX";
        //set cusomer name
        String customerName = "Ada";
       //set product
        String productType = "wood";
        //set area
        String area= "1000";
        //create order
        order1 = new Order(date,customerName, stateAbbr,
                productType, area);
        //calculate related costs
        order1.calculateOrder();
        
        order2 = new Order("03/04/2023","Sara", 
                "CA","carpet", "2000");
        order2.calculateOrder();
        testDao.addOrder(order2);
        
        order3 = new Order("04/05/2024","Sara", 
                "CA","carpet", "2000");
        order3.calculateOrder();
        testDao.addOrder(order3);

    }
    
    @Test   
    public void testAddorder() throws Exception {
        try{                
            File file = new File("src/main/resources/TestOrders/TestOrders_" +
                    date.replace("/", "") + ".txt");
            try (PrintWriter out = new PrintWriter(new FileWriter(file, 
                    false))) {
                if (file.length() == 0)
                    out.println("OrderNumber,CustomerName,State,TaxRate,"
                            + "ProductType,Area,CostPerSquareFoot,"
                            + "LaborCostPerSquareFoot,MaterialCost,LaborCost,"
                            + "Tax,Total");

                out.flush();
                List<Order> allOrdersBefore = testDao.getOrdersByDate(
                        order1.getDate());
                testDao.addOrder(order1);
                List<Order> allOrdersAfter = testDao.getOrdersByDate(
                        order1.getDate());
                Order retrievedOrder = testDao.getOrder(orderDate, 
                        order1.getNumber());
                assertEquals(order1, retrievedOrder);
                //check size after adding an order
                assertEquals(allOrdersBefore.size() + 1, allOrdersAfter.size());
                }
        } catch (IOException e) {
            throw new PersistenceException("Writing order failed.");
        }        
    }
    
    @Test
    public void testEditOrder() throws OrderNotFoundException, 
            PersistenceException{       
        //change customer name
        order2.setCustomerName("John");
        testDao.editOrder(order2);
        Order orderModified = testDao.getOrder(order2.getDate(),
                order2.getNumber());
        String newCustomerName = orderModified.getCustomerName();
        assertEquals("John", newCustomerName);
    }
    
    @Test
    public void testRemoveOrder() throws OrderNotFoundException, PersistenceException{
        List<Order> allOrdersBefore = testDao.getOrdersByDate(order3.getDate());
        System.out.println("orders before " + allOrdersBefore.size());
        testDao.removeOrder(order3);
        List<Order> allOrdersAfter = testDao.getOrdersByDate(order3.getDate());
        System.out.println("orders after " + allOrdersAfter.size());
        //check size after removing an order
        assertEquals(allOrdersBefore.size() - 1, allOrdersAfter.size());
    }
    
    @Test
    public void testWriteAllOrders() throws OrderNotFoundException, 
            PersistenceException, IOException{
        //load all orders to check number of orders exist:
        List<Order> allOrdersBefore = testDao.writeAllOrders();
        try {          
            int lines;
            try (BufferedReader reader = 
                new BufferedReader(
                new FileReader(
              "src/main/resources/Backup/TestDataExport.txt"))) {
                lines = 0;
                while (reader.readLine() != null) lines++;
            }
            assertEquals(allOrdersBefore.size(), lines - 1);
        } catch (FileNotFoundException e) {
                throw new OrderNotFoundException("export file not found!");
        }
    }
}
