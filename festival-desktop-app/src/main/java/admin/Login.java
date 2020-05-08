package admin;

import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;
import java.util.Scanner;

public class Login {
    public static boolean login(String username) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Scanner sc = new Scanner(System.in);
        String password;
        System.out.print("Enter password: ");
        for (int i = 0; i < 3; i++) {
            password = sc.nextLine();
            try {
                ba.hentBruger(username, password);
                return true;
            } catch (Exception e) {
                if (i == 2){
                    System.out.println("Maximum login attempts reached, system exiting");
                    System.exit(1);
                }else{
                    System.out.print("Wrong password\nTry again: ");
                }
            }
        }
        return false;
    }
}