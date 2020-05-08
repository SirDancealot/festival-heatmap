package Gruppe11.RoskildeApp;

import brugerautorisation.transport.rmi.Brugeradmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@RestController
public class JavaBogUserController {


    private Brugeradmin ba;

    @PostMapping("/javaboglogin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity validateUser(@RequestParam("name") String name,
                                       @RequestParam("password") String password) throws RemoteException, NotBoundException, MalformedURLException {

        ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        try {

            ba.hentBruger(name, password);
            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
