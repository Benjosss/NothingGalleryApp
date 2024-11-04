package fr.benjosss.nothinggalleryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ImageGallery {

    public static List<String> listOfMedia(Context context) {
        List<String> mediaFiles = new ArrayList<>();

        // Récupérer les images
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projectionImages = {MediaStore.Images.Media.DATA};
        Cursor cursorImages = context.getContentResolver().query(imagesUri, projectionImages, null, null, null);

        if (cursorImages != null) {
            while (cursorImages.moveToNext()) {
                mediaFiles.add(cursorImages.getString(cursorImages.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
            }
            cursorImages.close();
        }

        // Récupérer les vidéos
        Uri videosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projectionVideos = {MediaStore.Video.Media.DATA};
        Cursor cursorVideos = context.getContentResolver().query(videosUri, projectionVideos, null, null, null);

        if (cursorVideos != null) {
            while (cursorVideos.moveToNext()) {
                mediaFiles.add(cursorVideos.getString(cursorVideos.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
            }
            cursorVideos.close();
        }

        return mediaFiles;
    }
}
