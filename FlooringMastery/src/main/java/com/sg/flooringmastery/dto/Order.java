package com.sg.flooringmastery.dto;

import static com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting.parseFileDate;
import static com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting.parseInputDate;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.sg.flooringmastery.dao.FlooringMasteryTaxDaoFileImpl.taxes;
import static com.sg.flooringmastery.dao.FlooringMasteryProductDaoFileImpl.products;
import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author The Code Warriors
 */
public final class Order extends Transaction {
    
    private String customerName;
    
    private Tax taxInfo;
    
    private Product productInfo;

    private BigDecimal area, materialCost, laborCost, taxCost, total;
    
    public Order(
        LocalDate orderDate,
        String customerName,
        String stateAbbr, 
        String productType,
        BigDecimal area
    ) throws DataValidationException {
        super(orderDate);
        
        this.customerName = customerName;
        this.taxInfo = getTaxInfo(stateAbbr);
        this.productInfo = getProductInfo(productType);
        this.area = area;
        this.calculateOrder();
    }
    
    public Order(
            int orderNumber,
            String orderDate,
            String customerName,
            String state_abbr,
            String tax_rate,
            String productType,
            String area,
            String costPerSquareFoot,
            String laborCostPerSquareFoot,
            String materialCost,
            String laborCost,
            String tax,
            String total) {
        
        super(orderNumber,parseFileDate(orderDate));
        
        this.customerName = customerName;
        
        this.taxInfo = new Tax(state_abbr, 
                new BigDecimal(tax_rate));
        
        this.productInfo = new Product(
                productType, 
                new BigDecimal(costPerSquareFoot), 
                new BigDecimal(laborCostPerSquareFoot)
        );
        
        this.area = new BigDecimal(area);
        
        this.materialCost = new BigDecimal(materialCost);
        this.laborCost = new BigDecimal(laborCost);
        this.taxCost = new BigDecimal(tax);
        this.total = new BigDecimal(total);
    }
    
    public Order(
        String orderDate,
        String customerName,
        String stateAbbr, 
        String productType,
        String area
    ) throws DataValidationException {
        super(parseInputDate(orderDate));
        
        this.customerName = customerName;
        this.taxInfo = getTaxInfo(stateAbbr);
        this.productInfo = getProductInfo(productType);
        this.area = new BigDecimal(area);
        this.calculateOrder();
    }
    
    public Order(
        int orderNumber,
        String orderDate,
        String customerName,
        String stateAbbr, 
        String productType,
        String area
    ) throws DataValidationException {
        super(orderNumber, parseInputDate(orderDate));
        
        this.customerName = customerName;
        this.taxInfo = getTaxInfo(stateAbbr);
        this.productInfo = getProductInfo(productType);
        this.area = new BigDecimal(area);
        this.calculateOrder();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        
        this.customerName = customerName.isEmpty()? 
                this.customerName : customerName;
    }
    
    public Tax getTaxInfo() {
        return taxInfo;
    }

    public Tax getTaxInfo(String taxAbbr) {
        return taxes.get(taxAbbr.toUpperCase());
    }

    public void setTaxInfo(String stateAbbr) {
        Tax info = taxes.get(stateAbbr.toUpperCase());
        this.taxInfo = info == null? this.taxInfo : info;
    }
    
    public Product getProductInfo() {
        return productInfo;
    }

    public Product getProductInfo(String productType) {
        return products.get(productType.toUpperCase());
    }

    public void setProductInfo(String productType) {
        Product info = products.get(productType.toUpperCase());
        this.productInfo = info == null? this.productInfo : info;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTaxCost() {
        return taxCost;
    }

    public void setTaxCost(BigDecimal taxCost) {
        this.taxCost = taxCost;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    private BigDecimal calculateMaterialCost(BigDecimal area,Product product) {
        return area.multiply(product.getCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateLaborCost(BigDecimal area, Product product) {
        return area.multiply(product.getLabourCostPerSquareFoot())
                .setScale(2,RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateTaxCost(Tax tax) {
        BigDecimal subTotal = this.materialCost.add(this.laborCost);
        
        return subTotal.multiply(tax.getTaxRate()
                .divide(new BigDecimal("100")))
                .setScale(2,RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateTotalCost() {
        return this.materialCost.add(this.laborCost)
                .add(this.taxCost);
    }
    
    public void calculateOrder() throws DataValidationException {
        if (this.taxInfo == null)
            throw new DataValidationException("We cannot sell in this "
                    + "state.");
        
        if (this.productInfo == null)
            throw new DataValidationException("We do not have this product "
                    + "in our inventory.");
            
        setMaterialCost(
                calculateMaterialCost(
                this.area, this.productInfo));
        setLaborCost(calculateLaborCost(
                this.area, this.productInfo));
        setTaxCost(calculateTaxCost(
                this.taxInfo));
        setTotal(calculateTotalCost());
    }
    
    @Override
    public String toString() {
        return "Date: " + this.date + "\nNumber: " + this.number +
                "\nCustomer Name: " + this.customerName + "\nTax Info: " +
                this.taxInfo + "\nProduct Info: " + this.productInfo + 
                "\nArea: " + this.area + "\nMaterial Cost: " + 
                this.materialCost + "\nLabor Cost: " + this.laborCost + 
                "Tax Cost: " + this.taxCost + "\nTotal: " + this.total;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        
        Order order = (Order) o;
        
        return 
                this.date.equals(order.date) && 
                this.number == order.number &&
                this.customerName.equals(order.customerName) &&
                this.taxInfo.equals(order.taxInfo) &&
                this.productInfo.equals(order.productInfo) &&
                this.area.equals(order.area);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        
        hash = 19 * hash + Objects.hashCode(this.customerName);
        hash = 19 * hash + Objects.hashCode(this.taxInfo);
        hash = 19 * hash + Objects.hashCode(this.productInfo);
        hash = 19 * hash + Objects.hashCode(this.area);
        
        return hash;
    }
}

