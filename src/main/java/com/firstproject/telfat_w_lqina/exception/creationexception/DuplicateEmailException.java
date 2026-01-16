package com.firstproject.telfat_w_lqina.exception.creationexception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
