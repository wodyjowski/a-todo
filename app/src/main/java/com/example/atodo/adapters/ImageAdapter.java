package com.example.atodo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.atodo.EditActivityVM;
import com.example.atodo.R;
import com.example.atodo.database.entities.Image;
import com.example.atodo.database.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    @NonNull
    private final EditActivityVM editActivityVM;
    private List<Image> imageList;

    public ImageAdapter(Context context, @NonNull EditActivityVM editActivityVM, List<Image> imageList) {
        this.context = context;
        this.editActivityVM = editActivityVM;
        this.imageList = imageList;

        if (this.imageList == null) {
            this.imageList = new ArrayList<Image>();
        }

    }

    public void setTaskList(List<Image> imgList) {
        this.imageList = imgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageList.get(position).uid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position > imageList.size()) {
            return null;
        }


        convertView = LayoutInflater.from(context).inflate(R.layout.imagelist_row, parent, false);


        Image img = imageList.get(position);
        ImageView imgView = convertView.findViewById(R.id.imageViewRow);
        Bitmap bmp = BitmapFactory.decodeByteArray(img.data, 0, img.data.length);
        imgView.setImageBitmap(bmp);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editActivityVM.deleteImage(img);
            }
        });

        return convertView;
    }
}
