package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.items.CourseItem;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.fragments.TitleFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
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
@Leaf(module = "course", path = "/list/") public class CourseListFragment extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,TitleFragment,
    FlexibleAdapter.OnUpdateListener {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject IPermissionModel serPermisAction;
  @Inject public ICourseModel courseApi;
	Toolbar toolbar;
	public TextView toolbarTitle;
  TextView tvBottomContent;
	ViewGroup toolbarLayout;
	RecyclerView rv;
	public SwipeRefreshLayout srl;
	public FloatingActionButton floatingActionButton;
	public LinearLayout llBottomAdd;
  @Need public Boolean mIsPrivate = false;

  protected CommonFlexAdapter commonFlexAdapter;

  public static CourseListFragment newInstance(boolean isPrivate) {
    Bundle args = new Bundle();
    args.putBoolean("p",isPrivate);
    CourseListFragment fragment = new CourseListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null && getArguments().containsKey("p")){
      mIsPrivate = getArguments().getBoolean("p",true);
    }
  }

  @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_course_list, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (ViewGroup) view.findViewById(R.id.toolbar_layout);
    rv = (RecyclerView) view.findViewById(R.id.rv);
    srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    llBottomAdd=view.findViewById(R.id.ll_bottom_add);
    tvBottomContent = view.findViewById(R.id.tv_bottom_content);
    tvBottomContent.setOnClickListener(v -> onViewClicked());
    floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_course_btn);
    view.findViewById(R.id.add_course_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClicked();
      }
    });

    initToolbar(toolbar);
    if ((!mIsPrivate && !serPermisAction.checkInBrand(PermissionServerUtils.TEAMSETTING)) || (
      mIsPrivate
        && !serPermisAction.checkInBrand(PermissionServerUtils.PRISETTING))) {
      View v = inflater.inflate(R.layout.item_common_no_data, container, false);
      ImageView img = (ImageView) v.findViewById(R.id.img);
      img.setImageResource(R.drawable.ic_no_permission);
      TextView hint = (TextView) v.findViewById(R.id.hint);
      hint.setText(R.string.sorry_for_no_permission);
      return v;
    }

    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.addItemDecoration(
      new FlexibleItemDecoration(getContext()).withDefaultDivider().withBottomEdge(true));
    rv.setAdapter(commonFlexAdapter);
    srl.setOnRefreshListener(this);
    onRefresh();
    SensorsUtils.trackScreen(this.getClass().getCanonicalName()+"_"+(mIsPrivate?"private":"group"));

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    if (getParentFragment() instanceof CourseHomeInBrandFragment) {
      toolbarLayout.setVisibility(View.GONE);
    }else {
      super.initToolbar(toolbar);
      toolbarTitle.setText(mIsPrivate ? "私教课程" : "团课课程");
    }
  }

  @Override public String getFragmentName() {
    return CourseListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if ( !serPermisAction.checkInBrand(mIsPrivate?PermissionServerUtils.PRISETTING_CAN_CHANGE:PermissionServerUtils.TEAMSETTING_CAN_CHANGE)){
      showAlert(R.string.sorry_for_no_permission);
      return true;
    }

    IFlexible iFlexible = commonFlexAdapter.getItem(position);
    if (iFlexible == null) return true;
    if (iFlexible instanceof CourseItem) {

      routeTo("/detail/",
        new CourseDetailParams().mCourseDetail(((CourseItem) iFlexible).courseDetail).build());
    }

    return true;
  }

  /**
   * 新增课程
   */
 public void onViewClicked() {
    if ( !serPermisAction.checkInBrand(mIsPrivate?PermissionServerUtils.PRISETTING_CAN_WRITE:PermissionServerUtils.TEAMSETTING_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return ;
    }
    routeTo("/add/", new AddCourseParams().isPrivate(mIsPrivate).build());
  }

  /**
   * 刷新列表
   */
  @Override public void onRefresh() {
    RxRegiste(courseApi.qcGetCourses(mIsPrivate)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(qcResponse -> {
        srl.setRefreshing(false);
        if (ResponseConstant.checkSuccess(qcResponse)) {
          if (qcResponse.data != null && qcResponse.data.courses != null) {
            List<IFlexible> datas = new ArrayList<IFlexible>();
            for (CourseType course : qcResponse.data.courses) {
              datas.add(new CourseItem(course));
            }
            if (datas.size() == 0) {
              datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无课程种类"));
            }
            commonFlexAdapter.updateDataSet(datas, true);
          }
        } else {
          onShowError(qcResponse.getMsg());
        }
      }, new NetWorkThrowable()));
  }

  @Override public String getTitle() {
    return (getArguments() != null && getArguments().getBoolean("p")) ? "私教课程" : "团课课程";
  }

  @Override public void onUpdateEmptyView(int size) {

  }
}
