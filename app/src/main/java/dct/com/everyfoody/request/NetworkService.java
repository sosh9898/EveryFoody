package dct.com.everyfoody.request;

import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.model.StoreInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Jyoung on 2017-07-18.
 */

public interface NetworkService {

    @GET("/main/lists/{location}/{latitude}/{longitude}")
    Call<MainList> getMainLists(@Header("token") String token, @Path("location") int location, @Path("latitude") double lat, @Path("longitude") double lon );

    @GET("/store/info/{storeID}")
    Call<StoreInfo> getStoreInfo(@Header("token") String token, @Path("storeID") int storeId);
}
