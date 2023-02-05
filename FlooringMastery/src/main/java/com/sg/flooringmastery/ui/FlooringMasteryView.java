package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import static com.sg.flooringmastery.common.values.
        FlooringMasteryDateFormatting.parseInputDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.sg.flooringmastery.dao.
        FlooringMasteryTaxDaoFileImpl.taxes;

import static com.sg.flooringmastery.dao.
        FlooringMasteryProductDaoFileImpl.products;

import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sg.flooringmastery.common.values.FlooringMasteryDateFormatting;

/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryView implements FlooringMasteryDateFormatting {
    
    UserIO io;
        
    @Autowired
    public FlooringMasteryView (UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() {
        
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.println("* << Flooring Mastery >>");
        io.println("* 1. Display Orders");
        io.println("* 2. Add an Order");
        io.println("* 3. Edit an Order");
        io.println("* 4. Remove an Order");
        io.println("* 5. Export All Data");
        io.println("* 6. Quit");
        io.println("*");
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
       
        return io.readInt(
                "Please select from the above choices:", 1, 6);
    }
    
    public void displayAddOrderBanner() {
        io.println("===  Add an Order  ===");
        io.println("Enter new Order details:");
    }
    
    public void displayAddOrderSuccessBanner(Order order) {
        io.println("Congradulation, order #"+ order.getNumber()+ 
                " has been placed successfully!");
    }
    
    public void displayAllTaxInfo() {
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        
        io.println("\nWe sell in these states :");
        
        String labelLine = String.format("%-30s%-30s%-30s",
                "State Abbreviation",
                "State name", 
                "Tax Rate");
        
        io.println(labelLine);
        
        for(Tax tax: taxes.values()) {
            String productInfo = String.format("%-30s%-30s%-30s",
                    tax.getStateAbbr(),
                    tax.getStateName(),
                    tax.getTaxRate());
            
            io.println(productInfo);
        }
        
        io.println("");
    }
    
    public void displayAllProductsInfo() {
        io.println("We offer the following products :");
        
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        
        String labelLine = String.format(
                "%-30s%-30s%-30s",
                "Product Type","Cost Per Square Foot", 
                "Labor Cost Per Square Foot");
        
        io.println(labelLine);
        
        for(Product product: products.values()) {
            String productInfo = String.format("%-30s%-30s%-30s",
                    product.getProductType(),
                    product.getCostPerSquareFoot(),
                    product.getLabourCostPerSquareFoot());
            io.println(productInfo);
        }
        
        io.println("");
    }
  
    
    public String getProductType() {
       return io.readString("Product Type:");

    }
    
    //display this user choose not submit the order
    public void displayOrderNotSubmitted() {
        io.println("Order has not been submitted.");
    }
    
    public void displayExitMessage() {
        io.println("Thank you!");
    }

    public void displayOrderDetails(Order order) {
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.println("=== Order Detail ===");
        io.print(order);
    }
    
    public List<String> getRemoveOrderInfo() {
        String orderDate = io.readString("Enter order date: ");
        String orderNumber = io.readString("Enter order number: ");
        List<String> response = new ArrayList<>();
        response.add(orderDate);
        response.add(orderNumber);
        return response;
    }
    
    public void displayOrderNotRemoved() {
        io.println("Order was not cancelled.");
    }
    
    public int getOrderNumber() throws NumberFormatException {
        return Integer.parseInt(
                io.readString("Please Enter order number:"));
    }
    
    public void displayRemoveOrderSuccess() {
        io.println("Order cancelled successfully.");
    }
    
    public void displayFetchOrdersBanner() {
        io.println("=== Fetch Orders ===");
    }
    
    public void displayOrderNotFound() {
        io.println("Sorry, order not found");
    }
    //for editing an order
    //pass the order that user want to edit as argument
    public void displayEditOrderBannerAndNotice() {
        io.println("=== Edit Order ===");
    }
    
    public void displayRemoveOrderBannerAndNotice() {
        io.println("=== Remove Order ===");
    }
    
    public void displayEditOrderSummary(Order order) {
        String orderDetailString = String.format("""
            Order Number: %s
            Order Date: %s
            Customer Name: %s
            Product Type: %s
            State: %s
            Area(sqft): %s""",
            order.getNumber(),
            order.getDate(),
            order.getCustomerName(),
            order.getProductInfo().getProductType(),
            order.getTaxInfo().getStateName(),
            order.getArea()
        );
        
        io.println("\n* * * * * * * * * * * * * * * * * * * * * * * * *");
        io.println("=== Order Detail ===");
        io.println(orderDetailString);
        
    }
    
    public void displayEditSuccess() {
        io.println("Edit Order Success!");
    }
    
    public String getNewCustomerName(Order orderToEdit) {
        return io.readString("Enter customer name (current customer name:  " 
                + orderToEdit.getCustomerName() + ")");
    }
    
    public String getNewState(Order orderToEdit){
        return io.readString("Enter state (current state: " 
                + orderToEdit.getTaxInfo().getStateAbbr() + ")");
    }
    
    public String getNewProductType(Order orderToEdit) {
        return io.readString("Enter pruduct type (current product type: " 
                + orderToEdit.getProductInfo().getProductType() + ")");
    }
    
    public BigDecimal getNewArea(Order orderToEdit) throws 
            DataValidationException {
        
        BigDecimal area;
        
        String value = io.readString("Enter area (current area: " + 
                orderToEdit.getArea() + ")");
        
        try {
            area = new BigDecimal(value);
        } catch (NumberFormatException e) {
            if (value.isEmpty())
                return orderToEdit.getArea();
            
            else throw new DataValidationException(
                    "Area should be a decimal number.");
        }
        
        return area;
    }
    
    private String getCustomerName() {
        return io.readString("Customer Name:");
    }
    
    private String getState() {
        return io.readString("State:");
    }
    
    private BigDecimal getArea() throws DataValidationException {
        BigDecimal area;
        
        try {
            area = new BigDecimal(io.readString("Area:"));
        } catch (NumberFormatException e) {
            throw new DataValidationException(e.getMessage());
        }
        
        return area;
    }
    
    private void displayDateConstraint() {
        io.println(" *** Date must be in the future and of MM/DD/YYYY "
                + "format. ***");
                
    }
    
    private void displayCustomerNameConstraint() {
        io.println(" *** Customer Name must be alphanumeric and may "
                + "contain ',' and '.' *** ");
    }
    
    private void displayStateConstraint() {
        io.println(" *** Must enter one of the above state "
                + "abbreviations. *** ");
    }
    
    private void displayProductTypeConstraint() {
        io.println(" *** Must enter one of the above product types. "
                + "*** ");
    }
    
    private void displayAreaConstraint() {
        io.println(" *** Must enter an area with a minimum of 100 sq ft."
                + " *** ");
    }
    
    public Order getNewOrder() throws DataValidationException {
        
        displayDateConstraint();
        LocalDate date = getOrderDate();

        displayCustomerNameConstraint();
        String customerName = getCustomerName();

        displayStateConstraint();
        String state = getState();

        displayProductTypeConstraint();
        String productType = getProductType();

        displayAreaConstraint();
        
        BigDecimal area= getArea();
        
        return new Order(date, customerName, state,
                productType, area);
    }
    
    public LocalDate getOrderDate() throws DataValidationException {
        LocalDate date;
        
        try {
            date = parseInputDate(io.readString("Order Date:"));
        } catch(DateTimeParseException e) {
            throw new DataValidationException("Wrong Input Date Format.\n");
        }
            
        return date;
    }
    
    public void displayExportSuccess() {
        io.println("Export Data Success!");
    }
    
    public void displayRemoveSuccess() {
        io.println("Remove Order Success!");
    }
    
    public void displayError(String error) {
        io.println(error);
    }
    
    public boolean confirm(String confirmation) {
        String answer = io.readString(confirmation + " y or n:");
        
        if (answer.equals("y"))
            return true;
        
        if (answer.equals("n"))
            return false;
        
        return false;
    }
}
