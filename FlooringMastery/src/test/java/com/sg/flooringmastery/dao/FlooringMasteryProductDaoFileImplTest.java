/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Product;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author The Code Warriors
 */
public class FlooringMasteryProductDaoFileImplTest {
    
    FlooringMasteryProductDao testProductDao = 
            new FlooringMasteryProductDaoFileImpl();
    
    HashMap<String, Product> allProducts;
    
    public FlooringMasteryProductDaoFileImplTest() throws PersistenceException {
    }
    
    @BeforeAll
    public static void setUpClass() {
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
    public void testGetAllProducts() {
        //Arrange
        allProducts = testProductDao.getAllProducts();
        
        // ACT & ASSERT
        // allProducts can't be none
        assertNotNull(allProducts);
        
        // size should be 4, since we only have 4 products
        assertEquals(allProducts.size(),4);
        
    }
    
    @Test
    public void testGetProductByProductType() {
        //Arrange
        String productType = "Tile";
        // ACT & ASSERT
        assertNotNull(testProductDao.getProductByProductType(productType));
        
        assertEquals(testProductDao.getProductByProductType(productType)
                .getProductType(),productType);
        
        //Arrange
        productType = "Carpet";
        // ACT & ASSERT
        assertNotNull(testProductDao.getProductByProductType(productType));
        assertEquals(testProductDao.getProductByProductType(productType)
                .getProductType(),productType);
        
        //Arrange
        productType = "Laminate";
        // ACT & ASSERT
        assertNotNull(testProductDao.getProductByProductType(productType));
        assertEquals(testProductDao.getProductByProductType(productType)
                .getProductType(),productType);
        
        //Arrange
        productType = "Wood";
        // ACT & ASSERT
        assertNotNull(testProductDao.getProductByProductType(productType));
        assertEquals(testProductDao.getProductByProductType(productType)
                .getProductType(),productType);
    }
}
