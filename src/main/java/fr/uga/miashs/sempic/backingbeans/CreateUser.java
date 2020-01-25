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
import fr.uga.miashs.sempic.qualifiers.Selected;

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
import javax.persistence.Transient;
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

    @Inject
    @Selected
    private SempicUser selectedUser;

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
        if(this.getCurrent().getPassword().equals("ADMIN")){
           return "admin"; 
        }else{
            return "user";
        }
    }

    public Boolean verifMdp() throws Exception{
        Boolean res= true;
        
        if(selectedUser.getPassword().equals(selectedUser.getTempstr())){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Les mots de passe sont différents!, veuillez confirmer avec le même mot de passe"));
         res=false;
        }

            return res;
    }

    public Boolean allowUpdate() throws Exception{
        Boolean res= false;
             if(selectedUser.getUserType().equals("ADMIN")){
                res= true;
            }else{
                res= false;
            }
            return res;
    }

    public String update() throws Exception{
        String res = "false";
        if(userDao.readByEmail(selectedUser.getEmail())!=null)
        try {
            if(selectedUser.getTempstr().equals(selectedUser.getPassword())){
           userDao.update(selectedUser); selectedUser.setTempstr("Utilisateur Modifié avec succes");
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Utilisateur Modifié avec succes"));
           res="sucess";
            }else { 
                res="failure";
                selectedUser.setTempstr("mot de passe différent !");
            }         
        } 
        catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Echec de la Modification de l'utilisateur"));
            res= "failure";
        }

        return res;

    }

    
}
