package edu.sdccd.cisc191.server.exceptions;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(Long id) {
        super("Could not find food " + id);

    }
}
