package com.example.atodo.database;

import androidx.lifecycle.LiveData;

import com.example.atodo.database.dao.ImageDao;
import com.example.atodo.database.entities.Image;

import java.util.List;

public class ImageRepository {
    private ImageDao mImageDao;

    public ImageRepository(AppDatabase appDatabase) {
        mImageDao = appDatabase.imageDao();
    }

    public void insert(final Image image){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mImageDao.insert(image);
        });
    }

    public void delete(final Image image){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mImageDao.delete(image);
        });
    }

    public LiveData<List<Image>> getAllImages(int uid){
        return mImageDao.loadAll(uid);
    }
}
