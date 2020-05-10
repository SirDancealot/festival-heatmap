package Service;

import Objects.Coordinates;
import Objects.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    private static FirebaseService INSTANCE;
    private FirebaseService() {}

    public static FirebaseService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FirebaseService();
        }
        return INSTANCE;
    }

    public Boolean getUserDetails(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference col = db.collection("coordinates");
        ApiFuture<DocumentSnapshot> future = col.document(email).get();
        return future.get().exists();
    }

    public void saveUserDetails(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference col = db.collection("coordinates");
        ApiFuture<WriteResult> future = col.document(user.getEmail()).set(user);
        future.get();
    }

    public void deleteUser(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference col = db.collection("coordinates");
        ApiFuture<WriteResult> future = col.document(user.getEmail()).delete();
        future.get();
    }

    public ArrayList<String> getGeoPoints() throws ExecutionException, InterruptedException {
        ArrayList<String> geoPoints = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents in the coordinates
        ApiFuture<QuerySnapshot> future = db.collection("coordinates").get();
        // Gets all geopoints as strings.
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            geoPoints.add(document.getString("g"));
        }
        return geoPoints;
    }

    public Coordinates getUserCoordinates(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("coordinates").document(user.getEmail());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if(document.exists()) {
            String g = document.getString("g");
            double[] d = GeoHashUtils.decode(g);
            return new Coordinates(d[0], d[1]);
        } else {
            return null;
        }
    }

    public ArrayList<String> getAllUsers() throws ExecutionException, InterruptedException {

        ArrayList<String> users = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("coordinates").get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document: documents) {
            users.add(document.getString("email"));
        }
        return users;
    }
}
