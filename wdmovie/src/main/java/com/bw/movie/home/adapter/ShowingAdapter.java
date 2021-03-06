package com.bw.movie.home.adapter;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.home.bean.HotBean;
import com.bw.movie.home.bean.ShowingBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  @author Tang
 *  @time 2019/1/28  9:08
 *  @describe 首页电影多条目正在热映电影的适配器
 */
public class ShowingAdapter extends RecyclerView.Adapter<ShowingAdapter.ViewHoder> {

    private Context mContext;
    private List<ShowingBean.ResultBean> mList;
    public ShowingAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<ShowingBean.ResultBean> list) {
        if(list!=null){
            mList.addAll(list);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_recycleview, viewGroup, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, final int i) {
        viewHoder.title.setText(mList.get(i).getName());
        Uri uri = Uri.parse(mList.get(i).getImageUrl());
        viewHoder.image.setImageURI(uri);
        viewHoder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.skip(mList.get(i).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    ShowingCallBack callBack;
    public interface ShowingCallBack{
        void skip(int id);
    }
    public void setCallBack(ShowingCallBack back){
        callBack=back;
    }
}
