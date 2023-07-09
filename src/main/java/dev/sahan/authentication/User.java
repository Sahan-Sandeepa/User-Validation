package dev.sahan.authentication;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    public User(){}

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }
    public void setPassword(String password2) {
        this.password = password2;
    }
    public void setEmail(String email2) {
        this.email = email2;
    }
    
}