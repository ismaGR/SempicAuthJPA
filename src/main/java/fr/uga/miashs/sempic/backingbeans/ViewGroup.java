///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package fr.uga.miashs.sempic.backingbeans;
//
//import fr.uga.miashs.sempic.SempicModelException;
//import fr.uga.miashs.sempic.dao.GroupFacade;
//import fr.uga.miashs.sempic.dao.PhotoFacade;
//import fr.uga.miashs.sempic.entities.Album;
//import fr.uga.miashs.sempic.entities.Photo;
//import fr.uga.miashs.sempic.entities.SempicGroup;
//import java.io.Serializable;
//import java.util.Collections;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
//import javax.inject.Named;
//
///**
// *
// * @author inhoa
// */    
//@Named
//@ViewScoped
//public class ViewGroup implements Serializable {
//    
//    /*@Inject
//    @Selected*/
//    private SempicGroup current;
//    
//    @Inject
//    private GroupFacade service;
//    
//    @Inject 
//    private CreateGroup createGroup;
//    
//    
//    //public List<CreatePhoto> getPhotos() {
//        public List<SempicGroup> getGroup() {
//        try {
//            return service.findAll(current);
//        } catch (SempicModelException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot retrieve the photos",ex.getMessage()));
//        }
//        return Collections.emptyList();
//    }
//
//    public SempicGroup getCurrent() {
//        return current;
//    }
//
//    public void setCurrent(SempicGroup current) {
//        this.current = current;
//    }
//    
//
//    public CreatePhoto getCreatePhoto() {
//        return createPhoto;
//    }
//    
// public List<> getPhotos() {
//        try {
//            return service.findAll(current);
//        } catch (SempicModelException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot retrieve the photos",ex.getMessage()));
//        }
//        return Collections.emptyList();
//    }
//    
//}
