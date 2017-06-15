package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.HidenBean;
import com.qingchengfit.fitcoach.http.bean.QcCertificateDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 */
public class ComfirmDetailFragment extends BaseSettingFragment {
    public static final String TAG = ComfirmDetailFragment.class.getName();
    @BindView(R.id.recorddetail_title) TextView recorddetailTitle;
    @BindView(R.id.recorddetail_host) TextView recorddetailHost;
    @BindView(R.id.recorddetail_time) TextView recorddetailTime;
    @BindView(R.id.recorddetail_score) TextView recorddetailScore;
    @BindView(R.id.recorddetail_comment) TextView recorddetailComment;
    @BindView(R.id.comfirm_img) ImageView comfirmImg;
    @BindView(R.id.comfirm_createtime) TextView comfirmCreatetime;
    @BindView(R.id.record_comfirm_img) ImageView recordComfirmImg;
    @BindView(R.id.workexp_detail_hiden) TextView workexpDetailHiden;
    @BindView(R.id.valid_lable) TextView validLable;
    private int id;
    private DialogSheet dialogSheet;
    private MaterialDialog delDialog;
    private QcCertificatesReponse.DataEntity.CertificatesEntity entity;
    private Unbinder unbinder;

    public ComfirmDetailFragment() {
    }

    public static ComfirmDetailFragment newInstance(QcCertificatesReponse.DataEntity.CertificatesEntity entity) {

        Bundle args = new Bundle();
        args.putParcelable("certificate", entity);
        ComfirmDetailFragment fragment = new ComfirmDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = getArguments().getParcelable("certificate");
            id = entity.getId();
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comfirm_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        initData();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initData() {
        if (!TextUtils.isEmpty(entity.getPhoto())) {
            Glide.with(getContext()).load(PhotoUtils.getSmall(entity.getPhoto())).asBitmap().into(new ScaleWidthWrapper(comfirmImg));
        }

        if (entity.getIs_authenticated()) {//青橙认证
            workexpDetailHiden.setVisibility(View.VISIBLE);
            if (entity.is_hidden()) {
                workexpDetailHiden.setText("该资质认证已隐藏");
                workexpDetailHiden.setBackgroundColor(getResources().getColor(R.color.orange));
                workexpDetailHiden.setTextColor(getResources().getColor(R.color.white));
            } else {
                workexpDetailHiden.setBackgroundColor(getResources().getColor(R.color.backgroud_grey));
                workexpDetailHiden.setText("来自[" + entity.getOrganization().getName() + "]");
                workexpDetailHiden.setTextColor(getResources().getColor(R.color.text_black));
            }
            fragmentCallBack.onToolbarMenu(entity.is_hidden() ? R.menu.menu_unhide : R.menu.menu_hide, R.drawable.ic_arrow_left, "认证详情");
            fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (entity.is_hidden()) {
                        //隐藏
                        QcCloudClient.getApi().postApi.qcHidenCertificates(id, new HidenBean(false))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Action1<QcResponse>() {
                                @Override public void call(QcResponse qcResponse) {
                                    if (qcResponse.status == ResponseResult.SUCCESS) {
                                        freshData();
                                    } else {

                                    }
                                }
                            });
                    } else {
                        QcCloudClient.getApi().postApi.qcHidenCertificates(id, new HidenBean(true))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<QcResponse>() {
                                @Override public void call(QcResponse qcResponse) {
                                    if (qcResponse.status == ResponseResult.SUCCESS) {
                                        freshData();
                                    }
                                }
                            });
                    }

                    return true;
                }
            });
        } else {
            fragmentCallBack.onToolbarMenu(R.menu.menu_flow, R.drawable.ic_arrow_left, "认证详情");
            fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (dialogSheet == null) {
                        dialogSheet = new DialogSheet(getContext());
                        dialogSheet.addButton("编辑", new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                dialogSheet.dismiss();
                                RecordEditFragment fragment =
                                    RecordEditFragment.newInstance(true, new Gson().toJson(entity), entity.getType());
                                fragmentCallBack.onFragmentChange(fragment);
                                //                                                    fragmentCallBack.onFragmentChange(new WorkExpeEditFragment().newInstance("编辑工作经历",experiencesEntity));
                            }
                        }).addButton("删除", new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                dialogSheet.dismiss();
                                showDialog(entity.getId());
                            }
                        });
                    }
                    dialogSheet.show();
                    return true;
                }
            });
        }

        recorddetailTitle.setText(entity.getName()

        );
        recorddetailHost.setText(entity.getOrganization().getName()

        );
        recorddetailScore.setText(entity.getGrade()

        );

        if (TextUtils.isEmpty(entity.getStart()) || TextUtils.isEmpty(entity.getEnd())) {
            recorddetailTime.setText("长期有效");
            validLable.setText("");
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(entity.getStart()

            )));
            sb.append("至");
            Date d = DateUtils.formatDateFromServer(entity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000) {
                recorddetailTime.setText("长期有效");
                validLable.setText("");
            } else

            {
                sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(entity.getEnd())));
                recorddetailTime.setText(sb.toString());
            }
        }

        comfirmCreatetime.setText(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(entity.
            getDate_of_issue()

        )));
        if (entity.is_authenticated()) {
            recordComfirmImg.setVisibility(View.VISIBLE);
        } else {
            recordComfirmImg.setVisibility(View.GONE);
        }
    }

    private void freshData() {
        QcCloudClient.getApi().getApi.qcGetCertificateDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(qcCertificateDetailResponse -> {

            if (recorddetailTitle != null) {
                getActivity().runOnUiThread(() -> {
                    final QcCertificateDetailResponse.DataEntity.CertificateEntity certificateEntity =
                        qcCertificateDetailResponse.getData().getCertificate();
                    if (certificateEntity.getIs_authenticated()) {//青橙认证
                        workexpDetailHiden.setVisibility(View.VISIBLE);
                        if (certificateEntity.is_hidden()) {
                            workexpDetailHiden.setText("该资质认证已隐藏");
                            workexpDetailHiden.setBackgroundColor(getResources().getColor(R.color.orange));
                            workexpDetailHiden.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            workexpDetailHiden.setBackgroundColor(getResources().getColor(R.color.backgroud_grey));
                            workexpDetailHiden.setText("来自[" + certificateEntity.getOrganization().getName() + "]");
                            workexpDetailHiden.setTextColor(getResources().getColor(R.color.text_black));
                        }

                        fragmentCallBack.onToolbarMenu(certificateEntity.is_hidden() ? R.menu.menu_unhide : R.menu.menu_hide,
                            R.drawable.ic_arrow_left, "认证详情");
                        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override public boolean onMenuItemClick(MenuItem item) {
                                if (certificateEntity.is_hidden()) {
                                    //隐藏
                                    QcCloudClient.getApi().postApi.qcHidenCertificates(id, new HidenBean(false))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Action1<QcResponse>() {
                                            @Override public void call(QcResponse qcResponse) {
                                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                                    initData();
                                                }
                                            }
                                        });
                                } else {
                                    QcCloudClient.getApi().postApi.qcHidenCertificates(id, new HidenBean(true))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<QcResponse>() {
                                            @Override public void call(QcResponse qcResponse) {
                                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                                    initData();
                                                }
                                            }
                                        });
                                    }

                                return true;
                            }
                        });
                    } else {
                        fragmentCallBack.onToolbarMenu(R.menu.menu_flow, R.drawable.ic_arrow_left, "认证详情");
                        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override public boolean onMenuItemClick(MenuItem item) {
                                if (dialogSheet == null) {
                                    dialogSheet = new DialogSheet(getContext());
                                    dialogSheet.addButton("编辑", new View.OnClickListener() {
                                        @Override public void onClick(View v) {
                                            dialogSheet.dismiss();
                                            RecordEditFragment fragment =
                                                RecordEditFragment.newInstance(true, new Gson().toJson(certificateEntity),
                                                    certificateEntity.getType());
                                            fragmentCallBack.onFragmentChange(fragment);
                                            //                                                    fragmentCallBack.onFragmentChange(new WorkExpeEditFragment().newInstance("编辑工作经历",experiencesEntity));
                                        }
                                    }).addButton("删除", new View.OnClickListener() {
                                        @Override public void onClick(View v) {
                                            dialogSheet.dismiss();
                                            showDialog(certificateEntity.getId());
                                        }
                                    });
                                    }
                                dialogSheet.show();
                                return true;
                            }
                        });
                    }

                    recorddetailTitle.setText(qcCertificateDetailResponse.getData().
                        getCertificate().getName()

                    );
                    recorddetailHost.setText(qcCertificateDetailResponse.getData().
                        getCertificate().getOrganization().getName()

                    );
                    recorddetailScore.setText(qcCertificateDetailResponse.getData().
                        getCertificate().getGrade()

                    );

                    if (TextUtils.isEmpty(qcCertificateDetailResponse.getData().getCertificate().getStart()) || TextUtils.isEmpty(
                        qcCertificateDetailResponse.getData().getCertificate().getEnd())) {
                        recorddetailTime.setText("长期有效");
                        validLable.setText("");
                    } else {
                        validLable.setText("有效期");
                        StringBuffer sb = new StringBuffer();
                        sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().
                            getCertificate().getStart()

                        )));
                        sb.append("至");
                        Date d = DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getEnd());
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        c.setTime(d);
                        if (c.get(Calendar.YEAR) == 3000) {
                                recorddetailTime.setText("长期有效");
                                validLable.setText("");
                        } else

                        {
                            validLable.setText("有效期");
                            sb.append(DateUtils.Date2YYYYMMDD(
                                DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getEnd())));
                            recorddetailTime.setText(sb.toString());
                            }
                    }

                    comfirmCreatetime.setText(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().

                            getCertificate().
                            getDate_of_issue()

                            )));
                    if (qcCertificateDetailResponse.getData().
                        getCertificate().
                        is_authenticated())

                    {
                        recordComfirmImg.setVisibility(View.VISIBLE);
                    } else {
                        recordComfirmImg.setVisibility(View.GONE);
                    }
                });
                }
        }, throwable -> {
        }, () -> {
        });
    }

    private void showDialog(final int id) {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("删除此条资质?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                        fragmentCallBack.ShowLoading("请稍后");
                        QcCloudClient.getApi().postApi.qcDelCertificate(id)

                            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(qcResponse -> {
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                ToastUtils.show("删除成功");
                                getActivity().onBackPressed();
                            } else {
                                ToastUtils.show(R.drawable.ic_share_fail, "删除失败");
                            }
                        }, throwable -> {
                        }, () -> {
                        });
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();
        }
        delDialog.show();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
