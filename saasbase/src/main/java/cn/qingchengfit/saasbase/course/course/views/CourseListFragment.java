package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasRouter;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.items.CourseItem;
import cn.qingchengfit.saasbase.course.course.network.response.CourseLisWrap;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/8/2.
 */
public class CourseListFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject SerPermisAction serPermisAction;
  @Inject ICourseModel courseApi;
  @Inject SaasRouter saasRouter;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.srl) SwipeRefreshLayout srl;

  private CommonFlexAdapter mAdatper;
  private boolean mIsPrivate;


  public static CourseListFragment newInstance(boolean isPrivate) {

    Bundle args = new Bundle();
    args.putBoolean("isPrivate", isPrivate);
    CourseListFragment fragment = new CourseListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mIsPrivate = getArguments().getBoolean("isPrivate");
    }
  }

  @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_course_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    if ((!mIsPrivate && !serPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMSETTING)) || (
        mIsPrivate
            && !serPermisAction.checkAtLeastOne(PermissionServerUtils.PRISETTING))) {
      View v = inflater.inflate(R.layout.item_common_no_data, container, false);
      ImageView img = (ImageView) v.findViewById(R.id.img);
      img.setImageResource(R.drawable.ic_no_permission);
      TextView hint = (TextView) v.findViewById(R.id.hint);
      hint.setText(R.string.sorry_for_no_permission);
      return v;
    }

    mAdatper = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDefaultDivider().withBottomEdge(true));
    rv.setAdapter(mAdatper);
    srl.setOnRefreshListener(this);
    onRefresh();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
  }



  @Override public String getFragmentName() {
    return CourseListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    return true;
  }

  @OnClick(R2.id.add_course_btn) public void onViewClicked() {
    //saasRouter.routerTo(mIsPrivate ? CourseUri.COURSE_TYPE_GROUP_ADD:CourseUri.COURSE_TYPE_PRIVATE_ADD);
  }

  @Override public void onRefresh() {
    RxRegiste(courseApi.qcGetCourses(mIsPrivate)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CourseLisWrap>>() {
          @Override public void call(QcDataResponse<CourseLisWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data != null && qcResponse.data.courses != null) {
                List<IFlexible> datas = new ArrayList<IFlexible>();
                for (CourseType course : qcResponse.data.courses) {
                  datas.add(new CourseItem(course));
                }
                if (datas.size() == 0) {
                  datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无课程种类"));
                }
                mAdatper.updateDataSet(datas, true);
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }
}
