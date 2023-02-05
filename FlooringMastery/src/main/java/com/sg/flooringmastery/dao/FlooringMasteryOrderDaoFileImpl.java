package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.common.values.FlooringMasteryCommonDaoValues;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dao.exceptions.OrderNotFoundException;
import com.sg.flooringmastery.dao.exceptions.PersistenceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;


import static com.sg.flooringmastery.common.values.
        FlooringMasteryDateFormatting.FILE_DATE_FORMATTER;

import static com.sg.flooringmastery.common.values.
        FlooringMasteryDateFormatting.EXPORT_DATE_FORMATTER;


/**
 *
 * @author The Code Warriors
 */
@Component
public class FlooringMasteryOrderDaoFileImpl
        implements FlooringMasteryOrderDao, FlooringMasteryCommonDaoValues {
    
    // Cache of Orders
    public static List<Order> orders = new ArrayList<>();;
    private static int size;
    
    protected String ORDERS_FOLDER = "src/main/resources/Orders";
    protected String ORDERS_FILE_PREFIX = "src/main/resources/Orders/Orders_";
    protected String EXPORT_FILE = "src/main/resources/Backup/DataExport.txt";
    
    // Read order from a text
    private Order unmarshallOrder(String orderAsText, String date){
        String[] orderTokens = orderAsText.split(DELIMITER);
        
        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String state_abbr = orderTokens[2];
        String tax_rate = orderTokens[3];
        String productType = orderTokens[4];
        String area = orderTokens[5];
        String costPerSquareFoot = orderTokens[6];
        String laborCostPerSquareFoot = orderTokens[7];
        String materialCost = orderTokens[8];
        String laborCost = orderTokens[9];
        String tax = orderTokens[10];
        String total = orderTokens[11];
        
        Order orderFromFile = new Order(
            orderNumber,
            date,
            customerName,
            state_abbr,
            tax_rate,
            productType,
            area,
            costPerSquareFoot,
            laborCostPerSquareFoot,
            materialCost,
            laborCost,
            tax,
            total
        );
        
        return orderFromFile;
    }
    
    // Convert order object to String
    private String marshallOrder(Order order){
        String orderAsText = order.getNumber() + DELIMITER;
        orderAsText += order.getCustomerName()+ DELIMITER;
        orderAsText += order.getTaxInfo().getStateAbbr()+ DELIMITER;
        orderAsText += order.getTaxInfo().getTaxRate() + DELIMITER;
        orderAsText += order.getProductInfo().getProductType() + DELIMITER; 
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProductInfo().
                getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getProductInfo().
                getLabourCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTaxCost() + DELIMITER;
        orderAsText += order.getTotal();
        
        return orderAsText;
    }
    
    // Load orders at a specific date
    private List<Order> loadOrdersByDate(LocalDate orderDate) throws 
            OrderNotFoundException {
        
        List<Order> ordersList = new ArrayList<>(orders.stream()
                .filter(o -> o.getDate().equals(orderDate)).toList());
        
        if (ordersList.isEmpty()) {
            String date = orderDate.format(FILE_DATE_FORMATTER);
            List<Order> ordersByDate = new ArrayList<>();

            Scanner scanner;
            
            try {
            
                scanner = new Scanner(new BufferedReader(
                        new FileReader(ORDERS_FILE_PREFIX + date + ".txt")));
            } catch (FileNotFoundException e) {
                throw new OrderNotFoundException(
                        "Orders for this date not found.");
            }

            scanner.nextLine();

            while (scanner.hasNextLine()) {
                // Get the next line in the file
                String currentLine = scanner.nextLine();
                // Unmarshall the line into an order
                Order currentOrder = 
                        unmarshallOrder(currentLine, date);

                ordersByDate.add(currentOrder);
                
                int index = findOrderIndex(currentOrder, orders);
                
                // Caching
                if (index == -1) {
                    orders.add(currentOrder);
                    size++;
                }
            }
            
            return ordersByDate;
        }
        
        return ordersList;
    }
    
    private int findOrderIndex(Order order, List<Order> orders) {
           return orders.indexOf(order);
    }
    
    @Override
    public List<Order> getAllOrders() throws OrderNotFoundException {
        return loadAllOrders();
    }
    
    private List<Order> loadAllOrders() throws OrderNotFoundException {
        
        if (orders.size() == size && size != 0)
            return orders;
        
        orders.clear();
        
        List<Order> ordersList = new ArrayList<>();
        
        File rootOrderDirectory = new File(ORDERS_FOLDER);
        File[] orderFiles = rootOrderDirectory.listFiles();

        for (File orderFile : orderFiles) {
            String date =
                    orderFile.getName()
                    .split("_")[1]
                    .split("[.]")[0];
            

            // Cache all orders
            ordersList.addAll(
                    loadOrdersByDate(
                            LocalDate.parse(
                            date,
                            FILE_DATE_FORMATTER)));
        }
        
        size = orders.size();
       
        return ordersList;
    }
    
    @Override
    public List<Order> writeAllOrders() throws 
            OrderNotFoundException,
            PersistenceException {
        
        List<Order> currentOrders;
                
        try {
        
            currentOrders = loadAllOrders();

            PrintWriter out;
           
            out = new PrintWriter(
                    new FileWriter(EXPORT_FILE, false));
            
            out.println(EXPORT_FILE_HEADER);
            
            for (Order order : currentOrders) {
                String orderAsText = marshallOrder(order) + DELIMITER + 
                        LocalDate.now()
                        .format(EXPORT_DATE_FORMATTER);
                out.println(orderAsText);
            }
            
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new PersistenceException("Export orders failed.");
        }
        
        return currentOrders;
    }

    // Write an order to file based on order's date
    private void writeOrder(Order order) throws PersistenceException {
        String orderAsText = marshallOrder(order);
        
        try {
            File file = new File(
                    ORDERS_FILE_PREFIX + order.getDate().format(
                            FILE_DATE_FORMATTER) + ".txt");
            try (PrintWriter out =
                    new PrintWriter(new FileWriter(file, true))) {
                
                if (file.length() == 0)
                    out.println(ORDER_FILE_HEADER);
                
                out.println(orderAsText);
                out.flush();
            }
        } catch (IOException e) {
            throw new PersistenceException("Writing order failed.");
        }
    }    

    // Write all order to file based on order's date
    private void writeOrders(List<Order> orders, LocalDate orderDate) throws 
            PersistenceException { 
        try {
            
            try (PrintWriter out = new PrintWriter(new FileWriter(
                    ORDERS_FILE_PREFIX +
                    orderDate.format(FILE_DATE_FORMATTER)
                    + ".txt"))) {
                
                out.println(ORDER_FILE_HEADER);
                out.flush();
                
                for(Order order : orders){
                    String orderAsText = marshallOrder(order);
                    out.println(orderAsText);
                }
                
                out.flush();
            }
            
        } catch (IOException e) {
            throw new PersistenceException("Could not save order data.");
        }
        
    }    
    
    @Override
    public Order addOrder(Order order) throws PersistenceException {
        
        writeOrder(order);
        
        orders.add(order);
        size++;
            
        return order;
    }
    
    /*
    For removing an order, the system should ask for the date and order number.
    If it exists, the system should display the order information 
    and prompt the user if they are sure. If yes, it should be removed from 
    the list.
    */
    @Override
    public Order removeOrder(Order order) throws 
            OrderNotFoundException,
            PersistenceException {    
        
        List<Order> currentOrders = loadOrdersByDate(order.getDate());
        int index = findOrderIndex(order, currentOrders);
        ((ArrayList<Order>)currentOrders).remove(index);
        
        orders.remove(order);
        writeOrders(currentOrders, order.getDate());

        return order;
    }
    
    @Override
    public Order editOrder(Order order) throws 
            OrderNotFoundException,
            PersistenceException {  
        
        List<Order> currentOrders = loadOrdersByDate(order.getDate());
        
        Order oldOrder = 
                getOrder(order.getDate(), order.getNumber());
        
        int index = findOrderIndex(oldOrder, currentOrders);
        
        currentOrders.set(index, order);
        
        int cacheIndex = findOrderIndex(oldOrder, orders);
        
        if (cacheIndex != -1)
            orders.set(cacheIndex, order);

        writeOrders(currentOrders, order.getDate());
        
        return order;
    }
    
    @Override
    public List<Order> getOrdersByDate(LocalDate orderDate) throws 
            OrderNotFoundException {
        
        return loadOrdersByDate(orderDate);
    }
    
    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws 
            OrderNotFoundException {
        
        Order orderFound = orders.stream()
            .filter(o -> orderNumber == o.getNumber())
            .findAny()
            .orElse(null);
        
        if(orderFound == null)  {
            orderFound = loadOrdersByDate(orderDate).stream()
                .filter(o -> orderNumber == o.getNumber())
                .findAny()
                .orElse(null);

            if(orderFound == null)          
                throw new OrderNotFoundException(
                        "Order with this order number not found.");
        }
        
        return orderFound;
    }
}
