/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.dao.GroupFacade;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.io.Serializable;
import java.util.List;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class CreateGroup implements Serializable {
    
    private SempicGroup current;
    
    @Inject
    private GroupFacade groupDao;
    
    @Inject
    private SempicUserFacade userDao;

    
    public CreateGroup() {
        
    }
    
    @PostConstruct
    public void init() {
        current=new SempicGroup();
    }
    
    public void setOwnerId(String id) {
        System.out.println(id); 
        current.setOwner(userDao.read(Long.valueOf(id)));
    }
    
    public String getOwnerId() {
        if (current.getOwner()==null)
            return "-1";
        return ""+current.getOwner().getId();
    }

    
    public List<SempicUser> getUsers() {
        return userDao.findAll();
    }

    public SempicGroup getCurrent() {
        return current;
    }

    public void setCurrent(SempicGroup current) {
        this.current = current;
    }
    
    public String create() {
        System.out.println(current);
        
        try {
            groupDao.create(current);
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
}
