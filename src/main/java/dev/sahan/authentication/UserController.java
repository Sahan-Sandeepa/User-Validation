package dev.sahan.authentication;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
   
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode the user's password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Return the saved user
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        // Retrieve all users from the database
        List<User> users = userRepository.findAll();

        // Return the list of users
        return users;
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        // Find the user by username and password
        User existingUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        // Check if the user exists and the password is correct
        if (existingUser == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Return the authenticated user
        return ResponseEntity.ok(existingUser);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") String id, @RequestBody User user) {
        // Find the user by id
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user details
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());

        // Save the updated user to the database
        User updatedUser = userRepository.save(existingUser);

        // Return the updated user
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        // Find the user by id
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete the user from the database
        userRepository.delete(existingUser);

        // Return no content response
        return ResponseEntity.noContent().build();
    }
}
