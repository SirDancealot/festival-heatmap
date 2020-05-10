package Gruppe11.RoskildeApp;

import Objects.Coordinates;
import Objects.SaveObject;
import Objects.User;
import Service.FirebaseService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.type.LatLng;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @PostMapping ("/logintest")
    public Object test(@RequestParam ("token") String token) throws ExecutionException, InterruptedException {
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
    @PostMapping ("/saveUser")
    public Object createUser(@RequestBody SaveObject obj) throws ExecutionException, InterruptedException {
        String token = obj.getToken();
        Double latitude = obj.getLatitude();
        Double longitude = obj.getLongitude();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport,jsonFactory).
                setAudience(Collections.singletonList("323786655673-drp7qhjh87inj687gn9qhrr5lnugstg8.apps.googleusercontent.com")).build();

        try {
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null){
                GoogleIdToken.Payload payload = idToken.getPayload();
                System.out.println(payload.toString());

                FirebaseService firebaseService = FirebaseService.getInstance();
                User user = new User(payload.getEmail(),new GeoPoint(latitude,longitude));
                firebaseService.saveUserDetails(user);

                return new ResponseEntity(HttpStatus.OK);
            }else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

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
    @CrossOrigin
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

