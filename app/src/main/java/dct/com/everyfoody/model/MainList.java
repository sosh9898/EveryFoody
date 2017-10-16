package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 15..
 */

public class MainList extends BaseModel{


    @SerializedName("data")
    @Expose
    private List<TruckList> truckLists = null;

    public List<TruckList> getTruckLists() {
        return truckLists;
    }

    public void setTruckLists(List<TruckList> truckLists) {
        this.truckLists = truckLists;
    }

    public class TruckList {

        @SerializedName("storeID")
        @Expose
        private Integer storeID;
        @SerializedName("storeName")
        @Expose
        private String storeName;
        @SerializedName("storeImage")
        @Expose
        private String storeImage;
        @SerializedName("reservationCount")
        @Expose
        private Integer reservationCount;
        @SerializedName("storeLocation")
        @Expose
        private String storeLocation;
        @SerializedName("storeDistance")
        @Expose
        private float storeDistance;
        @SerializedName("storeDistanceUnit")
        @Expose
        private String storeDistanceUnit;

        public Integer getStoreID() {
            return storeID;
        }

        public void setStoreID(Integer storeID) {
            this.storeID = storeID;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreImage() {
            return storeImage;
        }

        public void setStoreImage(String storeImage) {
            this.storeImage = storeImage;
        }

        public Integer getReservationCount() {
            return reservationCount;
        }

        public void setReservationCount(Integer reservationCount) {
            this.reservationCount = reservationCount;
        }

        public String getStoreLocation() {
            return storeLocation;
        }

        public void setStoreLocation(String storeLocation) {
            this.storeLocation = storeLocation;
        }

        public float getStoreDistance() {
            return storeDistance;
        }

        public void setStoreDistance(float storeDistance) {
            this.storeDistance = storeDistance;
        }

        public String getStoreDistanceUnit() {
            return storeDistanceUnit;
        }

        public void setStoreDistanceUnit(String storeDistanceUnit) {
            this.storeDistanceUnit = storeDistanceUnit;
        }
    }

}