package com.lyhome.ETNews.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyhome.ETNews.MyApplication;
import com.lyhome.ETNews.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.lyhome.ETNews.bean.NewsItem;

import java.util.List;

import static com.lyhome.ETNews.R.id.tv_title;

/**
 * Created by lyhome on 2015/11/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<NewsItem> mNewsList;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerAdapter(Context context, List newsList) {
        mContext = context;
        mNewsList = newsList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void updateData(List<NewsItem> data) {
        this.mNewsList = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  final int position) {
        holder.mTvTitle.setText(mNewsList.get(position).getTitle());
        holder.mTvDate.setText(mNewsList.get(position).getDate());
        ImageLoader.getInstance().displayImage(mNewsList.get(position).getImageurl(), holder.mIvPicture, MyApplication.getInstance().getOptionsWithRoundedCorner());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        ImageView mIvPicture;
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(tv_title);
            mIvPicture = (ImageView) itemView.findViewById(R.id.iv_image);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
