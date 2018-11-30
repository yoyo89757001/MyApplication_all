package com.example.yqtlaifangdengji.ui;

/**
 * Created by Administrator on 2018/7/3.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.bean.ChaXun;
import com.example.yqtlaifangdengji.utils.GlideCircleTransform;


import java.util.List;



/**
 * Created  2018/1/15.
 */


public class MoBanAdapter extends RecyclerView.Adapter<MoBanAdapter.ViewHolder> {


    private List<ChaXun.ListBean> list;
    private Context context;
    private int dw,dh;
    private OnRvItemClick mOnRvItemClick;
    private RequestOptions myOptions2 = null;


    public MoBanAdapter(List<ChaXun.ListBean> list, Context context, int dw, int dh, OnRvItemClick onRvItemClick)
    {
        this.list = list;
        this.context=context;
        this.dw=dw;
        this.dh=dh;
        this.mOnRvItemClick=onRvItemClick;

        myOptions2 = new RequestOptions()
                //   .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
                .transform(new GlideCircleTransform(context));

      }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moban, parent, false);



           return new ViewHolder(view);
      }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.bumen.setText(list.get(position).getDepartmentName());
        try {

            Glide.with(context)
                    .load(list.get(position).getDisplayPhoto())
                    .apply(myOptions2)
                    .into(holder.touxiang);

        } catch (Exception e) {
            e.printStackTrace();
        }

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.touxiang.getLayoutParams();
            params2.width=(int)(dh*0.1f);
            params2.height=(int)(dh*0.1f);
            holder. touxiang.setLayoutParams(params2);
            holder. touxiang.invalidate();

            RecyclerView.LayoutParams params22 = (RecyclerView.LayoutParams) holder.cardView.getLayoutParams();
            params22.width=(int)(dh*0.33f);
            params22.height=(int)(dh*0.16f);
            holder. cardView.setLayoutParams(params22);
            holder. cardView.invalidate();



     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,bumen;
        ImageView touxiang;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
             touxiang =itemView.findViewById(R.id.touxiang);
             name = itemView.findViewById(R.id.name);
             bumen = itemView.findViewById(R.id.bumen);
             cardView=itemView.findViewById(R.id.root_layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnRvItemClick != null)
                mOnRvItemClick.onItemClick(v, getAdapterPosition());
        }
    }
    /**
     * item点击接口
     */
    public interface OnRvItemClick {
        void onItemClick(View v, int position);
    }
}