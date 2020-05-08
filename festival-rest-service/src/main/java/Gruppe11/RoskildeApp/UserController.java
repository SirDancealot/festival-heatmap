package Gruppe11.RoskildeApp;

import Objects.User;
import Objects.RequestUser;
import Service.FirebaseService;
import com.google.cloud.firestore.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @GetMapping("/getUser")
    public String getUser(/*@RequestHeader String name*/) throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        return "bund";
    }


    @ResponseBody
    @GetMapping ("/saveUser")
    public Object createUser(/*@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude*/) throws ExecutionException, InterruptedException {

       // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       // if (!(authentication instanceof AnonymousAuthenticationToken)) {

            // get details from logged in user and regex match the email
            String name = "harcdodedemail@email.com";
            Pattern p = Pattern.compile("[a-zA-Z0-9-_.]+@[a-zA-Z0-9-_.]+");
            Matcher m = p.matcher(name);

            // if email is found save it in database with geoCords
            if (m.find()) {
                User user = new User(m.group());

                //TODO test POST request with geoCords
                // User user = new User(m.group(), new GeoPoint(latitude,longitude));
                FirebaseService firebaseService = FirebaseService.getInstance();
                firebaseService.saveUserDetails(user);


                // return HTTP response status 200
                return new ResponseEntity(HttpStatus.OK);
            }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //TODO Get all user geoLocs from db and save them in a list
    @GetMapping("/allUsers")
    public void showUser(){


    }

    //TODO update a users geoLoc - search the db for the user's email and update the geolocs
    @PostMapping("/updateUser")
    public void updateUserLoc(@RequestParam ("latitude") Double latitude, @RequestParam("longitude") Double longitude){

    }

    //TODO delete a user from the database with given email
    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam ("email")String email){

    }
}
