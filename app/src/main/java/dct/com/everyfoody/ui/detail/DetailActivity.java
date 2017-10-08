package dct.com.everyfoody.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.OrangeThemeActivity;
import dct.com.everyfoody.ui.detail.edit.EditActivity;
import dct.com.everyfoody.ui.detail.location.MapActivity;
import dct.com.everyfoody.ui.detail.normal.NormalFragment;
import dct.com.everyfoody.ui.detail.review.ReviewActivity;

public class DetailActivity extends OrangeThemeActivity {
    @BindView(R.id.detail_toolbar)Toolbar detailToolbar;
    @BindView(R.id.detail_main_image)ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getInitData();

        if(savedInstanceState == null){
            addFragment(NormalFragment.getInstance(), null);
            //TODO 네트워킹 이후 번들로 넘겨줄 데이터 받을 것
        }
        setToolbar();
    }

    private void getInitData(){
        //TODO 네트워킹
    }

    private void setToolbar(){
        //Glide.with(this).load().into(mainImage); TODO    네트워킹 정보를 바탕으로 이미지 및 툴바 타이틀 설정
        //detailToolbar.setTitle();
        detailToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(detailToolbar);
        detailToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    public void addFragment(Fragment fragment, Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.detail_content, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.menu_detail_map){
            Intent mapIntent = new Intent(this, MapActivity.class);
            startActivity(mapIntent);
        }
        else if(id == R.id.menu_detail_bookmark_on || id == R.id.menu_detail_bookmark_off){
            /*TODO
            즐겨찾기 상태 판별해서 네트워킹
             */
        }
        else if(id == R.id.menu_detail_edit){
            Intent editIntent = new Intent(this, EditActivity.class);
            startActivity(editIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem map = menu.findItem(R.id.menu_detail_map);
        MenuItem bookmarkOn = menu.findItem(R.id.menu_detail_bookmark_on);
        MenuItem bookmarkOff = menu.findItem(R.id.menu_detail_map);

        /*TODO
        게스트면 맵, 즐겨찾기 사업자면 수정
         */

        return super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.review_btn)
    public void reviewClick(View view){
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        startActivity(reviewIntent);
    }

}
