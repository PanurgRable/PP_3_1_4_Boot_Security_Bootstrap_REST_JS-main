package ru.kata.spring.boot_security.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new HashSet<>();

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public String getShortRoles() {
        if (roles.isEmpty()) {
            return "NONE";
        } else if (roles.toString().equals("[ROLE_USER]")) {
            return "USER";
        } else if (roles.toString().equals("[ROLE_ADMIN]")) {
            return "ADMIN";
        }
        return "ADMIN USER";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getFirstName().equals(user.getFirstName()) && getLastName().equals(user.getLastName()) && getEmail().equals(user.getEmail()) && getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail(), getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", roles='" + getShortRoles() + '\'' +
                '}';
    }

    public static List<User> getUsersListDefault() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("Brian", "Fox", "br.fox@gmail.com", "brifox", "$2a$12$O7FOtdAuQidpMIcj2/P67uv2dGFstk1XqlLRVz1KU51thm97Zk5v6")); //brbr
        userList.add(new User("Lettice", "Mayanama", "let.may@gmail.com", "letmay", "$2a$12$Te5ZlbyYoFJP5BiSwF2dWOy3912xDx0xcuddWHkThde6ddEpHlQBO")); //letlet
        userList.add(new User("Hardy", "Nutts", "hardnut@hotmail.com", "harnut", "$2a$12$7IT6xpJn8YQpNIvSpPR1qeAs/OdQHE0DwtUF9PyJRhPfky7Qtpebe")); //nut
        userList.add(new User("Felicia", "Montano", "fel.montano@gmail.com", "felmon", "$2a$12$deAScTdBmU9K6bXqkvduiOOYQoF6cDw78Qm0NHJqSmeinAVUuyI4W")); //fff
        userList.add(new User("Watt", "Ettoh", "watt.ettoh@yahoo.eu", "wateto", "$2a$12$d8O3Al/eZWWe1sTHTKO6H./1dzyMY2.UxDb94ELqYMub0RormUUlW")); //wwwww
        return userList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles != null
                ? new HashSet<>(roles)
                : Collections.emptySet();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}