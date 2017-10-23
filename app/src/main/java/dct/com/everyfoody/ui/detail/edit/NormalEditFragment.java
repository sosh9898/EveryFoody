package dct.com.everyfoody.ui.detail.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.StoreInfo;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class NormalEditFragment extends Fragment {
    @BindView(R.id.edit_operation_time)EditText operationEdit;
    @BindView(R.id.edit_break_time)EditText breakTiemEdit;
    @BindView(R.id.edit_hashtag)EditText hashtagEdit;
    @BindView(R.id.edit_phone_number)EditText phoneNumEdit;
    @BindView(R.id.edit_facebook_url)EditText facebookEdit;
    @BindView(R.id.edit_twitter_url)EditText twitterEdit;
    @BindView(R.id.edit_instagram_url)EditText instagramEdit;

    private StoreInfo.BasicInfo basicInfo;

    public NormalEditFragment() {
    }

    public static NormalEditFragment getInstance(Bundle bundle){
        NormalEditFragment normalEditFragment = new NormalEditFragment();
        normalEditFragment.setArguments(bundle);
        return normalEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_info_edit, null);
        ButterKnife.bind(this, view);
        if(getArguments() != null){
            Gson gson = new Gson();
            basicInfo = gson.fromJson(getArguments().getString("basic"), StoreInfo.BasicInfo.class);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initInfo();
    }

    private void initInfo(){
        operationEdit.setText(basicInfo.getStoreOpentime());
        breakTiemEdit.setText(basicInfo.getStoreBreaktime());
        hashtagEdit.setText(basicInfo.getStoreHashtag());
        phoneNumEdit.setText(basicInfo.getStorePhone());
        facebookEdit.setText(basicInfo.getStoreFacebookURL());
        twitterEdit.setText(basicInfo.getStoreTwitterURL());
        instagramEdit.setText(basicInfo.getStoreInstagramURL());
    }

    public StoreInfo.BasicInfo getEditInfo(){
        basicInfo.setStoreBreaktime(breakTiemEdit.getText().toString());
        basicInfo.setStoreOpentime(operationEdit.getText().toString());
        basicInfo.setStoreHashtag(hashtagEdit.getText().toString());
        basicInfo.setStorePhone(phoneNumEdit.getText().toString());
        basicInfo.setStoreFacebookURL(facebookEdit.getText().toString());
        basicInfo.setStoreInstagramURL(instagramEdit.getText().toString());
        basicInfo.setStoreTwitterURL(twitterEdit.getText().toString());

        return basicInfo;
    }
}
