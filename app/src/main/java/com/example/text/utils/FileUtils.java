package com.example.text.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        try {
            @SuppressLint("Recycle") InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File file = new File(context.getCacheDir(), "temp_avatar.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while (true) {
                assert inputStream != null;
                if ((read = inputStream.read(buffer)) == -1) break;
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
