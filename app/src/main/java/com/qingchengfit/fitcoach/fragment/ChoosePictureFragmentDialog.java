package com.qingchengfit.fitcoach.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import cn.qingchengfit.widgets.utils.FileUtils;
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
                                    Uri uri = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
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
                            intent.setType("image/jpeg");

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
                    clipPhoto(Uri.fromFile(new File(filepath)));
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
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
        Uri uriout = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriout);
        startActivityForResult(intent, CLIP);
    }
}
