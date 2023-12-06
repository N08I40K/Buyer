package ru.n08i40k.buyer.exceptions;

public class EmptyListException extends Exception {
    public EmptyListException() {
        super("The list must be non-empty!");
    }
}
