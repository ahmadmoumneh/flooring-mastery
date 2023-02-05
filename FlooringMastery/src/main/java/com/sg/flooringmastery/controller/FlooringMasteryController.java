package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.exceptions.DataValidationException;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import static com.sg.flooringmastery.dto.Transaction.counter;
import com.sg.flooringmastery.service.FlooringMasteryService;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryController {
    
    private final FlooringMasteryView view;
    private final FlooringMasteryService service;
    
    @Autowired
    public FlooringMasteryController (
            FlooringMasteryView view, FlooringMasteryService service) {
        
       this.view = view;
       this.service = service;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection;
        
        while (keepGoing) {
            menuSelection = getMenuSelection();
            
            try {
                switch (menuSelection) {
                    case 1 -> {
                        view.displayFetchOrdersBanner();

                        LocalDate date = view.getOrderDate();

                        List<Order> orders = service.getOrdersByDate(date);
                        orders.forEach(order -> 
                                view.displayOrderDetails(order));
                    }
                    
                    case 2 -> {
                        view.displayAllTaxInfo();
                        view.displayAllProductsInfo();

                        view.displayAddOrderBanner();

                        Order newOrder = view.getNewOrder();
                        
                        this.service.validateOrderData(newOrder);
                        
                        view.displayOrderDetails(newOrder);

                        boolean placeOrder = 
                                view.confirm("Are you sure you want "
                                        + "to place this order?");

                        if (placeOrder) {
                            service.addOrder(newOrder);
                            
                            view.displayAddOrderSuccessBanner(newOrder);
                        } else {
                            view.displayError("Order not placed.");
                            counter--;
                        }
                    }
                    
                    case 3 -> {
                        view.displayEditOrderBannerAndNotice();

                        Order order = getOrderFromDateAndOrderNumber();

                        String customerName = 
                                view.getNewCustomerName(order);

                        String state = view.getNewState(order);

                        String productType = 
                                view.getNewProductType(order);

                        BigDecimal area = view.getNewArea(order);

                        order.setCustomerName(customerName);
                        order.setTaxInfo(state);
                        order.setProductInfo(productType);
                        order.setArea(area);
                        
                        this.service.validateOrderData(order);

                        view.displayEditOrderSummary(order);

                        boolean save = 
                                view.confirm("Are you sure you want "
                                        + "to edit order?");

                        if (save) {
                            service.editOrder(order);
                            view.displayEditSuccess();
                        }

                        else {
                            view.displayError("Order not edited.");
                        }
                    }

                    case 4 -> {
                        view.displayRemoveOrderBannerAndNotice();

                        Order order = getOrderFromDateAndOrderNumber();
                        
                        this.service.validateOrderData(order);

                        view.displayOrderDetails(order);

                        boolean remove = view.confirm("Do you want "
                                + "to remove order?");

                        if (remove) {
                            service.removeOrder(order);
                            view.displayRemoveSuccess();
                        }

                        else {
                            view.displayError("Order not removed.");
                        }
                    }
                    
                    case 5 -> {
                        service.exportAllOrders();
                        view.displayExportSuccess();
                    }
                    
                    case 6 -> {
                        view.displayExitMessage();
                        keepGoing = false;
                    }

                    default-> view.displayError("Invalid command.");
                }
                
            } catch (DataValidationException |
                    OrderNotFoundException |
                    PersistenceException e) {

                view.displayError(e.getMessage());
            }
        }
    }
    
    private Order getOrderFromDateAndOrderNumber() throws
            DataValidationException, 
            OrderNotFoundException {
        
        LocalDate date = view.getOrderDate();
        int orderNumber = view.getOrderNumber();

        return service.getOrder(date, orderNumber);
    }
  
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
}
