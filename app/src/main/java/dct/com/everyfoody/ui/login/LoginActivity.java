package dct.com.everyfoody.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.CheckId;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.signup.user.SignUp2Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends WhiteThemeActivity {
    @BindView(R.id.kakao_signup)
    LoginButton kakaoBtn;

    public static final String LOGIN_RESULT = "login-result-key";     //로그인 결과를 Intent로 보낼 때, IntExtra의 key값
    public static final int RESULT_GUEST = 401;     //value값1
    public static final int RESULT_OWNER = 402;     //value값2
    public static final int RESULT_NON_AUTH_OWNER = 403;  // 미인증 사업자
    public static final int EXPIRED_OWNER = 404;          // 만료된 사업

    public static final int LOGIN_KAKAO = 101;
    public static final int LOGIN_FACEBOOK = 102;

    private final int EXIST_ID = 601;
    private final int NONEXIST_ID = 602;

    private NetworkService networkService;
    private SessionCallback callback;
    private CallbackManager callbackManager;
    private JSONObject json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @OnClick(R.id.facebook_login_btn)
    public void facebookLoginClick(final View view) {

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
//                Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(final LoginResult result) {
//
//                GraphRequest request;
//                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//
//                    @Override
//                    public void onCompleted(JSONObject user, GraphResponse response) {
//                        if (response.getError() != null) {
//
//                        } else {
//                            json = user;
//                            final Call<CheckId> getCheckId;
//                            try {
//                                ((LoginActivity)view.getParent())
//                                getCheckId = networkService.checkId(String.valueOf(user.getLong("id")));
//                                getCheckId.enqueue(new Callback<CheckId>() {
//                                    @Override
//                                    public void onResponse(Call<CheckId> call, Response<CheckId> response) {
//                                        if (response.isSuccessful()) {
//                                            if (response.body().getStatus().equals("success")) {
//                                                if (response.body().getIdFlag() == EXIST_ID) {
//                                                    Profile profile = Profile.getCurrentProfile();
//
//                                                    final String link = profile.getProfilePictureUri(200, 200).toString();
//                                                    setResult(RESULT_OK);
//
//                                                    Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
//                                                    try {
//                                                        i.putExtra("id", json.get("email").toString() + "facebook");
//                                                        i.putExtra("profileImage", link);
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    startActivity(i);
//                                                } else {
//                                                    try {
//                                                        startLogin();
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    Intent intent = new Intent(getApplicationContext(), TabActivity.class);
//                                                    startActivity(intent);
//                                                    finish();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<CheckId> call, Throwable t) {
//
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            finish();
//                        }
//                    }
//                });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.e("test", "Error: " + error);
//            }
//
//            @Override
//            public void onCancel() {
//            }
//        });


        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("owner5");
        userInfo.setUid("111");
        userInfo.setCategory(LOGIN_KAKAO);

        Call<Login> loginCall2 = networkService.userLogin(userInfo);

        loginCall2.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesService.getInstance().setPrefData("auth_token", response.body().getResultData().getToken());
                    SharedPreferencesService.getInstance().setPrefData("user_name", response.body().getResultData().getName());
                    int userStatus = response.body().getResultData().getCategory();

                    Intent loginResult = new Intent();
                    if (userStatus == RESULT_GUEST) {
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    } else {
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("sdfsdf", t.toString());
            }
        });

    }

//    public void ddd(){
//
//    }

    @OnClick(R.id.kakao_login_btn)
    public void kakaoLoginClick(View view) {

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        kakaoBtn.performClick();

    }

    public class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            requestMe();
            Log.d("kakaokkkk", "?");

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("kakaokkkk", "??");

            if (exception != null) {
                Log.d("kakao_session_error", exception.toString());
            }
            FacebookSdk.sdkInitialize(LoginActivity.this);
            callbackManager = CallbackManager.Factory.create();
            setContentView(R.layout.activity_login);
        }

        protected void requestMe() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user inof. msg =" + errorResult;
                    Log.v("fail", message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorMessage());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        redirectLoginActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    redirectLoginActivity();
                }

                @Override
                public void onNotSignedUp() {
                    showSignup();
                }

                @Override
                public void onSuccess(final UserProfile result) {

                    checkUid(result);
                }
            });
        }

        protected void showSignup() {
            redirectLoginActivity();
        }


        public void checkUid(final UserProfile userInfo) {

            Log.d("fefe", userInfo.getId() + "");
            Call<CheckId> checkIdCall = networkService.checkId(String.valueOf(userInfo.getId()));

            checkIdCall.enqueue(new Callback<CheckId>() {
                @Override
                public void onResponse(Call<CheckId> call, Response<CheckId> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            ToastMaker.makeShortToast(getApplicationContext(), "성공" + response.body().getIdFlag());
                            if (response.body().getIdFlag() == EXIST_ID)
                                startLogin(userInfo.getEmail(), userInfo.getId());
                            else if (response.body().getIdFlag() == NONEXIST_ID)
                                redirectMainActivity(userInfo.getProfileImagePath(), userInfo.getId(), userInfo.getEmail());

                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckId> call, Throwable t) {

                }
            });
        }

        public void startLogin(String email, long uid) {

            Log.d("??", String.valueOf(uid) + "");
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(email);
            userInfo.setUid(String.valueOf(uid));
            userInfo.setCategory(LOGIN_KAKAO);
            userInfo.setDeviceToken(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));

            Call<Login> loginCall = networkService.userLogin(userInfo);

            loginCall.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        SharedPreferencesService.getInstance().setPrefData("auth_token", response.body().getResultData().getToken());
                        SharedPreferencesService.getInstance().setPrefData("user_name", response.body().getResultData().getName());
                        int userStatus = response.body().getResultData().getCategory();

                        Intent loginResult = new Intent();
                        if (userStatus == RESULT_GUEST) {
                            SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                            loginResult.putExtra(LOGIN_RESULT, userStatus);
                            setResult(RESULT_OK, loginResult);
                            finish();
                        } else {
                            SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                            loginResult.putExtra(LOGIN_RESULT, userStatus);
                            setResult(RESULT_OK, loginResult);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d("sdfsdf", t.toString());
                }
            });
        }

        public void redirectMainActivity(String profileImage, long kakaoId, String email) {
            Intent intent = new Intent(getApplicationContext(), SignUp2Activity.class);
            intent.putExtra("imageUrl", profileImage);
            intent.putExtra("uid", String.valueOf(kakaoId));
            intent.putExtra("email", email);
            intent.putExtra("kakao", LOGIN_KAKAO);
            startActivity(intent);
            finish();
        }

        protected void redirectLoginActivity() {
            final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }
}
