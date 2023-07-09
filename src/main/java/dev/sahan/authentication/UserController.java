package dev.sahan.authentication;

import dev.sahan.authentication.User;
import dev.sahan.authentication.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        // Retrieve all users from the database
        List<User> users = userRepository.findAll();

        // Return the list of users
        return users;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Check if the user already exists in the database
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Return the saved user
        return savedUser;
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User user) {
        // Find the user by username and password
        User existingUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        // Check if the user exists and the password is correct
        if (existingUser == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Return the authenticated user
        return existingUser;
    }

    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable("id") String id, @RequestBody User user) {
        // Find the user by id
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user details
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());

        // Save the updated user to the database
        User updatedUser = userRepository.save(existingUser);

        // Return the updated user
        return updatedUser;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        // Find the user by id
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete the user from the database
        userRepository.delete(existingUser);
    }

}
