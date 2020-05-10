package admin;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import java.util.Scanner;

public class AdminPanel {

    public static void main(String[] args) {

        String email;
        HttpResponse response;
        boolean loggedIn = false;
        boolean active = true;
        Scanner scan = new Scanner(System.in);

        System.out.println("--- Velkommen til admin panel, login for at forsætte ---");

        do {
            if (login())
                loggedIn = true;
        }while (!loggedIn);

        while (active) {
            System.out.println("Tast 1 for at vise alle mails i database");
            System.out.println("Tast 2 for at slettee en bruger");
            System.out.println("Tast 3 for at opdatere en bruger");
            System.out.println("Tast 4 for at afslutte");

            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    System.out.println(Unirest.get("http://localhost:8080/javaboglogin_getAllEmails").asJson().getBody().toString());
                    break;

                case 2:
                    System.out.println("Indast email på den bruger du vil slette");
                    email = scan.next();

                    response = Unirest.delete("http://localhost:8080/javaboglogin_deleteUser").field("email",email).asEmpty();
                    if (response.getStatus() == 200)
                        System.out.println("Bruger slettet");
                    else System.out.println("Der skete en fejl, prøv igen");
                    break;

                case 3:
                    System.out.println("Indtast en email for den bruger du vil opdatere");
                    email = scan.next();
                    System.out.println("Indtast ny latitude");
                    String latitude = scan.next();
                    System.out.println("Indtast ny longitude");
                    String longitude = scan.next();

                    response = Unirest.post("http://localhost:8080/javaboglogin_updateUser")
                            .field("email",email)
                            .field("latitude", latitude)
                            .field("longitude",longitude).asEmpty();
                    if (response.getStatus() == 200)
                        System.out.println("Bruger opdateret");
                    else System.out.println("Der skete en fejl, prøv igen");
                    break;

                case 4:
                    active = false;
                    System.out.println("Afslutter program");
                    break;

                default:
                    break;
            }
        }
    }

    private static boolean login(){
        boolean loggedIn;

        Scanner scan = new Scanner(System.in);

        do{
            System.out.print("Brugernavn:");
            String username = scan.nextLine();
            System.out.print("Kodeord:");
            String password = scan.nextLine();

            String URL = "http://localhost:8080";
            HttpResponse response = Unirest.post(URL +"/javaboglogin").field("name",username).field("password", password).asEmpty();

            if(response.getStatus() == 200){
                loggedIn = true;
            } else {
                System.out.println("Brugernavn eller kodeord er forkert. Prøv igen!");
                loggedIn = false;
            }
        }while(!loggedIn);

        return true;
    }
}
