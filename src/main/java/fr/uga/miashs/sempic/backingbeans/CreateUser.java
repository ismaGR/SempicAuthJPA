/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.io.Serializable;
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
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class CreateUser implements Serializable {
    
    private SempicUser current;
    
    @Inject
    private SempicUserFacade userDao;
    
    public CreateUser() {
    }
    
    @PostConstruct
    public void init() {
        current=new SempicUser();
    }


    public SempicUser getCurrent() {
        return current;
    }

    public void setCurrent(SempicUser current) {
        this.current = current;
    }
    
    public String getPassword() {
        return null;
    }
    
    
    public void setPassword(@NotBlank(message="Password is required") String password) {
        getCurrent().setPassword(password);
    }
    
    /*public String generateHash(String s) {
        return hashAlgo.generate(s.toCharArray());
    }*/
    
    public String create() {
        //System.out.println(current);
        
        try {
            userDao.create(current);
        } 
        catch (SempicModelUniqueException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("un utilisateur avec cette adresse mail existe déjà"));
            return "failure";
        }
        catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        if(this.getCurrent().getUserType().equals("ADMIN")){
           return "admin"; 
        }else{
            return "user";
        }
    }
}
