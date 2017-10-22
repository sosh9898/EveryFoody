package dct.com.everyfoody.ui.home.owner;

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

import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_CHECKED;
import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_UNCHECKED;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class OwnerSettingRecyclerAdapter extends RecyclerView.Adapter {
    private List<Integer> ownerSettingList;

    public void refreshAdapter(List<Integer> ownerSettingList){
        this.ownerSettingList = ownerSettingList;
        notifyDataSetChanged();
    }

    public OwnerSettingRecyclerAdapter(List<Integer> ownerSettingList) {
        this.ownerSettingList = ownerSettingList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_push_list_rcv_item, parent, false);
        return new OwnerSettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((OwnerSettingViewHolder)holder).bindView(ownerSettingList.get(position));
    }

    @Override
    public int getItemCount() {
        return ownerSettingList != null ? ownerSettingList.size() : 0;
    }


    public class OwnerSettingViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.push_item_name)TextView storeName;
        @BindView(R.id.push_item_switch)SwitchCompat pushSwitch;


        public OwnerSettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int status){
            switch (getAdapterPosition()){
                case 0:
                    storeName.setText("순번 추가시");
                    checkToggle(status);
                    break;
                case 1:
                    storeName.setText("후기 등록시");
                    checkToggle(status);
                    break;
                case 2:
                    storeName.setText("즐겨찾기 추가시");
                    checkToggle(status);
                    break;
            }

        }

        public void checkToggle(int status){
            if(status == TOGGLE_CHECKED)
                pushSwitch.setChecked(true);
            else if(status == TOGGLE_UNCHECKED)
                pushSwitch.setChecked(false);
        }


    }
}
