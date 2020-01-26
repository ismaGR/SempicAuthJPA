/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.Selected;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.Map;

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
    


    private Album current;
    

    @Inject
    private AlbumFacade service;

    @Inject
    @Selected
    private SempicUser selectedUser;


    private Long albumId;

    public Long getAlbumId(){
        return albumId;
    }
    public void setAlbumId(Long albumId){
        this.albumId = albumId;
    }
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
    


    public String save(){
        try {
            Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String albumIdRequest=requestParams.get("albumId");            
            //String albumIdRequest = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("albumId");
            System.out.println("album id from request");
            System.out.println(albumIdRequest);
            albumId = Long.parseLong(albumIdRequest);
            Album album=service.read(albumId);
            album.setGroups(current.getGroups());       
            Logger.getLogger(SessionTools.class.getName()).log(Level.INFO, "albumIdRequest: "+albumIdRequest+" requestParams"+requestParams, "albumIdRequest: "+albumIdRequest+" requestParams"+requestParams);
            Logger.getLogger(SessionTools.class.getName()).log(Level.INFO, "album: "+album, "album: "+album);
            if(album != null && service.update(album) instanceof Album){
                return "success";
            }
            //st.setCurrentAlbum(current);
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "failure";        
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
