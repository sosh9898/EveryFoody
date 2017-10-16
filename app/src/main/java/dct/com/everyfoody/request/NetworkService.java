package dct.com.everyfoody.request;

import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.model.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jyoung on 2017-07-18.
 */

public interface NetworkService {

    //로그인
    @POST("/signin")
    Call<Login> userLogin(@Body UserInfo userInfo);

    //메인
    @GET("/main/lists/{location}/{latitude}/{longitude}")
    Call<MainList> getMainLists(@Header("token") String token, @Path("location") int location, @Path("latitude") double lat, @Path("longitude") double lon );

    //디테일
    @GET("/store/info/{storeID}")
    Call<StoreInfo> getStoreInfo(@Header("token") String token, @Path("storeID") int storeId);

    //예약하기
    @GET("/reservation/compilation/{storeID}")
    Call<BaseModel> userReseve(@Header("token") String token, @Path("storeID") int storeId);

    //즐겨찾기
    @GET("/bookmark/compilation/{storeID}")
    Call<BaseModel> userBookmark(@Header("token") String token, @Path("storeID") int storeId);

    //즐겨찾기 리스트
    @GET("/bookmark/lists/{latitude}/{longitude}")
    Call<MainList> getBookmarkList(@Header("token") String token, @Path("latitude") int lat, @Path("longitude") int log);

}
