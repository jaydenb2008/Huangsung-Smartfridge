package edu.sdccd.cisc191.server.exceptions;

/**
 * This exception is called when a food cannot be found in the database.
 */

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(Long id) {
        super("Could not find food " + id);

    }
}
