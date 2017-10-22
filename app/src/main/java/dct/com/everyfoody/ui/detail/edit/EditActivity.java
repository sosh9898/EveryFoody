package dct.com.everyfoody.ui.detail.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.model.StoreInfo;

public class EditActivity extends WhiteThemeActivity {
    @BindView(R.id.edit_toolbar)Toolbar editToolbar;
    @BindView(R.id.edit_viewpager)ViewPager editViewPager;
    @BindView(R.id.edit_tablayout)TabLayout editTab;
    @BindView(R.id.edit_main_image)ImageView mainImage;

    private EditPagerAdapter editPagerAdapter;
    private StoreInfo storeInfo;

    public static final int MENU_EDIT = 501;
    public static final int MENU_ADD = 502;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        getStoreInfo();
        setToolbar();
        setLayout();

    }

    private void getStoreInfo(){
        Intent getInfo = getIntent();
        Gson gson = new Gson();
        storeInfo = gson.fromJson(getInfo.getExtras().getString("info"), StoreInfo.class);
    }

    private void setToolbar(){
        editToolbar.setTitle("");
        setSupportActionBar(editToolbar);
        editToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLayout(){
        editPagerAdapter = new EditPagerAdapter(getSupportFragmentManager(), storeInfo);
        editViewPager.setAdapter(editPagerAdapter);
        editTab.addTab(editTab.newTab().setText("기본정보"));
        editTab.addTab(editTab.newTab().setText("메뉴정보"));
        editTab.setupWithViewPager(editViewPager);
        Glide.with(this).load(storeInfo.getDetailInfo().getBasicInfo().getStoreImage()).into(mainImage);
    }

    @OnClick(R.id.menu_item_add_btn)
    public void onClickMenuAdd(View view){
        Intent addIntent =new Intent(getApplicationContext(), EditMenuActivity.class);
        addIntent.putExtra("addORedit", MENU_ADD);
        startActivity(addIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_complete){
            /*TODO
            서버로 정보 전송
             */
        }

        return super.onOptionsItemSelected(item);
    }
}
