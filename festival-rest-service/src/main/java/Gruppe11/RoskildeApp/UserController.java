package Gruppe11.RoskildeApp;

import Objects.RequestUser;
import Objects.User;
import Service.FirebaseService;
import com.google.cloud.firestore.GeoPoint;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @GetMapping("/getUser")
    public String getUser(@RequestHeader String name) throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        return firebaseService.getUserDetails(name);
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody RequestUser requestUser) throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        User user = new User(requestUser.getName(),new GeoPoint(requestUser.getLatitude(),requestUser.getLongitude()));
        return firebaseService.saveUserDetails(user);
    }
}
