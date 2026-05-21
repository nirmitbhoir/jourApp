package net.engdigest.jourApp.Controller;

import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {  //unauthenticated

    @Autowired
    private UserService userService;

    @GetMapping("/end")
    public String check(){
        return "OK";
    }

    @PostMapping("/create-user")
    public void createusers(@RequestBody User user){
        userService.saveNewUser(user);
    }
}
