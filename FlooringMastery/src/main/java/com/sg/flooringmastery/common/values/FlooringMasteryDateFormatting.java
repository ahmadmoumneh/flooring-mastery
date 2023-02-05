/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.flooringmastery.common.values;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author The Code Warriors
 */
public interface FlooringMasteryDateFormatting {
    public static DateTimeFormatter INPUT_DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    public static DateTimeFormatter FILE_DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("MMddyyyy");
    
    public static DateTimeFormatter EXPORT_DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    public static LocalDate parseInputDate(String date) throws
            DateTimeParseException {
        
        return LocalDate.parse(date, INPUT_DATE_FORMATTER);
    }
    
    public static LocalDate parseFileDate(String date) throws
            DateTimeParseException {
        
        return LocalDate.parse(date, FILE_DATE_FORMATTER);
    }
    
    public static LocalDate parseExportDate(String date) throws
            DateTimeParseException {
        
        return LocalDate.parse(date, EXPORT_DATE_FORMATTER);
    }
}
