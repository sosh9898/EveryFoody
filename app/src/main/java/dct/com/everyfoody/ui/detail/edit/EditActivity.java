package dct.com.everyfoody.ui.detail.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;

public class EditActivity extends AppCompatActivity {
    @BindView(R.id.edit_toolbar)Toolbar editToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        setToolbar();
        if(savedInstanceState == null){
            addFragment(NormalEditFragment.getInstance(),null);
        }

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

    public void addFragment(Fragment fragment, Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.edit_content, fragment);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.edit_content, fragment);
        transaction.commit();
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

    @OnClick(R.id.edit_normal_btn)
    public void normalClick(View view){
        replaceFragment(NormalEditFragment.getInstance(), null);
    }

    @OnClick(R.id.edit_menu_btn)
    public void menuClick(View view){
        replaceFragment(MenuEditFragment.getInstance(), null);
    }
}
