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
public class SempicException extends Exception {

    public SempicException(String message) {
        super(message);
    }

    public SempicException(String message, Throwable cause) {
        super(message, cause);
    }
}
