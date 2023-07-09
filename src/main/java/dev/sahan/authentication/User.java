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
    public Object getUsername() {
        return null;
    }
    public Object getPassword() {
        return null;
    }
    public Object getEmail() {
        return null;
    }
    public void setUsername(Object username2) {
    }
    public void setPassword(Object password2) {
    }
    public void setEmail(Object email2) {
    }

    
}