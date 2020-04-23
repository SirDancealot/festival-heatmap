package Controller;

import Objects.User;
import Service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class Usercontroller {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/getUser")
    public String getUser(@RequestHeader String name) {
        User usr = new User("bund",133,133);
        return usr.getName();
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return firebaseService.saveUserDetails(user);
    }

}
