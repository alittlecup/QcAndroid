package cn.qingchengfit.saasbase.course.course.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.course.course.presenters.AddCoursePresenter;
import cn.qingchengfit.saasbase.databinding.FragmentSaasCourseAddBinding;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import javax.inject.Inject;

@Leaf(module = "course", path = "/add/")
public class AddCourseFragment extends BaseFragment {

  private CourseBaseInfoEditFragment mEditBaseInfo;
  FragmentSaasCourseAddBinding db;

  @Inject AddCoursePresenter presenter;
  @Inject GymWrapper gymWrapper;

  @Need public Boolean isPrivate;

  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      if (mEditBaseInfo != null) {
        CourseType courseDetail = mEditBaseInfo.getCourse();
        if (courseDetail != null) {
          String support_gym = "";
          if (gymWrapper.inBrand()) {
            // TODO: 2017/12/19 品牌下选择多加场馆
            //if (layoutSuitGym.getVisibility() == View.VISIBLE) {
            //  support_gym = (String) btnSuitGyms.getTag();
            //}
          } else {
            support_gym = null;
          }
          if (gymWrapper.inBrand() && support_gym == null) {
            ToastUtils.show("请至少选择一个家场馆");
            return true;
          }

          String Planid = null;
          if (courseDetail.getPlan() != null) {
            Planid = Long.toString(courseDetail.getPlan().getId());
          }
          CourseBody body = new CourseBody.Builder().name(courseDetail.getName())
            .capacity(courseDetail.getCapacity())
            .is_private(getArguments().getBoolean("p") ? 1 : 0)
            .length(courseDetail.getLength())
            .min_users(getArguments().getBoolean("p") ? null : courseDetail.getMin_users())
            .photo(courseDetail.getPhoto())
            .plan_id(Planid)
            .shop_ids(support_gym)
            .build();
          showLoading();
          presenter.addCourse(body);
        }
      }

      return true;
    }
  };

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_saas_course_add, container, false);
    delegatePresenter(presenter, this);
    //initToolbar(db.layo);
    if (mEditBaseInfo == null) {
      mEditBaseInfo = CourseBaseInfoEditFragment.newInstance(new CourseType(isPrivate));
    }

    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(R.id.frag_baseinfo,mEditBaseInfo);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    //toolbarTitile.setText(getArguments().getBoolean("p") ? "新增私教种类" : "新增团课种类");
    //toolbar.inflateMenu(R.menu.menu_compelete);
    //toolbar.setOnMenuItemClickListener(listener);
  }

  @Override public String getFragmentName() {
    return AddCourseFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClickSuitGyms() {
    //MutiChooseGymFragment.start(this, false, null,
    //    getArguments().getBoolean("p") ? PermissionServerUtils.PRISETTING_CAN_WRITE : PermissionServerUtils.TEAMSETTING_CAN_WRITE,
    //    RESULT_GYMS);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //if (resultCode == Activity.RESULT_OK) {
    //  if (requestCode == RESULT_GYMS) {
    //    String ids = "";
    //    ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
    //    if (shops != null) {
    //      for (int i = 0; i < shops.size(); i++) {
    //        if (i < shops.size() - 1) {
    //          ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
    //        } else {
    //          ids = TextUtils.concat(ids, shops.get(i).id).toString();
    //        }
    //      }
    //      btnSuitGyms.setTag(ids);
    //      btnSuitGyms.setContent(shops.size() + "家");
    //    }
    //  }
    //}
  }

  //@Override public void showSuitGym() {
  //  layoutSuitGym.setVisibility(View.VISIBLE);
  //}
}
