package dct.com.everyfoody.ui.detail.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.ResReview;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends WhiteThemeActivity {
    @BindView(R.id.review_toolbar)Toolbar reviewToolbar;
    @BindView(R.id.review_rcv)RecyclerView reviewRecycler;

    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private NetworkService networkService;
    private List<ResReview.Review> reviewList;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        reviewList = new ArrayList<>();
        Intent getId = getIntent();
        storeId = getId.getExtras().getInt("storeId");

        setToolbar();
        setRecycler();
        getReviewList();
    }

    private void getReviewList(){
        Call<ResReview> getReviewList = networkService.getReviewList(SharedPreferencesService.getInstance().getPrefStringData("auth_token"),storeId);

        getReviewList.enqueue(new Callback<ResReview>() {
            @Override
            public void onResponse(Call<ResReview> call, Response<ResReview> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        reviewList = response.body().getResult().getReviews();
                        reviewRecyclerAdapter.refreshAdapter(reviewList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResReview> call, Throwable t) {

            }
        });
    }

    private void setRecycler(){
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(reviewList);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
    }

    private void setToolbar(){
        reviewToolbar.setTitle("");
        setSupportActionBar(reviewToolbar);
        reviewToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        reviewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
