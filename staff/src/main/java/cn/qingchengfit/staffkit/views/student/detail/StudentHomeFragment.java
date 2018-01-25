package cn.qingchengfit.staffkit.views.student.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardParams;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EditStudentEvent;
import cn.qingchengfit.staffkit.rxbus.event.StudentBaseInfoEvent;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
public class StudentHomeFragment extends BaseFragment {

    public int statusToTab = -1;
    @BindView(R.id.header) ImageView header;
    @BindView(R.id.gender) ImageView gender;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.tab) TabLayout tab;
    @BindView(R.id.student) ViewPager student;
    @BindView(R.id.buycard) Button buycard;
    @BindView(R.id.order_group) Button orderGroup;
    @BindView(R.id.order_private) Button orderPrivate;
    @BindView(R.id.orderbtn_layout) LinearLayout orderbtnLayout;
    @BindView(R.id.student_status) TextView studentStatus;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.tv_student_call) ImageView tvStudentCall;
    @BindView(R.id.tv_student_msg) ImageView tvStudentMsg;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    @Inject StudentWrapper studentBean;
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    @Inject StudentAction studentAction;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentAdapter mAdapter;
    private Observable<StudentBaseInfoEvent> ObInfo;
    private QcStudentBean mQcStudentBean;

    @Inject public StudentHomeFragment() {
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        if (!isLoading) {

            mAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        }

        student.setAdapter(mAdapter);
        student.setOffscreenPageLimit(3);
        tab.setupWithViewPager(student);
        student.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        student.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    orderbtnLayout.setVisibility(View.VISIBLE);
                    buycard.setVisibility(View.GONE);
                } else if (position == 1) {
                    orderbtnLayout.setVisibility(View.GONE);
                    buycard.setVisibility(View.VISIBLE);
                } else {
                    orderbtnLayout.setVisibility(View.GONE);
                    buycard.setVisibility(View.GONE);
                }
            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        ObInfo = RxBus.getBus().register(StudentBaseInfoEvent.class);
        Drawable drawableCall = ContextCompat.getDrawable(getContext(), R.drawable.vector_student_phone);
        drawableCall.setBounds(0, 0, drawableCall.getMinimumWidth(), drawableCall.getMinimumHeight());
        Drawable drawableMsg = ContextCompat.getDrawable(getContext(), R.drawable.vector_student_message);
        drawableMsg.setBounds(0, 0, drawableMsg.getMinimumWidth(), drawableMsg.getMinimumHeight());
        ObInfo.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<StudentBaseInfoEvent>() {
            @Override public void call(final StudentBaseInfoEvent studentBaseInfoEvent) {
                if (studentBaseInfoEvent.user_student != null) {
                    Glide.with(getContext())
                        .load(PhotoUtils.getSmall(studentBaseInfoEvent.user_student.getAvatar()))
                        .asBitmap()
                        .into(new CircleImgWrapper(header, getContext()));
                    if (TextUtils.isEmpty(studentBaseInfoEvent.user_student.getAvatar())){
                        header.setImageResource(studentBaseInfoEvent.user_student.getGender() == 0
                            ? R.drawable.default_student_male : R.drawable.default_student_female);
                    }
                    name.setText(studentBaseInfoEvent.user_student.getUsername());
                    Glide.with(getActivity())
                        .load(studentBaseInfoEvent.user_student.getGender() == 0 ? R.drawable.ic_gender_signal_male
                            : R.drawable.ic_gender_signal_female)
                        .into(gender);
                    phone.setText(studentBaseInfoEvent.user_student.getPhone());
                }

                orderPrivate.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        if (serPermisAction.check(PermissionServerUtils.PRIVATE_ORDER_CAN_WRITE)) {
                            WebActivity.startWeb(studentBaseInfoEvent.privateUrl, getContext());
                        } else {
                            showAlert(getString(R.string.alert_permission_forbid));
                        }
                    }
                });
                orderGroup.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (serPermisAction.check(PermissionServerUtils.ORDERS_DAY_CAN_WRITE)) {
                            WebActivity.startWeb(studentBaseInfoEvent.groupUrl, getContext());
                        } else {
                            showAlert(getString(R.string.alert_permission_forbid));
                        }
                    }
                });
                if (gymWrapper.inBrand()) {
                    studentStatus.setVisibility(View.GONE);
                } else {
                    studentStatus.setVisibility(View.VISIBLE);
                    StringUtils.studentStatus(studentStatus, studentBaseInfoEvent.status);
                }
            }
        });

        RxRegiste(studentAction
            .getStudentById(studentBean.id()).onBackpressureBuffer().subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(qcStudentBean -> {
                Glide.with(getActivity())
                    .load(PhotoUtils.getSmall(qcStudentBean.getAvatar()))
                    .asBitmap()
                    .into(new CircleImgWrapper(header, getContext()));
                name.setText(qcStudentBean.getUsername());
                Glide.with(getActivity())
                    .load(qcStudentBean.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female)
                    .into(gender);
                phone.setText(qcStudentBean.getPhone());
                mQcStudentBean = qcStudentBean;
            }, throwable -> {
            }));

        switch (statusToTab) {
            case 0:
                student.setCurrentItem(3);
                break;
            case 1:
                student.setCurrentItem(2);
                break;
            case 2:
                student.setCurrentItem(1);
                break;
        }
        isLoading = true;

        return view;
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();
        fragments.add(new ClassRecordFragment());
        fragments.add(new StudentsCardsFragment());
        fragments.add(new FollowRecordFragment());
        fragments.add(new StudentMoreInfoFragment());
        mAdapter.notifyDataSetChanged();
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("会员详情");
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                BottomSheetListDialogFragment.start(StudentHomeFragment.this, 201, new String[] { "删除学员" });
                return true;
            }
        });
    }

    @OnClick(R.id.buycard) public void onClick() {

        if (gymWrapper.inBrand()) {
            MutiChooseGymFragment.start(this, true, null, 9);
        } else {
            if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                showAlert("您没有该场馆购卡权限");
                return;
            }
            routeTo("card","/choose/cardtpl/",new ChooseCardTplForBuyCardParams().qcStudentBean(mQcStudentBean).build());
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 201) {
                if (gymWrapper.inBrand()) {
                    final List<String> supportId = new ArrayList<>();

                    List<String> sa = serPermisAction.checkMutiTrue(PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE,
                        studentBean.getStudentBean().getSupportIdList());

                    supportId.addAll(sa);
                    if (supportId.size() == 1) {
                        //删除学员
                        DialogUtils.instanceDelDialog(getContext(), "是否删除该学员", new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                RxBus.getBus()
                                    .post(new EditStudentEvent.Builder().type(EditStudentEvent.DEL).shop_ids(supportId.get(0)).build());
                            }
                        }).show();
                    } else if (supportId.size() > 1) {
                        ArrayList<String> list = new ArrayList<>();
                        list.addAll(studentBean.getStudentBean().getSupportIdList());
                        MutiChooseGymFragment.start(StudentHomeFragment.this, true, list, PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE,
                            11);
                    } else {
                        showAlert(getString(R.string.alert_permission_forbid));
                    }
                } else {
                    if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE)) {
                        //删除学员
                        DialogUtils.instanceDelDialog(getContext(), "是否删除该学员", new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                RxBus.getBus().post(new EditStudentEvent.Builder().type(EditStudentEvent.DEL).build());
                            }
                        }).show();
                    } else {
                        showAlert(getString(R.string.alert_permission_forbid));
                    }
                }
            } else if (requestCode == 10) {//购卡
                //buyCard(getContext(), (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE), this);
            } else if (requestCode == 9) {//选择场馆

                Shop shop = (Shop) IntentUtils.getParcelable(data);
                if (!serPermisAction.check(shop.id, PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                    showAlert("您没有该场馆购卡权限");
                    return;
                }
                CoachService mChooseShop = gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shop.id);
                if (mChooseShop != null) {
                    //Intent toCardType = new Intent(getActivity(), ChooseCardTypeActivity.class);
                    //gymWrapper.setCoachService(mChooseShop);
                    //startActivityForResult(toCardType, 10);
                    routeTo("card","/choose/cardtpl/",null);
                }
            } else if (requestCode == 11) {//删除学员所属场馆
                final Shop shop = (Shop) IntentUtils.getParcelable(data);

                if (shop != null) {
                    if (!serPermisAction.check(shop.id, PermissionServerUtils.MANAGE_STAFF_CAN_DELETE)) {
                        showAlert(getString(R.string.alert_permission_forbid));
                        return;
                    }
                    DialogUtils.instanceDelDialog(getContext(), "是否在所选场馆删除该学员", new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RxBus.getBus().post(new EditStudentEvent.Builder().type(EditStudentEvent.DEL).shop_ids(shop.id).build());
                        }
                    }).show();
                } else {
                    showAlert(R.string.alert_at_least_one_gym);
                }
            }
        }
    }



    @Override public void onDestroyView() {
        RxBus.getBus().unregister(StudentBaseInfoEvent.class.getName(), ObInfo);
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return StudentHomeFragment.class.getName();
    }

    @OnClick({ R.id.ll_student_call, R.id.ll_student_msg }) public void onClick(View view) {
        if (mQcStudentBean == null)
            return;
        switch (view.getId()) {
            case R.id.ll_student_call:
                new MaterialDialog.Builder(getContext()).autoDismiss(true)

                    .content(new StringBuilder().append("确定呼叫号码\n").append(mQcStudentBean.getPhone()).append("吗？").toString())
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new RxPermissions(getActivity()).request(Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
                                @Override public void call(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Uri uri =
                                            Uri.parse(new StringBuilder().append("tel:").append(mQcStudentBean.getPhone()).toString());
                                        Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
                                      dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(dialntent);
                                    } else {
                                        ToastUtils.show("请在应用设置中开启拨打电话权限");
                                    }
                                }
                            });
                        }
                    })
                    .show();
                break;
            case R.id.ll_student_msg:
                new RxPermissions(getActivity()).request(Manifest.permission.SEND_SMS).subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            AppUtils.doSendSMSTo(getContext(), mQcStudentBean.getPhone());
                        } else {
                            ToastUtils.show("请在应用设置中开启发短信权限");
                        }
                    }
                });

                break;
        }
    }
}
