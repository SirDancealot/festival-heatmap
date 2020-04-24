package Service;

import Objects.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

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

    public String getUserDetails(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference col = db.collection("coordinates");
        ApiFuture<DocumentSnapshot> future = col.document(name).get();
        return future.get().getUpdateTime().toString();
    }

    public String saveUserDetails(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference col = db.collection("coordinates");
        ApiFuture<WriteResult> future = col.document(user.getName()).set(user);
        future.get();
        return future.get().getUpdateTime().toString();
    }


}
