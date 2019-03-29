package org.masterylearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String username;

    public String fullname;

    @Column(nullable = false, unique = true)
    public String email;

    /**
     * The ui mode that we are going to use, defaults to flow mode.
     */
    public String mode = "flow";

    /**
     * WARNING: do not remove JsonIgnore flag or else the password will be visible to the user when
     * the user object is serialized. In general the User object should not be directly returned
     * from endpoints but a dto object should be used.
     */
    @JsonIgnore
    public String password;

    protected User () { }

    public User (String fullname, String email, String username, String password)
    {
        this.email = email;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<> ();

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    public List<CourseHistory> courseHistoryList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return getRoles ().stream ()
                          .map (role -> (GrantedAuthority) () -> "ROLE_" + role.name)
                          .collect (Collectors.toList ());
    }

    @Override
    public String getPassword () {
        return password;
    }

    @Override
    public String getUsername () {
        return username;
    }

    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }

    @Override
    public boolean isEnabled () {
        return true;
    }

    public List<Role> getRoles () {
        return roles;
    }

    public List<CourseHistory> getCourseHistoryList () {
        return courseHistoryList;
    }
}
