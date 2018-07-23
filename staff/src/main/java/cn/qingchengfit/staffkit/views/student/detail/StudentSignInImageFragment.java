package cn.qingchengfit.staffkit.views.student.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseImage;

import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.UpdateEvent;
import cn.qingchengfit.model.responese.SignInImg;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/24.
 */
public class StudentSignInImageFragment extends BaseFragment implements StudentSignInImagePresenter.MVPView {
    public static final String TAG = StudentSignInImageFragment.class.getSimpleName();
	RecyclerView fixRecordRv;
	ImageView curImg;
	TextView changeImg;
	LinearLayout noImgLayout;
	TextView addSigninHint;
	Button upImg;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentSignInImagePresenter presenter;
    @Inject StudentWrap studentBean;
	Toolbar toolbar;
	TextView toolbarTitile;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private CommonFlexAdapter mAdapter;

    public static StudentSignInImageFragment newInstance() {
        Bundle args = new Bundle();
        StudentSignInImageFragment fragment = new StudentSignInImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_img, container, false);
      fixRecordRv = (RecyclerView) view.findViewById(R.id.fix_record_rv);
      curImg = (ImageView) view.findViewById(R.id.cur_img);
      changeImg = (TextView) view.findViewById(R.id.change_img);
      noImgLayout = (LinearLayout) view.findViewById(R.id.no_img_layout);
      addSigninHint = (TextView) view.findViewById(R.id.add_signin_hint);
      upImg = (Button) view.findViewById(R.id.up_img);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.change_img).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          changeImg();
        }
      });
      view.findViewById(R.id.up_img).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          changeImg();
        }
      });
      view.findViewById(R.id.cur_img).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickImg();
        }
      });

      initToolbar(toolbar);
        delegatePresenter(presenter, this);
        fixRecordRv.setHasFixedSize(true);
        fixRecordRv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        fixRecordRv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        presenter.querySignInList(App.staffId);
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                if (TextUtils.isEmpty(eventChooseImage.filePath)) {
                  LogUtil.e("filePaht == null");
                }
                showLoading();
                Glide.with(getContext()).load(new File(eventChooseImage.filePath)).placeholder(R.drawable.img_loadingimage).into(curImg);
                RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .delay(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            Glide.with(getContext()).load(PhotoUtils.getSmall(s)).placeholder(R.drawable.img_loadingimage).into(curImg);
                            presenter.changeImage(App.staffId, s);
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            Glide.with(getContext()).load(R.drawable.img_loadingimage).into(curImg);
                            ToastUtils.show("图片上传失败，请重试");
                            hideLoading();
                        }
                    }));
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                Glide.with(getContext()).load(R.drawable.img_loadingimage).into(curImg);
                ToastUtils.show("图片上传失败，请重试");
                hideLoading();
            }
        });

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.signin_img);
    }

    @Override public String getFragmentName() {
        return TAG;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void changeImg() {
        if (gymWrapper.inBrand()) {
            //            showAlert("请到场馆中修改会员照片");
            return;
        }
        ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true);
        dialog.show(getFragmentManager(), "");
    }

 public void onClickImg() {
        if (mData.size() > 0 && mData.get(0) instanceof SignInImageItem) {
            SimpleImgDialog.newInstance(((SignInImageItem) mData.get(0)).getSignInImg().photo).show(getFragmentManager(), "");
        }
    }

    @Override public void showImageList(List<SignInImg> data) {
        if (data == null || data.size() == 0) {
            changeImg.setText("新增");
            if (gymWrapper.inBrand()) {
                upImg.setVisibility(View.GONE);
                addSigninHint.setText("通过会员照片工作人员可以在签到、预约的时候辨别会员真实身份，请在场馆内添加");
            }
            noImgLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            noImgLayout.setVisibility(View.GONE);
            changeImg.setText(gymWrapper.inBrand() ? "请在场馆内添加会员照片" : "修改");
            changeImg.setTextColor(CompatUtils.getColor(getContext(), gymWrapper.inBrand() ? R.color.text_grey : R.color.colorPrimary));
        }
        SignInImg signInImg = data.get(0);
        Glide.with(this).load(PhotoUtils.getSmall(signInImg.photo)).into(curImg);
        RxBus.getBus().post(new UpdateEvent());
        mData.clear();
        for (int i = 0; i < data.size(); i++) {
            mData.add(new SignInImageItem(data.get(i)));
        }
        mAdapter = new CommonFlexAdapter(mData);
        fixRecordRv.setAdapter(mAdapter);
    }

    @Override public void onSuccess() {
        hideLoading();
        presenter.querySignInList(App.staffId);
    }

    @Override public void onShowError(String e) {
        hideLoading();
        showAlert(e);
    }

    @Override public void onShowError(@StringRes int e) {
        hideLoading();
        showAlert(e);
    }
}
