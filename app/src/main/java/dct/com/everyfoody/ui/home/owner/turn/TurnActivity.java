package dct.com.everyfoody.ui.home.owner.turn;

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
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Turn;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnActivity extends WhiteThemeActivity {
    @BindView(R.id.turn_toolbar)Toolbar turnToolbar;
    @BindView(R.id.turn_rcv)RecyclerView turnRecycler;

    private List<Turn.TurnInfo> turnInfoList;
    private NetworkService networkService;
    private TurnRecyclerAdapter turnRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        turnInfoList = new ArrayList<>();
        setToolbar();
        setRecycler();
        getTurnList();

    }

    private void setRecycler(){
        turnRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        turnRecyclerAdapter = new TurnRecyclerAdapter(turnInfoList, getApplicationContext());
        turnRecycler.setAdapter(turnRecyclerAdapter);
    }

    private void getTurnList(){
        Call<Turn> turnCall = networkService.getTurnList(SharedPreferencesService.getInstance().getPrefStringData("auth_token"));

        turnCall.enqueue(new Callback<Turn>() {
            @Override
            public void onResponse(Call<Turn> call, Response<Turn> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        turnInfoList = response.body().getTurnList();
                        turnRecyclerAdapter.refreshAdapter(turnInfoList);
                        ToastMaker.makeShortToast(getApplicationContext(), "성공");

                    }
                }
            }

            @Override
            public void onFailure(Call<Turn> call, Throwable t) {
            }
        });
    }

    private void setToolbar(){
        turnToolbar.setTitle("");
        setSupportActionBar(turnToolbar);
        turnToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        turnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*FIXME
        위에 주석 풀어주고 아래 지웁시다!!
         */

        if (id == R.id.menu_notify_true) {
            nextGuest();
        }


        return super.onOptionsItemSelected(item);
    }

    private void nextGuest(){
        Call<BaseModel> nextCall = networkService.nextGuset(SharedPreferencesService.getInstance().getPrefStringData("auth_token"));

        nextCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        ToastMaker.makeShortToast(getApplicationContext(),"성공");
                        turnInfoList.remove(0);
                        turnRecyclerAdapter.refreshAdapter(turnInfoList);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }

}
