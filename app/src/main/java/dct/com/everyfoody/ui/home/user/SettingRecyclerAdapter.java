package dct.com.everyfoody.ui.home.user;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.SideMenu;

import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_CHECKED;
import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_UNCHECKED;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class SettingRecyclerAdapter extends RecyclerView.Adapter {
    private List<SideMenu.BookMark> bookMarkList;

    public void refreshAdapter(List<SideMenu.BookMark> bookMarkList){
        this.bookMarkList = bookMarkList;
        notifyDataSetChanged();
    }

    public SettingRecyclerAdapter(List<SideMenu.BookMark> bookMarkList) {
        this.bookMarkList = bookMarkList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_push_list_rcv_item, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingViewHolder)holder).bindView(bookMarkList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookMarkList != null ? bookMarkList.size() : 0;
    }


    public class SettingViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.push_item_name)TextView storeName;
        @BindView(R.id.push_item_switch)SwitchCompat pushSwitch;


        public SettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(SideMenu.BookMark sideItem){
            storeName.setText(sideItem.getStoreName());

            if(sideItem.getToggle() == TOGGLE_CHECKED)
            pushSwitch.setChecked(true);
            else if(sideItem.getToggle() == TOGGLE_UNCHECKED)
                pushSwitch.setChecked(false);
        }


    }
}
