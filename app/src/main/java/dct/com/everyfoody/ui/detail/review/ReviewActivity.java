package dct.com.everyfoody.ui.detail.review;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class ReviewActivity extends WhiteThemeActivity {
    @BindView(R.id.review_toolbar)Toolbar reviewToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        setToolbar();
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
