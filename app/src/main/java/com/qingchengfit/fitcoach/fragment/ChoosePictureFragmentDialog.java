package com.qingchengfit.fitcoach.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import cn.qingchengfit.utils.FileUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.io.File;
import java.util.List;
import rx.functions.Action1;

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
 * Created by Paper on 15/12/7 2015.
 */
public class ChoosePictureFragmentDialog extends DialogFragment {

    public static final int CHOOSE_CAMERA = 701;
    public static final int CHOOSE_GALLERY = 702;
    public static final int CLIP = 703;


    public static ChoosePictureFragmentDialog newInstance() {
        Bundle args = new Bundle();
        ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChoosePictureFragmentDialog newInstance(boolean clip) {
        Bundle args = new Bundle();
        args.putBoolean("c", clip);
        ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChoosePictureFragmentDialog newInstance(boolean clip,int request) {
        Bundle args = new Bundle();
        args.putBoolean("c", clip);
        args.putInt("r",request);
        ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }



    public ChoosePicResult getResult() {
        return mResult;
    }

    public void setResult(ChoosePicResult mResult) {
        this.mResult = mResult;
    }

    private ChoosePicResult mResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(STYLE_NO_TITLE, R.style.ChoosePicDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = this.getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);

        View view = inflater.inflate(R.layout.dialog_pic_choose, container, false);
        if (savedInstanceState != null) {
            mResult = (ChoosePicResult) savedInstanceState.getSerializable("callback");
        }


        TextView choosepicCameraTextView = (TextView) view.findViewById(R.id.choosepic_camera);
        TextView choosepicGalleyTextView = (TextView) view.findViewById(R.id.choosepic_galley);
        choosepicCameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions.getInstance(getContext())
                        .request(Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    Intent intent = new Intent();
                                    // 指定开启系统相机的Action
                                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri uri;
                                    if (Build.VERSION.SDK_INT >= 24){
                                        uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", FileUtils.getTmpImageFile(getContext()));
                                        getContext().grantUriPermission(getContext().getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                    }else {
                                        uri = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
                                    }

                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                                    startActivityForResult(intent, CHOOSE_CAMERA);
                                } else {
                                    ToastUtils.show("请开启拍照权限");
                                }
                            }
                        });
            }
        });


        RxView.clicks(choosepicGalleyTextView)
                .compose(RxPermissions.getInstance(getActivity()).ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setType("image/*");

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                startActivityForResult(intent, CHOOSE_GALLERY);
                            } else {
                                startActivityForResult(intent, CHOOSE_GALLERY);
                            }
                        } else {
                            RxPermissions.getInstance(getActivity())
                                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {
                                            if (aBoolean) {
                                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("image/jpeg");

                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                                    startActivityForResult(intent, CHOOSE_GALLERY);
                                                } else {
                                                    startActivityForResult(intent, CHOOSE_GALLERY);
                                                }
                                            } else {
                                                ToastUtils.show("您拒绝了读取图片请求");
                                            }
                                        }
                                    });
                        }

                    }
                });

//        choosepicGalleyTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof ChoosePicResult){
//            setResult((ChoosePicResult) context);
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        setResult(null);
//    }

    public interface ChoosePicResult {
        void onChoosePicResult(boolean isSuccess, String filePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filepath = "";
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CHOOSE_CAMERA || requestCode == CHOOSE_GALLERY) {
                if (requestCode == CHOOSE_GALLERY)
                    filepath = FileUtils.getPath(getActivity(), data.getData());
                else
                    filepath = FileUtils.getTmpImageFile(getContext()).getAbsolutePath();
                if (getArguments() != null && getArguments().getBoolean("c") && !TextUtils.isEmpty(filepath)) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= 24){
                        uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", new File(filepath));
                        getContext().grantUriPermission(getContext().getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    }else {
                        uri = Uri.fromFile(new File(filepath));
                    }

                    clipPhoto(uri);
                } else {
                    if (mResult != null) {
                        mResult.onChoosePicResult(true, filepath);
                    }
                    RxBus.getBus().post(new EventChooseImage(filepath,getArguments() == null ?0:getArguments().getInt("r",0)));
                    dismissAllowingStateLoss();
                }
            } else if (requestCode == CLIP) {
                filepath = FileUtils.getTmpImageFile(getContext()).getAbsolutePath();
                RxBus.getBus().post(new EventChooseImage(filepath,getArguments().getInt("r",0)));
                dismissAllowingStateLoss();
            }
        } else {
            if (mResult != null) {
                mResult.onChoosePicResult(false, filepath);
            }
            dismissAllowingStateLoss();
//            RxBus.getBus().post(new EventChooseImage(""));
        }

    }

    /* 裁剪图片方法实现
    * @param uri
    */
    public void clipPhoto(Uri uri) {
        boolean isReturnData = false;
        String manufacturer = android.os.Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            if (manufacturer.toLowerCase().contains("lenovo")) {//对于联想的手机返回数据
                isReturnData = true;
            }
        }



        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);



        Uri uriout;
        if (Build.VERSION.SDK_INT >= 24){
            uriout = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", FileUtils.getTmpImageFile(getActivity()));
            //uriout = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", new File(getContext().getCacheDir().getAbsolutePath() + File.separator + "tmp_img2"));
            //uriout = getImageContentUri(getActivity(), FileUtils.getTmpImageFile(getActivity()));
            //getActivity().grantUriPermission(getActivity().getPackageName(), uriout, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //getActivity().revokeUriPermission(uriout, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, uriout, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }else {
            uriout = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", isReturnData);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        //Uri uriout = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriout);
        startActivityForResult(intent, CLIP);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        try {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID },
                    MediaStore.Images.Media.DATA + "=? ", new String[] { filePath }, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        }catch (Exception e){
            return null;
        }
    }
}
