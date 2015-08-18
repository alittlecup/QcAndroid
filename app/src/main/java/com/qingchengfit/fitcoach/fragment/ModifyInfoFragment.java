package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.User;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyInfoFragment extends Fragment {
    public static final String TAG = ModifyInfoFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int SELECT_PIC_KITKAT = 10;
    public static int SELECT_PIC = 11;
    public static int SELECT_CAM = 12;
    private static String FILE_PATH = Configs.ExternalPath + "header.png";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.modifyinfo_header_pic)
    SimpleDraweeView modifyinfoHeaderPic;
    @Bind(R.id.comple_gender)
    RadioGroup compleGender;
    Gson gson = new Gson();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private User mModifyUser;

    public ModifyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyInfoFragment newInstance(String param1, String param2) {
        ModifyInfoFragment fragment = new ModifyInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        user = gson.fromJson(PreferenceUtils.getPrefString(getActivity(), "user_info", ""), User.class);
//        QcCloudClient.getApi()
//                .getApi.qcGetUserInfo(user.id)
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(qcResponUserInfo-> {
//                    LogUtil.e("Haahahahaha");
//                    user = qcResponUserInfo.data.user;
//                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_info, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setTitle("修改资料");
        toolbar.setNavigationOnClickListener(view1 ->
                        getActivity().onBackPressed()
        );
        modifyinfoHeaderPic.setImageURI(Uri.parse(user.avatar));

        return view;
    }

    @OnClick(R.id.modifyinfo_header_pic)
    public void onChangeHeader() {
        MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("请选择拍照或相册")
                .setPositiveButton("拍照", view -> {
                    mMaterialDialog.dismiss();
                    goCamera();
                })
                .setNegativeButton("相册", view -> {
                    mMaterialDialog.dismiss();
                    goGalley();
                });
        mMaterialDialog.show();

    }

    public void goGalley() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent, SELECT_PIC);
        }
    }

    public void goCamera() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//         根据文件地址创建文件
        File file = new File(FILE_PATH);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                //e.printStackTrace();
                RevenUtils.sendException("goCamera", TAG, e);
            }
//         把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
//         设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, SELECT_CAM);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == SELECT_PIC){
//
//            }else if (requestCode == SELECT_PIC_KITKAT){
//
//            }
        String filepath = "";
        if (resultCode == -1) {
            if (requestCode == SELECT_PIC || requestCode == SELECT_PIC_KITKAT)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            else filepath = FILE_PATH;
            LogUtil.d(filepath);
            Observable.just(filepath)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        File upFile = new File(s);
                        boolean reslut = UpYunClient.upLoadImg(user.id, upFile);
                        if (reslut) {
                            LogUtil.d("success");
                            Observable.just(upFile)
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(file -> {
                                        modifyinfoHeaderPic.setImageURI(Uri.fromFile(file));
                                        mModifyUser.avatar = UpYunClient.UPYUNPATH + "header/" + user.id + ".png";
                                    });

                        } else {
                            //upload failed TODO
                        }
                    });

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
