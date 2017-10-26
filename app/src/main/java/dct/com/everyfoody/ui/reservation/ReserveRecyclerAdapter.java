package dct.com.everyfoody.ui.reservation;

import android.content.Context;
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
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by jyoung on 2017. 10. 4..
 */

public class ReserveRecyclerAdapter extends RecyclerView.Adapter {
    private List<Reservation.Store> reservationList;
    private View.OnClickListener onClickListener;
    private Context context;
    private int holderType;

    private final int NORMAL = 800;
    private final int CANCEL = 801;

    public void refreshAdapter(List<Reservation.Store> reservationList) {
        this.reservationList = reservationList;
        notifyDataSetChanged();
    }

    public ReserveRecyclerAdapter(List<Reservation.Store> reservationList, View.OnClickListener onClickListener) {
        this.reservationList = reservationList;
        this.onClickListener = onClickListener;
        holderType = NORMAL;
    }

    public ReserveRecyclerAdapter(List<Reservation.Store> reservationList, View.OnClickListener onClickListener, Context context) {
        this.reservationList = reservationList;
        this.onClickListener = onClickListener;
        this.context = context;
        holderType = CANCEL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservation_rcv_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ReserveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holderType == NORMAL)
        ((ReserveViewHolder) holder).normalBindView(reservationList.get(position));
        else
            ((ReserveViewHolder)holder).cancelBindView(reservationList.get(position));

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
        @BindView(R.id.reservation_delete)ImageView reservationDelete;

        private NetworkService networkService;

        public ReserveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            networkService = ApplicationController.getInstance().getNetworkService();
            SharedPreferencesService.getInstance().load(context);

        }

        public void normalBindView(Reservation.Store reservationItem) {
            Glide.with(reservationItemImage.getContext()).load(reservationItem.getStoreImageURL()).into(reservationItemImage);
            reservationTruckName.setText(reservationItem.getStoreName());
            reservationCount.setText(reservationItem.getReservationCount()+"");
            reservationTime.setText(reservationItem.getReservationTime());
        }

        public void cancelBindView(final Reservation.Store reservationItem) {
            Glide.with(reservationItemImage.getContext()).load(reservationItem.getStoreImageURL()).into(reservationItemImage);
            reservationTruckName.setText(reservationItem.getStoreName());
            reservationCount.setText(reservationItem.getReservationCount()+"");
            reservationTime.setText(reservationItem.getReservationTime());

            reservationDelete.setVisibility(View.VISIBLE);
            reservationDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<BaseModel> cancelCall = networkService.userReseve(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), reservationItem.getStoreID());

                    cancelCall.enqueue(new Callback<BaseModel>() {
                        @Override
                        public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus().equals("success")){
                                    ToastMaker.makeShortToast(context, "성공");
                                    reservationList.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseModel> call, Throwable t) {

                        }
                    });
                }
            });
        }

    }
}
