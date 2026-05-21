package net.engdigest.jourApp.Service;

import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// ✅ 1. Import Lombok's logging annotation
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j // ✅ 2. This single annotation creates a 'log' variable for you automatically!
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordencoder = new BCryptPasswordEncoder();



    // ✅ Use ONLY for new user registration
    public boolean saveNewUser(User user) {
        try {
            user.setPassword(passwordencoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }
        catch(Exception e){
            log.error("hahaha {}",user.getUserName(),e);
            return false;
        }
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    // ✅ Use for updating existing user credentials — encodes password but preserves roles
    public void saveUpdatedUser(User user) {
        user.setPassword(passwordencoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // ✅ Use for saving user without touching password (e.g. updating journal entry list)
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> get() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}