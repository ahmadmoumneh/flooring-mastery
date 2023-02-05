package com.sg.flooringmastery.dao.exceptions;

/**
 *
 * @author The Code Warriors
 */
public class PersistenceException extends FlooringMasteryException {

    public PersistenceException(String error) {
        super(error);
    }

    public PersistenceException(String error, Throwable cause) {
        super(error, cause);
    }
    
}
