package Objects;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Coordinates implements Serializable {
    double lat;
    double lng;

    public Coordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
