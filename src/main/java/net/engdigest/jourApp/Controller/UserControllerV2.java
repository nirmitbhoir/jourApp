package net.engdigest.jourApp.Controller;

import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Repository.UserRepository;
import net.engdigest.jourApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserControllerV2 {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // ✅ GET — returns greeting with logged-in username
    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String greeting = "Hi " + authentication.getName();
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    // ✅ PUT — update username and password
    @PutMapping
    public ResponseEntity<?> updateuser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDb = userService.findByUserName(userName);

        if (userInDb == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());

        userService.saveUpdatedUser(userInDb); // ✅ FIXED: encodes password once, preserves roles

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ✅ DELETE — deletes the logged-in user
    @DeleteMapping
    public ResponseEntity<?> deleteuser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}