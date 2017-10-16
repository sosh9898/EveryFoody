package dct.com.everyfoody.ui.detail.edit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class EditActivity extends WhiteThemeActivity {
    @BindView(R.id.edit_toolbar)Toolbar editToolbar;
    @BindView(R.id.edit_viewpager)ViewPager editViewPager;
    @BindView(R.id.edit_tablayout)TabLayout editTab;

    private EditPagerAdapter editPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        setToolbar();
        setLayout();

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
        editPagerAdapter = new EditPagerAdapter(getSupportFragmentManager());
        editViewPager.setAdapter(editPagerAdapter);
        editTab.addTab(editTab.newTab().setText("기본정보"));
        editTab.addTab(editTab.newTab().setText("메뉴정보"));
        editTab.setupWithViewPager(editViewPager);
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
