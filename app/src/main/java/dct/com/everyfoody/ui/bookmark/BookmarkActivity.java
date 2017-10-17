package dct.com.everyfoody.ui.bookmark;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkActivity extends WhiteThemeActivity {
    @BindView(R.id.bookmark_toolbar)
    Toolbar bookmarkToolbar;
    @BindView(R.id.bookmark_rcv)
    RecyclerView bookmarkRecycler;

    private NetworkService networkService;
    private List<MainList.TruckList> bookmarkList;
    private BookmarkRecyclerAdapter bookmarkRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        setToolbar();
        getBookmarkList();
        setRecycler();


    }

    private void getBookmarkList() {
        networkService = ApplicationController.getInstance().getNetworkService();

        Call<MainList> bookmarkListCall = networkService.getBookmarkList(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), 44, 44);

        bookmarkListCall.enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        bookmarkList = new ArrayList<MainList.TruckList>();
                        bookmarkList = response.body().getTruckLists();
                        bookmarkRecyclerAdapter.refreshAdapter(bookmarkList);

                    }
                }
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {

            }
        });

    }

    private void setRecycler() {
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookmarkRecyclerAdapter = new BookmarkRecyclerAdapter(bookmarkList, onClickListener);
        bookmarkRecycler.setAdapter(bookmarkRecyclerAdapter);
    }

    private void setToolbar() {
        bookmarkToolbar.setTitle("");
        setSupportActionBar(bookmarkToolbar);
        bookmarkToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        bookmarkToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIc = menu.findItem(R.id.menu_delete);
        MenuItem completeIc = menu.findItem(R.id.menu_complete);


        return super.onPrepareOptionsMenu(menu);
    }


}
