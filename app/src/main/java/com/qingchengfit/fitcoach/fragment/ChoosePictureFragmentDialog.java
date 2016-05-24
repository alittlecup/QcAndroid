package com.qingchengfit.fitcoach.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = this.getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.horizontalMargin = 0;
//        lp.verticalMargin = 0;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);

        View view = inflater.inflate(R.layout.dialog_pic_choose, container, false);
        TextView choosepicCameraTextView = (TextView) view.findViewById(R.id.choosepic_camera);
        TextView choosepicGalleyTextView = (TextView) view.findViewById(R.id.choosepic_galley);

        choosepicCameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RxPermissions.getInstance(getContext()).isGranted(Manifest.permission.CAMERA)) {
                    Intent intent = new Intent();
                    // 指定开启系统相机的Action
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    Uri uri = Uri.fromFile(new File(Configs.CameraPic));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    startActivityForResult(intent, CHOOSE_CAMERA);
                } else {

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
                                        Uri uri = Uri.fromFile(new File(Configs.CameraPic));
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                                        startActivityForResult(intent, CHOOSE_CAMERA);
                                    } else {
                                        ToastUtils.showDefaultStyle("请打开拍照权限");
                                    }
                                }
                            });
                }


            }
        });
        choosepicGalleyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RxPermissions.getInstance(getContext()).isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        startActivityForResult(intent, CHOOSE_GALLERY);
                    } else {
                        startActivityForResult(intent, CHOOSE_GALLERY);
                    }
                } else {
                    RxPermissions.getInstance(getContext())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if (aBoolean){
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("image/jpeg");

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                            startActivityForResult(intent, CHOOSE_GALLERY);
                                        } else {
                                            startActivityForResult(intent, CHOOSE_GALLERY);
                                        }
                                    }else {
                                        ToastUtils.showDefaultStyle("请开启使用存储权限");
                                    }
                                }
                            });
                }
            }
        });


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
        LogUtil.e("result");
        String filepath = "";
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_GALLERY)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            else filepath = Configs.CameraPic;

            if (mResult != null) {
                LogUtil.e("filepath:" + filepath);
                mResult.onChoosePicResult(true, filepath);
            }
            LogUtil.e("?????");
        } else {
            if (mResult != null) {
                mResult.onChoosePicResult(false, filepath);
            }
        }
        this.dismiss();
    }
}
