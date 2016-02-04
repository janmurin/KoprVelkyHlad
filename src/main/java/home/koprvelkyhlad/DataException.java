/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.koprvelkyhlad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Janco1
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataException extends RuntimeException{

    public DataException(String message) {
        super(message);
    }
    
    
    
}
