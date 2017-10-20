package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 10. 20..
 */

public class OpenLocation {

    @SerializedName("opentruck_latitude")
    @Expose
    private double latitude;
    @SerializedName("opentruck_longtitude")
    @Expose
    private double longtitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
