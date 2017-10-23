package dct.com.everyfoody.ui.signup.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp2Activity extends AppCompatActivity {
    @BindView(R.id.signup_name_edit)EditText nameEdit;
    @BindView(R.id.signup_phone_edit)EditText phoneEdit;

    private String uid, email, imageUrl;
    private int category;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        getData();
    }

    private void getData(){
        Intent getData = getIntent();
        uid = getData.getExtras().getString("uid");
        email = getData.getExtras().getString("email");
        imageUrl = getData.getExtras().getString("imageUrl");
        category = getData.getExtras().getInt("kakao");

    }

    @OnClick(R.id.signup_btn)
    public void onClickSignUp(View view){
        UserInfo userInfo = new UserInfo();
        userInfo.setImageURL(imageUrl);
        userInfo.setCategory(category);
        userInfo.setUid(uid);
        userInfo.setEmail(email);
        userInfo.setPhone(phoneEdit.getText().toString());
        userInfo.setName(nameEdit.getText().toString());

        Call<BaseModel> signUpCall = networkService.userSignUp(userInfo);

        signUpCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Log.d("???", t.toString());
            }
        });
    }
}
