package Gruppe11.RoskildeApp;

import Objects.User;
import Service.FirebaseService;
import brugerautorisation.transport.rmi.Brugeradmin;
import com.google.cloud.firestore.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

@RestController
public class JavaBogUserController {

    @PostMapping("/javaboglogin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity validateUser(@RequestParam("name") String name,
                                       @RequestParam("password") String password) throws RemoteException, NotBoundException, MalformedURLException {

        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        try {

            ba.hentBruger(name, password);
            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/javaboglogin_deleteUser")
    public ResponseEntity deleteUser(@RequestParam ("email") String email){
        FirebaseService firebaseService = FirebaseService.getInstance();

        try {
            firebaseService.deleteUser(new User(email));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/javaboglogin_updateUser")
    public ResponseEntity updateUser(@RequestParam ("email") String email, @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude){
        FirebaseService firebaseService = FirebaseService.getInstance();
        try {
            firebaseService.saveUserDetails(new User(email, new GeoPoint(latitude, longitude)));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
