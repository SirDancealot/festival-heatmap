package com.example.festival_app;

public class UserInformation {

    private static UserInformation UserInformationInstance = new UserInformation();

    private String userEmail;
    private double latitude;
    private double longitude;
    private String access_type;
    private String token;

    private UserInformation() {}

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static UserInformation getInstance() {
        if (UserInformationInstance == null){
            UserInformationInstance = getInstance();
        }
        return UserInformationInstance;
    }
}
