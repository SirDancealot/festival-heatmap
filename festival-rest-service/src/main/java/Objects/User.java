package Objects;

import com.google.cloud.firestore.GeoPoint;
//import org.elasticsearch.index.search.geo.GeoHashUtils;
import org.springframework.stereotype.Component;

@Component
public class User {
    private String name;
    private GeoPoint coordinates;
    private String geohash;

    public User(String name){
        this.name = name;
    }

    public User(String name, GeoPoint coordinates) {
        this.name = name;
        setL(coordinates);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        //geohash = GeoHashUtils.encode(coordinates.getLatitude(),coordinates.getLongitude());
    }
}
