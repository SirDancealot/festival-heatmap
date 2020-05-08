package brugerautorisation.transport.rmi;


import brugerautorisation.data.Bruger;

@SuppressWarnings("NonAsciiCharacters")
public interface Brugeradmin extends java.rmi.Remote {
    /**
     * Henter alle en brugers data
     *
     * @return et Bruger-objekt med alle data
     */
    Bruger hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

}

