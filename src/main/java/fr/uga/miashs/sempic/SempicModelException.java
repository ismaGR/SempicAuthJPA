package fr.uga.miashs.sempic;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.ApplicationException;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@ApplicationException(rollback = true)
public class SempicModelException extends Exception {

    /**
     * Creates a new instance of <code>SempicModelException</code> without
     * detail message.
     */
    public SempicModelException() {
    }
    

    /**
     * Constructs an instance of <code>SempicModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SempicModelException(String msg) {
        super(msg);
    }

    public SempicModelException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
