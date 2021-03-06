package cn.qingchengfit.staffkit.views.student.bodytest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;

import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.responese.BodyTestExtra;
import cn.qingchengfit.model.responese.BodyTestMeasure;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.ImageGridAdapter;
import cn.qingchengfit.staffkit.views.custom.InterupteLinearLayout;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BodyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BodyTestFragment extends BaseFragment implements BodyTestView {
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
	ImageView imgModel;
	TextView hipline;
	TextView chest;
	TextView waistline;
	TextView height;
	LinearLayout heightLayout;
	TextView weight;
	LinearLayout weightLayout;
	TextView bmi;
	LinearLayout bmiLayout;
	TextView bodyFatRate;
	LinearLayout bodyFatRateLayout;
	TextView title;
	InterupteLinearLayout otherData;
	RecyclerView recyclerview;
	TextView leftUpper;
	TextView rightUpper;
	TextView leftThigh;
	TextView rightThigh;
	TextView leftCalf;
	TextView rightCalf;
	TextView testPicTitle;

  @Inject BodyTestPresenter presenter;
  @Inject StudentWrap studentBean;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout toolbarLayout;

  private int mGender;
  private ImageGridAdapter imageGridAdapter;
  private GridLayoutManager gridLayoutManager;
  private String mMeasureId;
  private List<BodyTestBody.Photo> datas = new ArrayList<>();

  public BodyTestFragment() {
  }

  public static BodyTestFragment newInstance(String measreid, int gender) {
    BodyTestFragment fragment = new BodyTestFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, measreid);
    args.putInt(ARG_PARAM2, gender);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mMeasureId = getArguments().getString(ARG_PARAM1);
      //            mMeasure = getArguments().getParcelable(ARG_PARAM2);
      mGender = getArguments().getInt(ARG_PARAM2);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_body_test, container, false);
    imgModel = (ImageView) view.findViewById(R.id.img_model);
    hipline = (TextView) view.findViewById(R.id.hipline);
    chest = (TextView) view.findViewById(R.id.chest);
    waistline = (TextView) view.findViewById(R.id.waistline);
    height = (TextView) view.findViewById(R.id.height);
    heightLayout = (LinearLayout) view.findViewById(R.id.height_layout);
    weight = (TextView) view.findViewById(R.id.weight);
    weightLayout = (LinearLayout) view.findViewById(R.id.weight_layout);
    bmi = (TextView) view.findViewById(R.id.bmi);
    bmiLayout = (LinearLayout) view.findViewById(R.id.bmi_layout);
    bodyFatRate = (TextView) view.findViewById(R.id.body_fat_rate);
    bodyFatRateLayout = (LinearLayout) view.findViewById(R.id.body_fat_rate_layout);
    title = (TextView) view.findViewById(R.id.title);
    otherData = (InterupteLinearLayout) view.findViewById(R.id.other_data);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    leftUpper = (TextView) view.findViewById(R.id.left_upper);
    rightUpper = (TextView) view.findViewById(R.id.right_upper);
    leftThigh = (TextView) view.findViewById(R.id.left_thigh);
    rightThigh = (TextView) view.findViewById(R.id.right_thigh);
    leftCalf = (TextView) view.findViewById(R.id.left_calf);
    rightCalf = (TextView) view.findViewById(R.id.right_calf);
    testPicTitle = (TextView) view.findViewById(R.id.test_pic_title);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);

    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    if (mGender == 0) {
      Glide.with(getContext()).load(R.drawable.img_model_male).into(imgModel);
    } else {
      Glide.with(getContext()).load(R.drawable.img_model_female).into(imgModel);
    }

    presenter.queryBodyTest(mMeasureId);

    imageGridAdapter = new ImageGridAdapter(datas);

    gridLayoutManager = new GridLayoutManager(getContext(), 3);
    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerview.setLayoutManager(gridLayoutManager);
    recyclerview.setAdapter(imageGridAdapter);
    view.setOnTouchListener((v, event) -> true);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("体测数据");
    toolbar.inflateMenu(R.menu.menu_edit);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        //跳到编辑页面
        if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
          getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), ModifyBodyTestFragment.newInstance(mMeasureId))
            .addToBackStack(null)
            .commit();
        } else {
          showAlert(getString(R.string.alert_permission_forbid));
        }

        return true;
      }
    });
  }

  @Override public void onMeasure(BodyTestMeasure mMeasure) {
    title.setText(
      DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(mMeasure.created_at)) + "体测数据");
    if (!TextUtils.isEmpty(mMeasure.bmi)) {
      bmiLayout.setVisibility(View.VISIBLE);
      bmi.setText(String.format("%s", mMeasure.bmi));
    }
    if (!TextUtils.isEmpty(mMeasure.weight)) {
      weightLayout.setVisibility(View.VISIBLE);
      weight.setText(String.format("%s", mMeasure.weight));
    }
    if (!TextUtils.isEmpty(mMeasure.height)) {
      heightLayout.setVisibility(View.VISIBLE);
      height.setText(String.format("%s", mMeasure.height));
    }
    if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
      bodyFatRateLayout.setVisibility(View.VISIBLE);
      bodyFatRate.setText(String.format("%s", mMeasure.body_fat_rate));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
      leftCalf.setVisibility(View.VISIBLE);
      leftCalf.setText(String.format("左小腿围: %s cm", mMeasure.circumference_of_left_calf));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
      rightCalf.setVisibility(View.VISIBLE);
      rightCalf.setText(String.format("右小腿围: %s cm", mMeasure.circumference_of_right_calf));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
      chest.setVisibility(View.VISIBLE);
      chest.setText(String.format("胸围: %s cm", mMeasure.circumference_of_chest));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
      leftThigh.setVisibility(View.VISIBLE);
      leftThigh.setText(String.format("左大腿围: %s cm", mMeasure.circumference_of_left_thigh));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
      rightThigh.setVisibility(View.VISIBLE);
      rightThigh.setText(String.format("右大腿围: %s cm", mMeasure.circumference_of_right_thigh));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
      leftUpper.setVisibility(View.VISIBLE);
      leftUpper.setText(String.format("左上臂围: %s cm", mMeasure.circumference_of_left_upper));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
      rightUpper.setVisibility(View.VISIBLE);
      rightUpper.setText(String.format("右上臂围: %s cm", mMeasure.circumference_of_right_upper));
    }
    if (!TextUtils.isEmpty(mMeasure.hipline)) {
      hipline.setVisibility(View.VISIBLE);
      hipline.setText(String.format("臀围: %s cm", mMeasure.hipline));
    }
    if (!TextUtils.isEmpty(mMeasure.waistline)) {
      waistline.setVisibility(View.VISIBLE);
      waistline.setText(String.format("腰围: %s cm", mMeasure.waistline));
    }
  }

  public List<String> getImages() {
    List<String> images = new ArrayList<>();
    for (BodyTestBody.Photo p : datas) {
      images.add(p.photo);
    }
    return images;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @Override public void onShowPics(boolean ishow, List<BodyTestBody.Photo> pics) {
    if (ishow) {
      testPicTitle.setVisibility(View.VISIBLE);
      datas.clear();
      datas.addAll(pics);
      imageGridAdapter.notifyDataSetChanged();
    } else {
      testPicTitle.setVisibility(View.GONE);
    }
  }

  @Override public void onExtras(List<BodyTestExtra> extras) {
    for (BodyTestExtra extra : extras) {
      CommonInputView commonInputView = new CommonInputView(getContext());
      commonInputView.setTag(R.id.tag_0, extra.name);
      commonInputView.setTag(R.id.tag_1, extra.id);
      commonInputView.setTag(R.id.tag_2, extra.unit);
      commonInputView.setMaxLines(100);
      commonInputView.setContentColor(ContextCompat.getColor(getContext(), R.color.text_dark));
      otherData.addView(commonInputView);
      commonInputView.setLabel(extra.name + "(" + extra.unit + ")");
      commonInputView.setContent(extra.value);
    }
  }

  @Override public String getFragmentName() {
    return BodyTestFragment.class.getName();
  }
}
