package com.interviewtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.interviewtask.R;
import com.interviewtask.models.GetUserRe;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetUserRe> getUserReArrayList;

    public UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(getUserReArrayList.get(position).getAvatarUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivImage);
        holder.tvName.setText(getUserReArrayList.get(position).getLogin());
        holder.tvType.setText(getUserReArrayList.get(position).getType());
        holder.tvUrl.setText(getUserReArrayList.get(position).getHtmlUrl());
    }

    @Override
    public int getItemCount() {
        return getUserReArrayList != null ? getUserReArrayList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        RoundedImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvUrl)
        TextView tvUrl;
        @BindView(R.id.container)
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setList(ArrayList<GetUserRe> getUserReArrayList) {
        this.getUserReArrayList = getUserReArrayList;
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<GetUserRe> newList) {
        int lastIndex = getUserReArrayList.size();
        getUserReArrayList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }

}
