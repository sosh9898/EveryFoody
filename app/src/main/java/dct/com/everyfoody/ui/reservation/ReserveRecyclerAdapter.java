package dct.com.everyfoody.ui.reservation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.Reservation;


/**
 * Created by jyoung on 2017. 10. 4..
 */

public class ReserveRecyclerAdapter extends RecyclerView.Adapter {
    private List<Reservation.Store> reservationList;
    private View.OnClickListener onClickListener;

    public void refreshAdapter(List<Reservation.Store> reservationList) {
        this.reservationList = reservationList;
        notifyDataSetChanged();
    }

    public ReserveRecyclerAdapter(List<Reservation.Store> reservationList, View.OnClickListener onClickListener) {
        this.reservationList = reservationList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservation_rcv_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ReserveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReserveViewHolder) holder).bindView(reservationList.get(position));
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }


    public class ReserveViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reservation_item_image)ImageView reservationItemImage;
        @BindView(R.id.reservation_truck_name)TextView reservationTruckName;
        @BindView(R.id.reservation_count)TextView reservationCount;
        @BindView(R.id.reservation_time)TextView reservationTime;

        public ReserveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Reservation.Store reservationItem) {
            Glide.with(reservationItemImage.getContext()).load(reservationItem.getStoreImageURL()).into(reservationItemImage);
            reservationTruckName.setText(reservationItem.getStoreName());
            reservationCount.setText(reservationItem.getReservationCount()+"");
            reservationTime.setText(reservationItem.getReservationTime());

        }
    }
}
