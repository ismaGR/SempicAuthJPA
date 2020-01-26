/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.dao.PhotoStorage;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.Selected;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import java.nio.file.Path;

/**
 *
 * @author inhoa
 */
@Named
@ViewScoped
// @SessionScoped
public class CreateAlbum implements Serializable {

    private Album current;

    @Inject
    private AlbumFacade service;
    @Inject
    private PhotoFacade servicePhoto;
    @Inject
    private PhotoStorage photoStorage;

    @Inject
    @Selected
    private SempicUser selectedUser;

    private Long photoId;

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    private Long albumId;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public CreateAlbum() {

    }

    @PostConstruct
    public void init() {
        current = new Album();

        current.setOwner(selectedUser);
    }

    public List<Album> findAll() {
        return service.findAlbumsOf(selectedUser);
    }

    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }

    public String save() {
        try {
            Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap();
            String albumIdRequest = requestParams.get("albumId");
            // String albumIdRequest =
            // FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("albumId");
            System.out.println("album id from request");
            System.out.println(albumIdRequest);
            albumId = Long.parseLong(albumIdRequest);
            Album album = service.read(albumId);
            album.setGroups(current.getGroups());
            Logger.getLogger(SessionTools.class.getName()).log(Level.INFO,
                    "albumIdRequest: " + albumIdRequest + " requestParams" + requestParams,
                    "albumIdRequest: " + albumIdRequest + " requestParams" + requestParams);
            Logger.getLogger(SessionTools.class.getName()).log(Level.INFO, "album: " + album, "album: " + album);
            if (album != null && service.update(album) instanceof Album) {

                selectedUser.setTempstr("Album partagé");
                return "success";
            }
            // st.setCurrentAlbum(current);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            selectedUser.setTempstr("Le partage de l'album a échoué");
            return "failure";
        }

        return "failure";
    }

    public String create() {
        // System.out.println(current);

        try {
            // SessionTools st = new SessionTools();
            service.create(current);
            // st.setCurrentAlbum(current);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }

        return "success";
    }

    public String deletePhoto() throws NumberFormatException, IOException {
        try {
            Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String photoIdRequest=requestParams.get("delPhoto");                        
            photoId = Long.parseLong(photoIdRequest);
            Photo photo=servicePhoto.read(photoId);
            Album album = photo.getAlbum();
            Set<Photo> photos =  album.getPhotos();
            photos.remove(photo);
            album.delPhoto(photo);
            
            service.update(album);
            Logger.getLogger(SessionTools.class.getName()).log(Level.INFO, "photo to del: "+photo, "photo: "+photo);
            photoStorage.deletePicture(photoStorage.getPicturePath(Paths.get(photo.getAlbum().getAlbumId()+"", photo.getPhotoId()+"")));
            //int w=Integer.parseInt(Photo.THUMB_WIDTH);            
            photoStorage.deletePicture(photoStorage.getThumbnailPath(Paths.get(photo.getAlbum().getAlbumId()+"", photo.getPhotoId()+""),  Integer.parseInt(Photo.THUMB_WIDTH)));
            servicePhoto.delete(photo);
            selectedUser.setTempstr("Le photo a été supprimé");
            return "success";
        } catch (SempicException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            selectedUser.setTempstr("Echec de la suppression (SempicException)");            
        } catch (SempicModelException e) {
            // TODO Auto-generated catch block
            selectedUser.setTempstr("Echec de la suppression (SempicModelException)");  
        }
        return "failure";
    }
    
}
