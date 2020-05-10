package Gruppe11.RoskildeApp;

import Objects.Coordinates;
import Objects.SaveObject;
import Objects.User;
import Service.FirebaseService;
import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.protobuf.InvalidProtocolBufferException;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @PostMapping("/logintest")
    public Object test(@RequestParam("token") String token) throws ExecutionException, InterruptedException {
        try {

            // Validate en token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            // Få en email
            decodedToken.getEmail();

            return new ResponseEntity(HttpStatus.OK);

        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveUser")
    public Object createUser(@RequestBody SaveObject obj) throws ExecutionException, InterruptedException {
        String token = obj.getToken();
        Double latitude = obj.getLatitude();
        Double longitude = obj.getLongitude();

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            User user = new User(decodedToken.getEmail(), new GeoPoint(latitude, longitude));
            FirebaseService firebaseService = FirebaseService.getInstance();
            firebaseService.saveUserDetails(user);

            return new ResponseEntity(HttpStatus.OK);

        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

    }

    @CrossOrigin
    @GetMapping("/allGeolocs")
    public ArrayList<String> showUser() throws ExecutionException, InterruptedException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        return firebaseService.getGeoPoints();
    }

    @CrossOrigin
    @GetMapping("/locationSeperate")
    public ArrayList<Coordinates> getCoordinates() throws ExecutionException, InterruptedException, InvalidProtocolBufferException {
        FirebaseService firebaseService = FirebaseService.getInstance();
        ArrayList<Coordinates> geoPoints = new ArrayList<>();
        ArrayList<String> geohash = firebaseService.getGeoPoints();
        for (int i = 0; i < geohash.size(); i++) {
            double[] d = GeoHashUtils.decode(geohash.get(i));
            geoPoints.add(new Coordinates(d[0], d[1]));
        }
        return geoPoints;
    }


    @CrossOrigin
    @ResponseBody
    @PostMapping ("/updateUser")
    public Object updateUserLoc(@RequestBody SaveObject obj) {
        FirebaseService firebaseService = FirebaseService.getInstance();
        String token = obj.getToken();
        Double latitude = obj.getLatitude();
        Double longitude = obj.getLongitude();

        try {
            FirebaseToken dToken = FirebaseAuth.getInstance().verifyIdToken(token);

            if(firebaseService.getUserDetails(dToken.getEmail())) {
                firebaseService.saveUserDetails(new User(dToken.getEmail(), new GeoPoint(latitude,longitude)));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteUser")
    public Object deleteUser(@RequestParam("token") String token){
        FirebaseService firebaseService = FirebaseService.getInstance();

        try {
            FirebaseToken dToken = FirebaseAuth.getInstance().verifyIdToken(token);

            if(firebaseService.getUserDetails(dToken.getEmail())) {
                firebaseService.deleteUser(new User(dToken.getEmail()));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/userCoor")
    public Coordinates userCoor(@RequestParam("token") String token) {
        FirebaseService firebaseService = FirebaseService.getInstance();
        Coordinates coordinates = null;
        try {
            FirebaseToken dToken = FirebaseAuth.getInstance().verifyIdToken(token);

            if(firebaseService.getUserDetails(dToken.getEmail())) {
                coordinates = firebaseService.getUserCoordinates(new User(dToken.getEmail()));
            }
        } catch (FirebaseAuthException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return coordinates;
        //hey
    }
}
