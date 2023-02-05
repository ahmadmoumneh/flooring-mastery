/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Tax;
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
public class FlooringMasteryTaxDaoFileImplTest  {
    
    FlooringMasteryTaxDao testTaxDao = new FlooringMasteryTaxDaoFileImpl();
    HashMap<String, Tax> taxes;
    
    public FlooringMasteryTaxDaoFileImplTest() throws PersistenceException {
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
    public void testGetAllTaxes() {
        //Arrange
        taxes = testTaxDao.getAllTaxes();
        
        // ACT & ASSERT
        //should not be null
        assertNotNull(taxes);
        //size should be 4 since we only have 4 taxes in file
        assertEquals(taxes.size(),4);
    }
    
    @Test
    public void TestGetTaxInfoByState() {
        //Arrange
        String stateAbbrev = "TX";
        // ACT & ASSERT
        assertEquals(taxes.get(stateAbbrev),stateAbbrev);
        
        //Arrange
        stateAbbrev = "WA";
        // ACT & ASSERT
        assertEquals(taxes.get(stateAbbrev),stateAbbrev);
        
        //Arrange
        stateAbbrev = "KY";
        // ACT & ASSERT
        assertEquals(taxes.get(stateAbbrev),stateAbbrev);
        
        //Arrange
        stateAbbrev = "CA";
        // ACT & ASSERT
        assertEquals(taxes.get(stateAbbrev),stateAbbrev);
 
    }
    
}
