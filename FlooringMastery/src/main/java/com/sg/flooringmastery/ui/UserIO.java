package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import java.util.List;

/**
 *
 * @author The Code Warriors
 */
public interface UserIO {
    
    void println(String prompt);
    
    void print(String prompt);
    
    void print(Order order);
    
    double readDouble(String prompt);
    
    double readDouble(String prompt, double min, double max);
    
    float readFloat(String prompt);
    
    float readFloat(String prompt, float min, float max);
    
    int readInt(String prompt);
    
    int readInt(String prompt, int min, int max);
    
    long readLong(String prompt);
    
    long readLong(String prompt, long min, long max);
    
    String readString(String prompt);
    
    String readString(String prompt, List<String> choices);
}
