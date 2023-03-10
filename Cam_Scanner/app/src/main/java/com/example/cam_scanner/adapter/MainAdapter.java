package com.example.cam_scanner.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cam_scanner.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<File> items = new ArrayList<>();
    private Context context;
    private int current_select_index = -1;
    private OnItemClickListener onItemClickListener;
    private SparseBooleanArray selected_items;

    public MainAdapter(Context context, List<File> items) {
        this.context = context;
        this.items = items;
        selected_items = new SparseBooleanArray();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, File obj, int position);
        void onLongItemClick(View view, File obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class FileItemHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView timestamp;
        public TextView size;
        public View lyt_parent;

        public FileItemHolder(View v) {
            super(v);
            image = v.findViewById(R.id.fileImageView);
            name = v.findViewById(R.id.nameItemTextview);
            timestamp = v.findViewById(R.id.dateTimeItemTextView);
            size = v.findViewById(R.id.sizeItemTextView);
            lyt_parent = v.findViewById(R.id.listItemLinearLayout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        vh = new FileItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        File obj = items.get(position);
        if(holder instanceof FileItemHolder) {
            FileItemHolder fileView = (FileItemHolder) holder;
            fileView.name.setText(obj.getName());
            Date lastModDate = new Date(obj.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            String dateTime = formatter.format(lastModDate);
            fileView.timestamp.setText(dateTime);
            fileView.size.setText(getSize(obj.length()));

            fileView.lyt_parent.setOnClickListener((v) -> {

            });

            fileView.lyt_parent.setOnLongClickListener((v) -> {
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>();
        for(int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public String getSize(long size) {
        String[] sizeUnit = {"bytes", "KB", "MB", "GB", "TB", "PB"};
        double lastSize = size;
        int index = 0;
        DecimalFormat sizeFormatter = new DecimalFormat("0.00");

        while((size >= 1024 && index < sizeUnit.length) || (size > 103 && index == 0)) {
            lastSize /= 1024;
            index++;
        }
        return sizeFormatter.format(lastSize).concat(" " + sizeUnit[index]);
    }

    public int getSelectedItemsCount() {
        return selected_items.size();
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

    public void deleteSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        items.remove(position);
    }
}
