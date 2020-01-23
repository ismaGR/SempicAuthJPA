import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
	import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;

@Named
@SessionScoped
public class SempicUserSession implements Serializable {

	private static final long serialVersionUID = -1076122964728342081L;

	@Inject
	private SempicUserFacade appUserService;

	private String email;
	private String password;

	private SempicUser connectedUser;

	// public String login() throws Exception {
	// 	try {
	// 		connectedUser = appUserService.login(email, password);
	// 	} catch (SempicException e) {
	// 		FacesContext facesContext = FacesContext.getCurrentInstance();
	// 		FacesMessage facesMessage = new FacesMessage("Email ou mot de passe incorrects");
	// 		// ajoute le message pour toute la page (1er param==null)
	// 		facesContext.addMessage(null, facesMessage);
    //         // return null;
    //         throw new Exception("img upload KO "+facesContext.getMessages());
	// 	}
	// 	return Pages.list_album_owned;
	// }

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		((HttpSession) context.getExternalContext().getSession(false)).invalidate();
		connectedUser=null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SempicUser getConnectedUser() {
		return connectedUser;
	}
}
