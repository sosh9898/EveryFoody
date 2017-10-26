package dct.com.everyfoody.base.util;

/**
 * Created by jyoung on 2017. 8. 18..
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        SharedPreferencesService.getInstance().load(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferencesService.getInstance().setPrefData("fcm_token", refreshedToken);

    }
}


