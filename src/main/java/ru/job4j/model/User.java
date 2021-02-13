package ru.job4j.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "j_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;

	@JsonbTransient
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public static User of(String name, String email, String password, Role role) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.role = role;
        return user;
    }
    
    public static User of(Integer id, Role role) {
        User user = new User();
        user.id = id;
        user.name = null;
        user.email = null;
        user.password = null;
        user.role = null;
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
}
