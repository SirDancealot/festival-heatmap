package Objects;

import com.google.cloud.firestore.GeoPoint;
import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.stereotype.Component;

@Component
public class User {
    private String email;
    private GeoPoint coordinates;
    private String geohash;

    public User(String email){
        this.email = email;
    }

    public User(String name, GeoPoint coordinates) {
        this.email = name;
        setL(coordinates);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getG() {
        return geohash;
    }
    public void setG(String g) {
        this.geohash = g;
    }
    public GeoPoint getL() {
        return coordinates;
    }
    public void setL(GeoPoint l) {
        this.coordinates = l;
        geohash = GeoHashUtils.encode(coordinates.getLatitude(),coordinates.getLongitude());
    }
}
