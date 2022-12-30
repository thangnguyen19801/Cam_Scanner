package com.example.cam_scanner.adapter;

import android.content.Context;
import android.media.Image;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cam_scanner.R;
import com.example.cam_scanner.modal.ImageDocument;

import java.io.File;
import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ImageDocument> items = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selected_items;
    private OnItemClickListener onItemClickListener;

    public PreviewAdapter(Context context, ArrayList<ImageDocument> items) {
        this.context = context;
        this.items = items;
        selected_items = new SparseBooleanArray();
    }
    public interface OnItemClickListener {
        void onItemClick(View view, ImageDocument obj, int position);
        void onLongItemClick(View view, ImageDocument obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class PreviewImageHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View lyt_parent;

        public PreviewImageHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_image, parent, false);
        vh = new PreviewImageHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PreviewImageHolder) {
            PreviewImageHolder view = (PreviewImageHolder) holder;
            try {
                Glide.with(context)
                        .load(items.get(position).getImageUri())
                        .diskCacheStrategy(DiskCacheStrategy.NONE).thumbnail()
                        .into(view.image);
            } catch (Exception e) {}

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, items.get(position), holder.getAdapterPosition());
                    }
                }
            });

            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemClickListener == null) return false;
                    onItemClickListener.onLongItemClick(v, items.get(position), holder.getAdapterPosition());
                    return true;
                }
            });

            toggleSelection(position);
        }
    }

    public void toggleSelection(int position) {
        if(selected_items.get(position, false)) {
            selected_items.delete(position);
        } else {
            selected_items.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void selectAll() {
        for(int i = 0; i < items.size(); i++) {
            selected_items.put(i, true);
            notifyItemChanged(i);
        }
    }

    public void removeData(int position) {
        items.remove(position);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
