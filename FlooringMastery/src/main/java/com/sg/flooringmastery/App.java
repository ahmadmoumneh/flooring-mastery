package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;

import org.springframework.context.annotation.
        AnnotationConfigApplicationContext;

/**
 *
 * @author The Code Warriors
 */
public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext appCon
                = new AnnotationConfigApplicationContext();
       
        appCon.scan("com.sg.flooringmastery");
        appCon.refresh();
        
        FlooringMasteryController con = appCon.getBean(
            "flooringMasteryController",
            FlooringMasteryController.class
        );
        
        con.run();
    }
}