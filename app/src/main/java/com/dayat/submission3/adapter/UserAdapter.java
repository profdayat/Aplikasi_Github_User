package com.dayat.submission3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dayat.submission3.R;
import com.dayat.submission3.model.UserItems;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<UserItems> mData = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    public UserAdapter(ArrayList<UserItems> mData, OnItemClickCallback onItemClickCallback) {
        this.mData = mData;
        this.onItemClickCallback = onItemClickCallback;
    }

    public ArrayList<UserItems> getData() {
        return mData;
    }

    public void setData(ArrayList<UserItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return new UserViewHolder(mView, onItemClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int position) {
        userViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        ImageView imageViewPhoho;

        OnItemClickCallback onItemClickCallback;

        public UserViewHolder(@NonNull View itemView, OnItemClickCallback onItemClickCallback) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textName);
            imageViewPhoho = itemView.findViewById(R.id.img_item_photo);

            this.onItemClickCallback = onItemClickCallback;

            itemView.setOnClickListener(this);
        }

        void bind(UserItems userItems) {
            textViewName.setText(userItems.getLogin());
            Glide.with(itemView.getContext())
                    .load(userItems.getAvatar_url())
                    .apply(new RequestOptions().override(55, 55))
                    .into(imageViewPhoho);
        }

        @Override
        public void onClick(View v) {
            onItemClickCallback.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(int position);
    }
}
