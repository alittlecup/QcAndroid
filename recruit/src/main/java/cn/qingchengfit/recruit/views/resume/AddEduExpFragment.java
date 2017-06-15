package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * Created by Paper on 2017/6/13.
 */
public class AddEduExpFragment extends BaseFragment {

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.civ_school_name) CommonInputView civSchoolName;
    @BindView(R2.id.civ_speciality) CommonInputView civSpeciality;
    @BindView(R2.id.civ_in_time) CommonInputView civInTime;
    @BindView(R2.id.civ_out_time) CommonInputView civOutTime;
    @BindView(R2.id.civ_degree) CommonInputView civDegree;
    @Inject QcRestRepository qcRestRepository;
    private SimpleScrollPicker simpleScrollPicker;
    private TimeDialogWindow timeDialogWindow;
    private int curDegree = 0;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleScrollPicker = new SimpleScrollPicker(getContext());
        timeDialogWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edu_exp, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("添加教育经历");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(civSchoolName.getContent())) {
                    showAlert("请填写学校名称");
                    return true;
                }
                if (TextUtils.isEmpty(civSpeciality.getContent())) {
                    showAlert("请填写学位名称");
                    return true;
                }

                if (TextUtils.isEmpty(civDegree.getContent())) {
                    showAlert("请选择职位");
                    return true;
                }
                if (TextUtils.isEmpty(civInTime.getContent()) || TextUtils.isEmpty(civInTime.getContent()) || (DateUtils.YYMM2date(
                    civInTime.getContent()).getTime() >= (DateUtils.YYMM2date(civOutTime.getContent()).getTime()))) {
                    showAlert("请选择正确入校时间和毕业时间");
                    return true;
                }
                showLoading();
                RxRegiste(qcRestRepository.createGetApi(PostApi.class)
                    .addEducation(new Education.Builder().education(curDegree)
                        .name(civSchoolName.getContent())
                        .major(civSpeciality.getContent())
                        .start(civInTime.getContent())
                        .end(civOutTime.getContent())
                        .build())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponse>() {
                        @Override public void call(QcResponse qcResponse) {
                            hideLoading();
                            getActivity().onBackPressed();
                            onShowError("添加成功！");
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            hideLoading();
                            onShowError(throwable.getMessage());
                        }
                    }));
                return false;
            }
        });
    }

    @Override public String getFragmentName() {
        return AddEduExpFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 入校时间
     */
    @OnClick(R2.id.civ_in_time) public void onCivInTimeClicked() {
        timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                civInTime.setContent(DateUtils.Date2YYYYMM(date));
            }
        });
        timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, DateUtils.YYMM2date(civInTime.getContent()));
    }

    /**
     * 毕业时间
     */
    @OnClick(R2.id.civ_out_time) public void onCivOutTimeClicked() {
        timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                civOutTime.setContent(DateUtils.date2YYMM(date));
            }
        });
        timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, DateUtils.YYMM2date(civOutTime.getContent()));
    }

    /**
     * 学位
     */
    @OnClick(R2.id.civ_degree) public void onCivDegreeClicked() {
        final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.education_degree)));
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                curDegree = pos;
                civDegree.setContent(d.get(pos));
            }
        });
        simpleScrollPicker.show(d, 0);
    }
}
