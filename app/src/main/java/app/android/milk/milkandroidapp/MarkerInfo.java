package app.android.milk.milkandroidapp;

/**
 * Created by billy_000 on 3/11/2018.
 */

public class MarkerInfo {
    String title;
    double latitude;
    double longitude;

    MarkerInfo(String _title, double _latitude, double _longitude) {
        setTitle(_title);
        setLatitude(_latitude);
        setLongitude(_longitude);
    }

    void setTitle(String t) {
        title = t;
    }
    void setLatitude(double lat) {
        latitude = lat;
    }
    void setLongitude(double lon) {
        longitude = lon;
    }

    String getTitle() {
        return title;
    }
    double getLatitude() {
        return latitude;
    }
    double getLongitude() {
        return longitude;
    }
}