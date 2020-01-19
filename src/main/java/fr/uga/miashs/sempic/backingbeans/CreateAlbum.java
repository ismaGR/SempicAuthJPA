/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.Selected;
import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;



/**
 *
 * @author inhoa
 */
@Named
@ViewScoped
//@SessionScoped
public class CreateAlbum implements Serializable{
    

    @Inject
    @Selected
    private Album currentAlbum;    

    private Album current;
    

    @Inject
    private AlbumFacade service;

    @Inject
    @Selected
    private SempicUser selectedUser;


    public CreateAlbum() {
        
    }
    
    @PostConstruct
    public void init() {
        current=new Album();
        
        current.setOwner(selectedUser);
    }
    
    public List<Album> findAll(){
        return service.findAlbumsOf(selectedUser);
    }


    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    
    public String create() {
        //System.out.println(current);
        
        try {
            //SessionTools st = new SessionTools();            
            service.create(current);
            //st.setCurrentAlbum(current);
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }

    
}
