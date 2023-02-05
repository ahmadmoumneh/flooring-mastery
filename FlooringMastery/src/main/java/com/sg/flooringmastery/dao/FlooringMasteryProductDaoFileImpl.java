/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.common.values.FlooringMasteryCommonDaoValues;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryProductDaoFileImpl
        implements FlooringMasteryProductDao, FlooringMasteryCommonDaoValues {
    
    public static final HashMap<String, Product> products = new HashMap<>();
    
    private static final String PRODUCT_FILE = 
            "src/main/resources/Data/Products.txt";
    
    public FlooringMasteryProductDaoFileImpl() throws PersistenceException {
        loadAllProducts();
    }
    
    private static Product unmarshallProduct(String productAsText){
        String[] taxTokens = productAsText.split(",");
        String type = taxTokens[0];
        BigDecimal cost = new BigDecimal(taxTokens[1]);
        BigDecimal laborCost = new BigDecimal(taxTokens[2]);
        
        return new Product(
                type, 
                cost, 
                laborCost);
    }
    
    private void loadAllProducts() throws PersistenceException {
        Scanner scanner;
        
        try {
            scanner = new Scanner(
                    new BufferedReader(   
                            new FileReader(new File(PRODUCT_FILE))));
            
            scanner.nextLine();
            
            String currentLine;
            
            Product currentProduct;
            
            while (scanner.hasNextLine()){
                currentLine = scanner.nextLine();
                currentProduct = unmarshallProduct(currentLine);
                
                products.put(currentProduct.getProductType().toUpperCase(), 
                        new Product(
                        currentProduct
                                .getProductType(), 
                        currentProduct
                                .getCostPerSquareFoot(),
                        currentProduct
                                .getLabourCostPerSquareFoot()));
            }
            
            scanner.close();
                 
        } catch (FileNotFoundException e) {
            throw new PersistenceException(e.getMessage());
        }
        
    }

    @Override
    //Since we save product key as upper case , I added to upper case here.
    public Product getProductByProductType(String productType) {
        return products.get(productType.toUpperCase());
    }

    @Override
    public HashMap<String, Product> getAllProducts() {
        return products;
    }
    
    // for junit test
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.products);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlooringMasteryProductDaoFileImpl other = (FlooringMasteryProductDaoFileImpl) obj;
        return Objects.equals(this.products, other.products);
    }

    @Override
    public String toString() {
        return "FlooringMasteryProductDaoFileImpl{" + "products=" + products + '}';
    }
}
