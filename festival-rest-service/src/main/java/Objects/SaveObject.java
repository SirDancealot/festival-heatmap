package Objects;

import org.springframework.stereotype.Component;

@Component
public class SaveObject  {
    String token;
    Double latitude, longitude;

    public SaveObject(String token, Double latitude, Double longitude) {
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
