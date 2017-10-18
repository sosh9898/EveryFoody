package dct.com.everyfoody.request;

import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.model.ResLocation;
import dct.com.everyfoody.model.ResReview;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.model.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    //예약 내역
    @GET("/reservation/lists")
    Call<Reservation> getReservationList(@Header("token") String token);

    //즐겨찾기
    @GET("/bookmark/compilation/{storeID}")
    Call<BaseModel> userBookmark(@Header("token") String token, @Path("storeID") int storeId);

    //즐겨찾기 리스트
    @GET("/bookmark/lists/{latitude}/{longitude}")
    Call<MainList> getBookmarkList(@Header("token") String token, @Path("latitude") int lat, @Path("longitude") int log);

    //리뷰 리스트
    @GET("/review/lists/{storeID}")
    Call<ResReview> getReviewList(@Header("token") String token, @Path("storeID") int storeId);

    //리뷰 등록
    @Multipart
    @POST("/review/registration")
    Call<BaseModel> registerReview(@Header("token") String token,
                                   @Part MultipartBody.Part files,
                                    @Part("storeID") RequestBody storeId,
                                    @Part("score") RequestBody score,
                                    @Part("content") RequestBody content);

    //가게 위치
    @GET("/store/location/{storeID}")
    Call<ResLocation> getLocation(@Header("token") String token, @Path("storeID") int storeId);
}
