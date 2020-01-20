/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.dao.PhotoFacade;
import java.io.Serializable;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;

import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.Selected;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Part;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author inhoa
 */
@Named
@ViewScoped
public class CreatePhoto implements Serializable {
    
    //@Inject
    //@Selected
    private Album selectedtAlbum;    

    private Album album;

    private Long albumId;

    public Long getAlbumId(){
        return this.albumId;
    }

    public void setAlbumId(Long id){
        this.albumId=id;
    }

    public Album getAlbum(){
        return this.album;
    }
    /*
     * @param album the album to set
     */
    public void setAlbum(Album album) {
        this.album = album;
    }
    @Inject
    private AlbumFacade albumDao;

    @Inject
    @Selected
    private SempicUser selectedUser;


    @Inject
    @Selected
    private Album selectedAlbum;

    private SempicUser user;

    public SempicUser getUser(){
        return this.user;
    }
    public void setUser(SempicUser user){
        this.user = user;
    }
    private Photo current;

    public Photo getCurrent(){
        return this.current;
    }
    public void setCurrent(Photo photo){
        this.current = photo;
    }
    private List<Part> files;

    private Part file;
          
    private static final Map <String, String> mimeTypes; // static - one obj for all
    
    static {
        Map <String, String> aMap = new HashMap <String, String>();
        aMap.put("image/png", "png");
        aMap.put("image/jpeg", "jpeg");
        mimeTypes = Collections.unmodifiableMap(aMap);
    }    

    @Inject
    private PhotoFacade service;
    
    public CreatePhoto() {
        
    }
    
    @PostConstruct
    public void init() {
        this.current = new Photo();
        this.user = selectedUser;
        this.album = selectedAlbum;
    }


    
    public List<Part> getFiles() {
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
        //current.setFilename("");//file.getName());
    }


    public Part getFile(){
        return file;
    }
    public void setFile(Part f){
        this.file = f;
    }    
    private String createSha1(InputStream fis) throws IOException  {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
             int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
            return  DatatypeConverter.printHexBinary(digest.digest());

        } catch (NoSuchAlgorithmException ex) {
            //  System.out.println(ex.getMessage());
        } 
        return null;
    }    
    public void save() throws Exception {
        try  {
            InputStream input = file.getInputStream();
            String mime = file.getContentType();
            
            if (mimeTypes.containsKey(mime)){
                
                String fileName = createSha1(input) + "." + mimeTypes.get(mime);
                input = file.getInputStream(); // mark(0) doesnt work, so we initialize again
            //  String fileName = "test.jpeg";
                System.out.println(fileName);
                //throw new Exception("OK "+fileName);
            } 
            
        }
        catch (IOException e) {
            throw new Exception("img upload KO "+e.getMessage());
        }
    }    
    public String create() {
        Album album = albumDao.read(this.albumId);
        Logger.getLogger(Album.class.getName()).log(Level.INFO, album+"", album);
        

        if (this.current.getAlbum()==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("parameter albumId must be given"));
            return "failure";
        }
        Logger.getLogger(Album.class.getName()).log(Level.INFO, null, album);
        boolean partiallyFailed=false;       
            Logger.getLogger(Album.class.getName()).log(Level.INFO, null, file);
            current=new Photo();
            current.setAlbum(album);
            try {
                service.create(current,file.getInputStream());
            } catch (SempicModelException ex) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
               partiallyFailed=true; 
               
            } catch (IOException ex) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
                 partiallyFailed=true;
                
            }
        
        if (partiallyFailed) {
             return "failure";
        }
        else {
            init();
            return "success";
        }
    }
}
