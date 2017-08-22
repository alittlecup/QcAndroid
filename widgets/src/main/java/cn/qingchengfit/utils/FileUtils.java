package cn.qingchengfit.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/7/31 2015.
 */
public class FileUtils {
    public static File getTmpImageFile(Context context) {
        File file;
        if (context.getExternalCacheDir() != null) {
            String path = context.getExternalCacheDir().getAbsolutePath();

            file = new File(path + File.separator + "tmp_img.png");

        } else {
            file = new File(context.getCacheDir().getAbsolutePath() + File.separator + "tmp_img.png");
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
        }
        return file;
    }

    public static File getTmpImageFileName(Context context, String name) {
        File file;
        if(context.getExternalCacheDir() != null) {
            String path = context.getExternalCacheDir().getAbsolutePath();
            file = new File(path + File.separator + name + ".png");
        } else {
            file = new File(context.getCacheDir().getAbsolutePath() + File.separator + name + ".png");
        }

        if(!file.exists()) {
            try {
                file.createNewFile();
                file.deleteOnExit();
            } catch (IOException var4) {
                ;
            }
        }

        return file;
    }
    public static File getTmpImageFile(Context context,String plus) {
        File file;
        if (context.getExternalCacheDir() != null) {
            //String path = context.getExternalCacheDir().getAbsolutePath();
            String path = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();

            file = new File(path + File.separator + "tmp_img"+plus+".png");
        } else {
            //file = new File(context.getCacheDir().getAbsolutePath() + File.separator + "tmp_img"+plus+".png");
            file = new File(context.getDir(Environment.DIRECTORY_DOWNLOADS,Context.MODE_APPEND).getAbsolutePath() + File.separator + "tmp_img"+plus+".png");
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
        }
        return file;
    }
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) public static String getFilePath(Context context, Uri uri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String docId;
            String[] split;
            String type;
            if (isExternalStorageDocument(uri)) {
                docId = DocumentsContract.getDocumentId(uri);
                split = docId.split(":");
                type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Uri split1 =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId).longValue());
                    return getDataColumn(context, split1, (String) null, (String[]) null);
                }

                if (isMediaDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[] { split[1] };
                    return getDataColumn(context, contentUri, "_id=?", selectionArgs);
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, (String) null, (String[]) null);
            }

            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    //    public static void saveInput2File(InputStream input,){
    //        try {
    //            File file = new File(getCacheDir(), "cacheFileAppeal.srl");
    //            OutputStream output = new FileOutputStream(file);
    //            try {
    //                try {
    //                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
    //                    int read;
    //
    //                    while ((read = input.read(buffer)) != -1) {
    //                        output.write(buffer, 0, read);
    //                    }
    //                    output.flush();
    //                } finally {
    //                    output.close();
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace(); // handle exception, define IOException and others
    //            }
    //        } finally {
    //            input.close();
    //        }
    //    }

    public static void saveCache(String key, String value) {
        if (TextUtils.isEmpty(value))
            return;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + key;


        File f1 = new File(path);

        //        if (f1.exists()) {
        try {
            if (!f1.exists())
                f1.createNewFile();
            FileOutputStream is = new FileOutputStream(f1);

            is.write(value.getBytes());
            is.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        //    }
    }

    public static String readCache(String key) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + key;
        File f1 = new File(path);
        String ret = null;
        if (f1.exists()) {
            try {
                FileInputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                ret = bf.readLine();

                bf.close();
                input.close();
                is.close();


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return ret;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                    split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * 读取本地文件中JSON字符串
     *
     * @param fileName
     * @return
     */
    public static String getJsonFromAssert(String fileName, Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                context.getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
            column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
