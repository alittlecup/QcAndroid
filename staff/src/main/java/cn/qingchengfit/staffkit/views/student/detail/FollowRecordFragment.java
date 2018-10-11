package cn.qingchengfit.staffkit.views.student.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;

import cn.qingchengfit.model.responese.FollowRecord;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.LoadingEvent;
import cn.qingchengfit.staffkit.service.UpyunService;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.ChatAdatper;
import cn.qingchengfit.staffkit.views.custom.ChatInputView;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.commonsware.cwac.cam2.CameraActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/19 2016.
 */
public class FollowRecordFragment extends BaseFragment implements FollowRecordView, TitleFragment {

	TextView cardnum;
	RecycleViewWithNoImg recycleview;

    List<FollowRecord> datas = new ArrayList<>();

	ChatInputView chatInput;
	View disableInput;
    @Inject StudentWrap studentBean;
    @Inject FollowRecordPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject IPermissionModel serPermisAction;

    private ChatAdatper adapter;
    private Observable<UpyunService.UpYunResult> obUpyun;
    private LinearLayoutManager manager;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_record, container, false);
      cardnum = (TextView) view.findViewById(R.id.cardnum);
      recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recycleview);
      chatInput = (ChatInputView) view.findViewById(R.id.chat_input);
      disableInput = (View) view.findViewById(R.id.disable_input);
      view.findViewById(R.id.disable_input).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickDisable();
        }
      });

      delegatePresenter(presenter, this);
        adapter = new ChatAdatper(datas);
        manager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(manager);
        recycleview.setAdapter(adapter);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData();
            }
        });
        adapter.setOnTouchListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                chatInput.close();
                AppUtils.hideKeyboard(getActivity());
            }
        });
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (!TextUtils.isEmpty(datas.get(pos).thumbnail)) {
                    //                    SimpleImgDialog.newInstance(datas.get(pos).thumbnail).show(getFragmentManager(), "");
                    SimpleImgDialog fragment = SimpleImgDialog.newInstance(datas.get(pos).thumbnail);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //                        fragment.setEnterTransition(new ChangeBounds());
                        ChangeBounds changeBoundsTransition = new ChangeBounds();
                        changeBoundsTransition.setDuration(1000l);
                        fragment.setSharedElementEnterTransition(changeBoundsTransition);
                        fragment.setAllowEnterTransitionOverlap(true);
                    }
                    //                    getParentFragment().getFragmentManager().beginTransaction()
                    //                            .addSharedElement(view.findViewById(R.id.chat_img), "shareimg")
                    //                            .add(mCallbackActivity.getFragId(), fragment)
                    //                            .addToBackStack(null)
                    //                            .commit();
                    fragment.show(getFragmentManager(), "");
                }
            }
        });
        presenter.queryData();
        chatInput.setSendCallback(new ChatInputView.OnSendCallback() {
            @Override public void onSendMsg(String s) {
                presenter.addFollow(s, null);
            }

            @Override public void onPicture() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent, ChoosePictureFragmentDialog.CHOOSE_GALLERY);
                } else {
                    startActivityForResult(intent, ChoosePictureFragmentDialog.CHOOSE_GALLERY);
                }
            }

            @Override public void onCamara() {
                new RxPermissions(getActivity()).request(Manifest.permission.CAMERA).subscribe(new Subscriber<Boolean>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Uri uri = Uri.fromFile(FileUtils.getTmpImageFile(getContext()));
                            startActivityForResult(
                                new CameraActivity.IntentBuilder(getContext()).confirmationQuality(
                                    0.7f).to(uri).build(),
                                ChoosePictureFragmentDialog.CHOOSE_CAMERA);
                        } else {
                            ToastUtils.show("请先开启拍照权限");
                        }
                    }
                });
            }
        });
        obUpyun = RxBus.getBus().register(UpyunService.UpYunResult.class);
        obUpyun.subscribe(new Action1<UpyunService.UpYunResult>() {
            @Override public void call(UpyunService.UpYunResult upYunResult) {
                if (TextUtils.isEmpty(upYunResult.getUrl())) {
                    RxBus.getBus().post(new LoadingEvent(false));
                } else {
                    presenter.addFollow(null, upYunResult.getUrl());
                }
            }
        });

        if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
            disableInput.setVisibility(View.VISIBLE);
        } else {
            disableInput.setVisibility(View.GONE);
        }

        return view;
    }

 public void onClickDisable() {
        showAlert(getString(R.string.alert_permission_forbid));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filepath = "";
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePictureFragmentDialog.CHOOSE_GALLERY) {
                filepath = FileUtils.getPath(getActivity(), data.getData());
            } else if (requestCode == ChoosePictureFragmentDialog.CHOOSE_CAMERA) {
                filepath = FileUtils.getTmpImageFile(getContext()).getAbsolutePath();
            }
            showLoading();
            UpYunClient.rxUpLoad("/android/", filepath)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        presenter.addFollow(null, s);
                    }
                }, new NetWorkThrowable());


        }
    }

    @Override public void onData(List<FollowRecord> records, int page) {
        hideLoading();
        if (page == 1) datas.clear();
        for (int i = 0; i < records.size(); i++) {
            datas.add(0, records.get(i));
        }
        //        datas.addAll(records);
        adapter.notifyDataSetChanged();
        recycleview.setFresh(false);
        recycleview.setNoData(datas.size() == 0);
        if (page == 1) {
            manager.scrollToPosition(datas.size() - 1);
        }
        //            recycleview.scrollToPosition(datas.size());
        AppUtils.hideKeyboard(getActivity());
        RxBus.getBus().post(new LoadingEvent(false));
        chatInput.close();
    }

    @Override public void onAdd() {
        hideLoading();
        presenter.initPage();
        presenter.queryData();
    }

    @Override public String getTitle() {
        return "跟进记录";
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(UpyunService.UpYunResult.class.getName(), obUpyun);
        presenter.unattachView();
        super.onDestroyView();
    }

    //    @OnClick(R.id.add_follow)
    //    public void onClick() {
    //        presenter.addFollow();
    //    }

    @Override public String getFragmentName() {
        return FollowRecordFragment.class.getName();
    }
}
