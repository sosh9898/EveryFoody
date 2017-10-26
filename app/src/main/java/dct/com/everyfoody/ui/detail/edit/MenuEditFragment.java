package dct.com.everyfoody.ui.detail.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.StoreInfo;

import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_ADD;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class MenuEditFragment extends Fragment {
    @BindView(R.id.edit_menu_rcv)RecyclerView menuEditRecycler;

    private List<StoreInfo.MenuInfo> menuInfoList;
    private EditMenuRecyclerAdapter editMenuRecyclerAdapter;

    public MenuEditFragment() {
    }

    public static MenuEditFragment getInstance(Bundle bundle){
        MenuEditFragment menuEditFragment = new MenuEditFragment();
        menuEditFragment.setArguments(bundle);
        return menuEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_info_edit, null);
        ButterKnife.bind(this, view);
//        ViewCompat.setNestedScrollingEnabled(menuEditRecycler, false);
        if(getArguments() != null){
            Gson gson = new Gson();
            menuInfoList = gson.fromJson(getArguments().getString("menu"), StoreInfo.class).getDetailInfo().getMenuInfo();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfo();
    }

    private void initInfo(){
        menuEditRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        editMenuRecyclerAdapter = new EditMenuRecyclerAdapter(menuInfoList, getContext());
        menuEditRecycler.setAdapter(editMenuRecyclerAdapter);
    }

    @OnClick(R.id.menu_item_add_btn)
    public void onClickMenuAdd(View view){
        Intent addIntent =new Intent(getContext(), EditMenuActivity.class);
        addIntent.putExtra("addORedit", MENU_ADD);
        startActivity(addIntent);
    }

}
