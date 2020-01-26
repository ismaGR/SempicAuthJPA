/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */


@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})
})
@NamedQueries({
@NamedQuery(
        name = "query.SempicUser.findAllEager", 
        query = "SELECT DISTINCT u FROM SempicUser u LEFT JOIN FETCH u.groups LEFT JOIN FETCH u.memberOf"
),
@NamedQuery(
        name = "query.SempicUser.readByEmail",
        query = "SELECT DISTINCT u FROM SempicUser u WHERE u.email=:email "
),
@NamedQuery(
        name = "query.SempicUser.login",
        query = "SELECT DISTINCT u FROM SempicUser u WHERE u.email=:email AND u.passwordHash=:passwordHash"
)
})
@NamedEntityGraph(
  name = "graph.SempicUser.groups-memberOf",
  attributeNodes = {
    @NamedAttributeNode("groups"),
    @NamedAttributeNode("memberOf"),
  }
)
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class SempicUser implements Serializable {
    public final static String PREFIX="/users/";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message="Un nom de famille doit être donné")
    private String lastname;
    
    @NotBlank(message="Un prénom doit être donné")
    private String firstname;
    
    @Email
    @NotBlank(message="Une adresse mail doit être donnée")
    private String email;
    
    @NotBlank(message="Un mot de passe doit être donné")
    private String passwordHash;
    
    @Transient
    private transient String password;

    @Transient
    private transient String tempstr;
    
    @OneToMany(mappedBy = "owner",cascade = CascadeType.REMOVE)
    private Set<SempicGroup> groups;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.REMOVE)
    private Set<Album> albums;

    @ManyToMany(mappedBy = "members" )//,cascade = CascadeType.REMOVE)//, fetch=FetchType.EAGER
    private Set<SempicGroup> memberOf;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition="VARCHAR(5)")
    private SempicUserType userType;
    


    public SempicUser() {
        userType=SempicUserType.USER;
    }
    
    public long getId() {
        return id;
    }
    
    
    public String getIdUri(){
        return "/users"+getId();
    }
    
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public void setPassword(String p) {
        password=p;
    }
    @XmlTransient
    public String getPassword() {
        return password;
    }
    
    @XmlIDREF
    public Set<SempicGroup> getGroups() {
        if (groups==null) return Collections.emptySet();
        return Collections.unmodifiableSet(groups);
    }

    @XmlIDREF
    public Set<Album> getAlbums() {
        if (albums==null) return Collections.emptySet();
        return Collections.unmodifiableSet(albums);
    }    

    @XmlIDREF
    public Set<SempicGroup> getMemberOf() {
        if (memberOf==null) return Collections.emptySet();
        return Collections.unmodifiableSet(memberOf);
    }
    
    public SempicUserType getUserType() {
        return userType;
    }

    public void setUserType(SempicUserType userType) {
        this.userType = userType;
    }

    public String getTempstr() {
        String msg= tempstr;

        tempstr = "";
        return msg;
    }

    public void setTempstr(String tempstr) {
        this.tempstr = tempstr;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SempicUser other = (SempicUser) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

 
    @Override
    public String toString() {
        return "SempicUser{id="+ id + ", "
                + "lastname=" + lastname + ", firstname=" + firstname + ", email=" + email + '}';
    }

  
}
