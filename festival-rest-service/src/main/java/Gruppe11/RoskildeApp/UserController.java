package Gruppe11.RoskildeApp;

import Objects.Coordinates;
import Objects.User;
import Service.FirebaseService;
import com.google.cloud.firestore.GeoPoint;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.type.LatLng;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {
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

    @GetMapping("/allGeolocs")
    public ArrayList<String> showUser() throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        return firebaseService.getGeoPoints();
    }

    @GetMapping("/locationSeperate")
    public ArrayList<Coordinates> getCoordinates() throws ExecutionException, InterruptedException, InvalidProtocolBufferException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        ArrayList<Coordinates> geoPoints = new ArrayList<>();
        ArrayList<String> geohash = firebaseService.getGeoPoints();
        for (int i = 0; i < geohash.size(); i++) {
            double[] d = GeoHashUtils.decode(geohash.get(i));
            geoPoints.add(new Coordinates(d[0],d[1]));
        }
        return geoPoints;
    }

    /**
     * Updates a users geohash
     * @param email
     * @param latitude
     * @param longitude
     * @return HTTP statuscode
     */
    @PostMapping("/updateUser")
    public Object updateUserLoc(@RequestParam("email") String email, @RequestParam ("latitude") Double latitude, @RequestParam("longitude") Double longitude) throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        if(firebaseService.getUserDetails(email)) {
            try {
                firebaseService.saveUserDetails(new User(email, new GeoPoint(latitude, longitude)));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * Delete user upon given email
     * @param email
     * @return HTTP statuscode
     */
    @DeleteMapping("/deleteUser")
    public Object deleteUser(@RequestParam ("email") String email){
        FirebaseService firebaseService = FirebaseService.getInstance();
        try {
            firebaseService.deleteUser(new User(email));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}

