package dct.com.everyfoody.ui.login;

import android.os.Bundle;

import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class LoginActivity extends WhiteThemeActivity {

    public static final String LOGIN_RESULT = "login-result-key";     //로그인 결과를 Intent로 보낼 때, IntExtra의 key값
    public static final int RESULT_GUEST = 401;     //value값1
    public static final int RESULT_OWNER = 402;     //value값2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
