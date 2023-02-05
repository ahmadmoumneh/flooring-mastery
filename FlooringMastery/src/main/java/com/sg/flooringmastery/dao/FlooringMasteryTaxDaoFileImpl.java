/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.common.values.FlooringMasteryCommonDaoValues;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryTaxDaoFileImpl
        implements FlooringMasteryTaxDao, FlooringMasteryCommonDaoValues {
    
    public static final HashMap<String, Tax> taxes = new HashMap<>();;
    public static final HashMap<String, String> states = new HashMap<>();;
    private static final String TAX_FILE = "src/main/resources/Data/Taxes.txt";
           
    public FlooringMasteryTaxDaoFileImpl() throws PersistenceException {
        loadTaxesAndStates();
    }
    
    private static Tax unmarshallTaxInfo(String taxInfoAsText){
        String[] taxTokens = taxInfoAsText.split(",");
        String abbr = taxTokens[0];
        String state = taxTokens[1];
        BigDecimal rate = new BigDecimal(taxTokens[2]);
        
        return new Tax(abbr, state, rate);
    }
    
    private void loadTaxesAndStates() throws PersistenceException {
        
        Scanner scanner;
        
        try {
            scanner = new Scanner(
                    new BufferedReader(   
                            new FileReader(new File(TAX_FILE))));
            
            scanner.nextLine();
            
            String currentLine;
            
            Tax currentTax;
            
            while (scanner.hasNextLine()){
                currentLine = scanner.nextLine();
                currentTax = unmarshallTaxInfo(currentLine);
                
                taxes.put(currentTax.getStateAbbr(), new Tax(
                        currentTax.getStateAbbr(), 
                        currentTax.getStateName(),
                        currentTax.getTaxRate()));
                
                states.put(currentTax.getStateAbbr(), currentTax.getStateName());
            }
            
            scanner.close();
                 
        } catch (FileNotFoundException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Tax getTaxInfoByState(String state) {
        return taxes.get(state);
    }

    @Override
    public HashMap<String, Tax> getAllTaxes() {
        return taxes;
    }
}
