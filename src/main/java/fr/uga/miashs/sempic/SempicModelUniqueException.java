/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
public class SempicModelUniqueException extends SempicModelException {

    public SempicModelUniqueException() {
    }

    public SempicModelUniqueException(String msg) {
        super(msg);
    }

    public SempicModelUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
