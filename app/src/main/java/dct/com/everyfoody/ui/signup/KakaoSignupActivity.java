package dct.com.everyfoody.ui.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.request.NetworkService;

/**
 * Created by jyoung on 2017. 8. 16..
 */

public class KakaoSignupActivity extends Activity {
    private NetworkService service;

    private final int EXIST_ID = 601;
    private final int NONEXIST_ID = 600;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesService.getInstance().load(this);
        service = ApplicationController.getInstance().getNetworkService();

    }

//    protected void requestMe(){
//        UserManagement.requestMe(new MeResponseCallback() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                String message = "failed to get user inof. msg =" + errorResult;
//                Log.v("fail", message);
//
//                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorMessage());
//                if(result == ErrorCode.CLIENT_ERROR_CODE){
//                    finish();
//                }else {
//                    redirectLoginActivity();
//                }
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
//            }
//
//            @Override
//            public void onNotSignedUp() {
//                showSignup();
//            }
//
//            @Override
//            public void onSuccess(final UserProfile result) {
//
//                checkUid(result);
//
//                redirectMainActivity(result.getProfileImagePath(), result.getId(), result.getEmail());
//            }
//        });
//    }
//
//    protected  void showSignup(){
//        redirectLoginActivity();
//    }
//
//
//
//    private void checkUid(final UserProfile userInfo){
//
//        Log.d("fefe", userInfo.getId()+"");
//        Call<CheckId> checkIdCall = service.checkId(String.valueOf(userInfo.getId()));
//
//        checkIdCall.enqueue(new Callback<CheckId>() {
//            @Override
//            public void onResponse(Call<CheckId> call, Response<CheckId> response) {
//                if(response.isSuccessful()){
//                    if(response.body().getStatus().equals("success")){
//                        ToastMaker.makeShortToast(getApplicationContext(), "성공"+response.body().getIdFlag());
//                        if(response.body().getIdFlag() == EXIST_ID)
//                            startLogin(userInfo.getEmail(), userInfo.getId());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CheckId> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void startLogin(String email, double uid){
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setEmail(email);
//        userInfo.setUid(String.valueOf(uid));
//        userInfo.setCategory(LOGIN_KAKAO);
//
//        Call<Login> loginCall = service.userLogin(userInfo);
//
//        loginCall.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                if (response.isSuccessful()) {
//                    SharedPreferencesService.getInstance().setPrefData("auth_token", response.body().getResultData().getToken());
//                    SharedPreferencesService.getInstance().setPrefData("user_name", response.body().getResultData().getName());
//                    int userStatus = response.body().getResultData().getCategory();
//
//                    Intent loginResult = new Intent();
//                    if (userStatus == RESULT_GUEST) {
//                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
//                        loginResult.putExtra(LOGIN_RESULT, userStatus);
//                        setResult(RESULT_OK, loginResult);
//                        finish();
//                    } else {
//                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
//                        loginResult.putExtra(LOGIN_RESULT, userStatus);
//                        setResult(RESULT_OK, loginResult);
//                        finish();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                Log.d("sdfsdf", t.toString());
//            }
//        });
//    }
//
//    public void redirectMainActivity(String profileImage, long kakaoId, String email){
//        Intent intent = new Intent(this, SignUp2Activity.class);
//        intent.putExtra("imageUrl", profileImage);
//        intent.putExtra("uid", String.valueOf(kakaoId));
//        intent.putExtra("email", email);
//        intent.putExtra("kakao", LOGIN_KAKAO);
//        startActivity(intent);
//        finish();
//    }
//
//    protected  void redirectLoginActivity(){
//        final Intent intent = new Intent(this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//        finish();
//    }
}
