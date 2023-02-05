package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author The Code Warriors
 */
public final class Product {
    private final String productType;
    private final BigDecimal costPerSquareFoot, labourCostPerSquareFoot;

    public Product(
        String productType, 
        BigDecimal costPerSquareFoot, 
        BigDecimal labourCostPerSquareFoot
    ) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }
    
    @Override
    public String toString() {
        return "Product Type: " + this.productType + "\nCost Per Square Foot: " + 
                this.costPerSquareFoot + "\nLabour Cost Per Square Foot: " + 
                this.labourCostPerSquareFoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
        
        Product product = (Product) o;
        
        return
            this.productType.equals(product.productType) &&
            this.costPerSquareFoot.equals(product.costPerSquareFoot) &&
            this.labourCostPerSquareFoot.equals(product.labourCostPerSquareFoot);
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.productType);
        hash = 29 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 29 * hash + Objects.hashCode(this.labourCostPerSquareFoot);
        return hash;
    }
}