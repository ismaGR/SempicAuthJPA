/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.entities.SharedAlbum;
import fr.uga.miashs.sempic.qualifiers.Selected;
import fr.uga.miashs.sempic.dao.SharedAlbumDao;


/**
 *
 * @author denisbolshakov
 */

@Named
@SessionScoped
public class SharedAlbums implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<Album> albumList;
    
    @EJB
    private SharedAlbumDao dao;

    @Inject
    @Selected
    private SempicUser connectedUser;



    private SharedAlbum sharedAlbum;

    private Long albumId;

    private String email;

    @EJB
    private SempicUserFacade userDao;
    @EJB
    private AlbumFacade daoAlbum;
    
    public SharedAlbums(){
        
    }

    @PostConstruct
    public void init() {
        sharedAlbum=new SharedAlbum();
        
        sharedAlbum.setReceiver(connectedUser);
    }
    
    // public SempicUser getConnectedUser() {
    //     if(connectedUser == null){
    //         connectedUser = am.getConnectedUser();
    //     }
    //     return connectedUser;
    // }
    
    public String getEmail() {
        return email;
    }
    
     public void setEmail(String email) {
        this.email = email;
    }
    
    public List<SharedAlbum> getSharedAlbumList() {
        System.out.println("Carrot from get: "+dao.getByReceiver(connectedUser));
        return dao.getByReceiver(connectedUser);
    }
    
     public void setSharedAlbum(SharedAlbum a) {
        this.sharedAlbum = a;
    }
    
    public SharedAlbum getSharedAlbum() {
        // if (sharedAlbum == null) {
        //    sharedAlbum = new SharedAlbum(getAlbumId(), );
        //}
        return sharedAlbum;
    }
    
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    
    public Long getAlbumId() {
       if (albumId == null){
            return Long.valueOf(0);
        }
        return albumId;
    }
    
    public String share() {
        try { 
            
            Long recieverId = userDao.readByEmail(email).getId();
            System.out.println("Email: " + email + " albumId: " +
             albumId + " recieverId: " + recieverId);
            dao.create(new SharedAlbum(daoAlbum.getById(albumId), userDao.readByEmail(email)));
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });

            }
        }
        return "";
    }
    
}
