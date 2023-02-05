package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author The Code Warriors
 */
@Component
public class UserIOConsoleImpl implements UserIO {
    
    private final Scanner scanner = new Scanner(System.in);

    //use to println something out
    @Override
    public void println(String prompt) {
        System.out.println(prompt);
    }
    
    // use to println prompt and get user input and return user input as String
    @Override
    public String readString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
    
    @Override
    public int readInt(String prompt) {
        while(true) {
            try{
                return Integer.parseInt(this.readString(prompt));
            } catch (NumberFormatException e) {
                this.println("Input Error. Please try again.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do {
            result = this.readInt(prompt);
        } while(result < min || result > max);
        return result;
    }

    @Override
    public double readDouble(String prompt) {
        while(true) {
            try{
                return Double.parseDouble(this.readString(prompt));
            } catch (NumberFormatException e) {
                this.println("Input error. Please try again.");
            }
            
        }
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result;
        do{
            result = readLong(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public float readFloat(String prompt) {
        while(true) {
            try{
                return Float.parseFloat(this.readString(prompt));
            } catch (NumberFormatException e) {
                this.println("Input error. Please try again.");
            }
        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result;
        do{
            result = readFloat(prompt);
        } while(result < min || result > max);
        
        return result;
    }


    @Override
    public long readLong(String prompt) {
        while(true) {
            try{
                return Long.parseLong(this.readString(prompt));
            }catch(NumberFormatException e) {
                this.println("Input Error. Please try again.");
            }
        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
         long result;
        do {
            result = this.readLong(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    // if user's choice is not in the list of choices, keep asking for new choice
    public String readString(String prompt, List<String> choices) {
        
        String choice;
        
        do{
            choice = this.readString(prompt);
        }while(!choices.contains(choice));
        
        return choice;
    }

    @Override
    public void print(String prompt) {
        System.out.print(prompt);
    }

    @Override
    public void print(Order order) {
        System.out.println(order);
    }


}
