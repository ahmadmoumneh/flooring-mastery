/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting;
import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDaoFileImpl;
import com.sg.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sg.flooringmastery.dao.FlooringMasteryTaxDaoFileImpl;
import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author The Code Warriors
 */
public class FlooringMasteryServiceImplTest implements
        FlooringMasteryDateFormatting {
    
    FlooringMasteryProductDao productDao;
    FlooringMasteryOrderDaoStubImpl orderDao;
    FlooringMasteryTaxDao taxDao;
    FlooringMasteryAuditDao auditDao;
    
    FlooringMasteryService service;
    
    public FlooringMasteryServiceImplTest() throws 
            PersistenceException,
            OrderNotFoundException {
        
        taxDao = new FlooringMasteryTaxDaoFileImpl();
        productDao = new FlooringMasteryProductDaoFileImpl();
        
        orderDao = new FlooringMasteryOrderDaoStubImpl();
        auditDao = new FlooringMasteryAuditDaoStubImpl();

        service = new FlooringMasteryServiceImpl(
                orderDao, 
                taxDao, 
                productDao, 
                auditDao
        );
    }
    
    @BeforeAll
    public void setUpClass() throws PersistenceException {
    }
    
    @AfterAll
    public static void tearDownClass() {
        
    }
    
    @BeforeEach
    public void setUp() {
       
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAddValidOrder() throws Exception {
        // ARRANGE
        Order order = new Order(
            "12/20/2024", 
            "Steve", 
            "TX", 
            "Carpet", 
            "900");
        
        try {
            // ACT
            service.addOrder(order);
        } catch (DataValidationException | PersistenceException ex) {
            fail("Order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testGetOrdersByDate() throws Exception {
        
        // ARRANGE
        Order testClone = new Order(
            3,
            "06/02/2013",
            "Albert Einstein",
            "KY",
            "Carpet",
            "217.00"
        );
                
        Order shoudldBeAlbert = service.getOrder("06/02/2013", 3);
        
        List<Order> orders = service.getOrdersByDate("06/02/2013");
        
        // ACT & ASSERT
        assertEquals( 2, orders.size(), 
            "Should be two orders.");
        
        assertTrue( shoudldBeAlbert.equals(testClone),
            "The one order customer should be Albert.");
    }
    
    @Test
    public void testAddOrderInvalidData() throws Exception {
        try {
            
            // ARRANGE 
            //INVALID DATE
            Order[] orders = {
            new Order(
            "12/20/2020", 
            "Steve", 
            "TX", 
            "Carpet", 
            "900"),

            //INVALID CUSTOMER NAME
            new Order(
                "12/20/2023", 
                "@Steve", 
                "TX", 
                "Carpet", 
                "900"),

            //INVALID STATE
            new Order(
                "12/20/2023", 
                "Steve", 
                "QC", 
                "Carpet", 
                "900"),

            //INVALID PRODUCT TYPE
            new Order(
                "12/20/2023", 
                "Steve", 
                "TX", 
                "Hardwood", 
                "900"),

            //INVALID AREA SIZE
            new Order(
                "12/20/2023", 
                "Steve", 
                "TX", 
                "Carpet", 
                "90")
            };

            for (Order order : orders) {
                    //ACT
                    service.validateOrderData(order);
                    service.addOrder(order);

                    fail("Expected DataValidationException was not thrown.");
                
            }
            
        } catch (PersistenceException ex) {
            fail("Incorrect exception was thrown.");
        } catch (DataValidationException e) {

        }
    }
    
    @Test
    public void testEditOrder() throws Exception {
        
        // ARRANGE
        // Order to be edited
        Order shouldBeBrian = service.getOrder("12/20/2023", 7);
        
        Order willBeMatt = new Order(
            7,
            "12/20/2023",
            "Brian",
            "TX",
            "Wood",
            "780"
        );
        // ACT & ASSERT
        willBeMatt.setCustomerName("Matt");
        
        Order shouldBeMatt = service.editOrder(willBeMatt);
        
        assertNotNull( shouldBeMatt, "Editing Order " + shouldBeMatt.getNumber() + " should be not null.");
        assertNotEquals(shouldBeMatt, shouldBeBrian, "Order " + shouldBeMatt.getNumber() + " should be different.");

        Order willBeBrian = new Order(
            7,
            "12/20/2023",
            "Matt",
            "TX",
            "Wood",
            "780"
        );
        // ACT & ASSERT
        willBeBrian.setCustomerName("Brian");
        
        shouldBeBrian = service.editOrder(willBeBrian);
        
        assertNotNull( shouldBeBrian, "Editing Order " + shouldBeBrian.getNumber() + " should be not null.");
        assertNotEquals(shouldBeBrian, shouldBeMatt, "Order " + shouldBeBrian.getNumber() + " should be different.");
    }
    
    @Test
    public void testRemoveOrder() throws Exception {
        // ARRANGE
        Order rachel = new Order(
                "12/21/2023",
                "Rachel",
                "KY",
                "Wood",
                "889"
        );
        
        service.addOrder(rachel);
        
        // ACT & ASSERT
        Order shouldBeRachel = service.removeOrder(rachel);
        assertNotNull( shouldBeRachel, "Removing order with customer name " 
                + shouldBeRachel.getCustomerName() + " should be not null.");
        
        
        try {
            Order order = new Order(
                "06/01/2024",
                "Matt",
                "WA",
                "Wood",
                "530.23"
            );
        
            service.removeOrder(order); 
            fail("Order Not Found Exception not thrown.");
        
        } catch (OrderNotFoundException e) {
           
        }
    }
    
    @Test
    public void testExportAllOrders() throws Exception {
        
        // ARRANGE
        List<Order> allOrders = service.exportAllOrders();
        List<Order> currentOrders = service.getAllOrders();
        
        // ACT & ASSERT
        assertEquals(currentOrders.size(), allOrders.size(), "Exported orders size should be equal to current orders size.");

    }
}
