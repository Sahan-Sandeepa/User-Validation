package dev.sahan.authentication;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
    // Check for validation errors
    if (bindingResult.hasErrors()) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        // Construct the error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage("Validation failed");
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }
        errorResponse.setErrors(errorMessages);

        // Return the error response with the appropriate HTTP status code
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Check if the user already exists in the database
    if (userRepository.existsByUsername(user.getUsername())) {
        throw new RuntimeException("Username already exists");
    }
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    // Encode the user's password
    user.setPassword(user.getPassword());

    // Save the user to the database
    User savedUser = userRepository.save(user);

    // Return the saved user
    return ResponseEntity.ok(savedUser);
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
