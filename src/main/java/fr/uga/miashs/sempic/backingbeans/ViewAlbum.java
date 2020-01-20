/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

/**
 *
 * @author inhoa
 */
import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.Selected;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class ViewAlbum implements Serializable {
    
    //@Inject
    //@Selected
    private Album current;
    
    @Inject
    private PhotoFacade service;
    
    @Inject 
    private CreatePhoto createPhoto;
    
    
//    public List<CreatePhoto> getPhotos() {
//        //public List<Photo> getPhotos() {
//        try {
//            return service.findAll(current);
//        } catch (SempicModelException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot retrieve the photos",ex.getMessage()));
//        }
//        return Collections.emptyList();
//    }

    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    

    public CreatePhoto getCreatePhoto() {
        return createPhoto;
    }
    
}