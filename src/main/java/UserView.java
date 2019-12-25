import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.model.datalayer.*;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.*;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@SessionScoped
public class UserView implements Serializable {

    private SempicUser selected;

    @EJB
    private SempicUserFacade dao;

    public String create() throws SempicModelException {
        try {
            dao.create(selected);
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

    public SempicUser getSelected() {
        if (selected == null) {
            selected = new SempicUser();
        }
        return selected;
    }

    public void setSelected(SempicUser selected) {
        this.selected = selected;
    }
}