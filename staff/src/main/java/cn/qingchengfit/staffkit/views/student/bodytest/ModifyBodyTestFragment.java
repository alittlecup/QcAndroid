package cn.qingchengfit.staffkit.views.student.bodytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.responese.BodyTestExtra;
import cn.qingchengfit.model.responese.BodyTestMeasure;
import cn.qingchengfit.model.responese.BodyTestTemplateBase;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.model.GymBaseInfoAction;
import cn.qingchengfit.saascommon.widget.NumberInputFilter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.ImageGridAdapter;
import cn.qingchengfit.staffkit.views.custom.GalleryPhotoViewDialog;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBodyTestFragment extends BaseFragment implements ModifyBodyTestView {

  CommonInputView testDate;
  CommonInputView height;
  CommonInputView weight;
  CommonInputView bmi;
  CommonInputView bodyFatRate;
  CommonInputView chest;
  CommonInputView waistline;
  CommonInputView hipline;
  RecyclerView recyclerview;
  TextView delete;
  LinearLayout otherData;
  TextView photosTitle;
  CommonInputView leftUpper;
  CommonInputView rightUpper;
  CommonInputView leftThigh;
  CommonInputView rightThigh;
  CommonInputView leftCalf;
  CommonInputView rightCalf;

  @Inject ModifyBodyTestPresenter presenter;
  @Inject StudentWrap studentBean;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  @Inject GymBaseInfoAction gymBaseInfoAction;
  Toolbar toolbar;
  TextView toolbarTitile;
  FrameLayout toolbarLayout;

  private TimeDialogWindow pwTime;
  private View view;
  private ImageGridAdapter imageGridAdapter;
  private GridLayoutManager gridLayoutManager;
  private List<BodyTestBody.Photo> datas = new ArrayList<>();
  private String measureId;
  private String mCurId, mCurModel;
  private MultiChoosePicFragment picDialog;

  public ModifyBodyTestFragment() {
  }

  /**
   *
   */
  public static ModifyBodyTestFragment newInstance(String measureid) {

    Bundle args = new Bundle();
    args.putString("measureid", measureid);
    ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    measureId = getArguments().getString("measureid");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        Shop shops = (Shop) IntentUtils.getParcelable(data);
        if (shops != null) {
          CoachService gym = gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shops.id);
          if (gym != null) {
            presenter.queryBodyModel();
            mCurId = gym.getId();
            mCurModel = gym.getModel();
          }
        }
      }
    } else {
      if (requestCode == 1) {
        getActivity().onBackPressed();
      }
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    if (TextUtils.isEmpty(measureId)) {//添加
      toolbarTitile.setText("添加体测");
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          saveInfo();
          return false;
        }
      });
    } else {
      toolbarTitile.setText("编辑体测");
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          saveInfo();
          return false;
        }
      });
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_modify_body_test, container, false);
    testDate = (CommonInputView) view.findViewById(R.id.test_date);
    height = (CommonInputView) view.findViewById(R.id.height);
    weight = (CommonInputView) view.findViewById(R.id.weight);
    bmi = (CommonInputView) view.findViewById(R.id.bmi);
    bodyFatRate = (CommonInputView) view.findViewById(R.id.body_fat_rate);
    chest = (CommonInputView) view.findViewById(R.id.chest);
    waistline = (CommonInputView) view.findViewById(R.id.waistline);
    hipline = (CommonInputView) view.findViewById(R.id.hipline);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    delete = (TextView) view.findViewById(R.id.delete);
    otherData = (LinearLayout) view.findViewById(R.id.other_data);
    photosTitle = (TextView) view.findViewById(R.id.photos_title);
    leftUpper = (CommonInputView) view.findViewById(R.id.left_upper);
    rightUpper = (CommonInputView) view.findViewById(R.id.right_upper);
    leftThigh = (CommonInputView) view.findViewById(R.id.left_thigh);
    rightThigh = (CommonInputView) view.findViewById(R.id.right_thigh);
    leftCalf = (CommonInputView) view.findViewById(R.id.left_calf);
    rightCalf = (CommonInputView) view.findViewById(R.id.right_calf);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    view.findViewById(R.id.test_date).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickDate();
      }
    });
    view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        OnDel();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    if (TextUtils.isEmpty(measureId)) {//添加
      mCallbackActivity.setToolbar("添加体测", false, null, R.menu.menu_save,
          new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
              saveInfo();
              return true;
            }
          });
      delete.setVisibility(View.GONE);
      testDate.setContent(DateUtils.Date2YYYYMMDD(new Date()));
      if (gymWrapper.inBrand()) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(studentBean.getStudentBean().getSupportIdList());
        MutiChooseGymFragment.start(this, true, list,
            PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE, 1);
      } else {
        mCurId = gymWrapper.id();
        mCurModel = gymWrapper.model();
        showLoading();
        presenter.queryBodyModel();
      }
    } else {

      showLoading();
      presenter.queryInfo(measureId);
    }

    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

    imageGridAdapter = new ImageGridAdapter(datas);
    imageGridAdapter.setIsEditable(true);
    gridLayoutManager = new GridLayoutManager(getContext(), 3);
    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerview.setLayoutManager(gridLayoutManager);
    recyclerview.setAdapter(imageGridAdapter);
    imageGridAdapter.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
        if (v.getId() == R.id.delete) {
          datas.remove(pos);
          imageGridAdapter.notifyDataSetChanged();
        } else {
          if (pos < datas.size()) {
            GalleryPhotoViewDialog galleryPhotoViewDialog =
                new GalleryPhotoViewDialog(getContext());
            galleryPhotoViewDialog.setImage(getImages());
            galleryPhotoViewDialog.setSelected(pos);
            galleryPhotoViewDialog.show();
          } else {
            addImage();
          }
        }
      }
    });
    setCommonInputViewFilter();
    return view;
  }

  void addImage() {
    picDialog = MultiChoosePicFragment.newInstance(null);
    picDialog.setUpLoadImageCallback(uris -> {
      if (uris != null && !uris.isEmpty()) {
        for (String uri : uris) {
          BodyTestBody.Photo photo = new BodyTestBody.Photo();
          photo.photo = uri;
          datas.add(photo);
        }
      }
      imageGridAdapter.notifyDataSetChanged();
    });
    if (!picDialog.isVisible()) picDialog.show(getChildFragmentManager(), "choose");
  }

  private void setCommonInputViewFilter() {
    bmi.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    weight.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    height.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    bodyFatRate.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    leftCalf.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    leftThigh.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    leftUpper.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    rightCalf.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    rightThigh.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    rightUpper.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    chest.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    hipline.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
    waistline.getEditText().setFilters(new InputFilter[] { new NumberInputFilter() });
  }

  public void onClickDate() {
    chooseDate();
  }

  public void setDate(String s) {
    testDate.setContent(s);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public BodyTestMeasure getMeasure() {
    BodyTestMeasure measure = new BodyTestMeasure();
    measure.hipline = hipline.getContent();
    measure.waistline = waistline.getContent();
    measure.circumference_of_left_upper = leftUpper.getContent();
    measure.circumference_of_right_upper = rightUpper.getContent();
    measure.circumference_of_chest = chest.getContent();
    measure.circumference_of_left_calf = leftCalf.getContent();
    measure.circumference_of_right_calf = rightCalf.getContent();
    measure.circumference_of_left_thigh = leftThigh.getContent();
    measure.circumference_of_right_thigh = rightThigh.getContent();
    measure.bmi = bmi.getContent();
    measure.created_at = testDate.getContent();
    measure.body_fat_rate = bodyFatRate.getContent();
    measure.weight = weight.getContent();
    measure.height = height.getContent();

    return measure;
  }

  public void chooseDate() {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
        Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        testDate.setContent(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
  }

  public void saveInfo() {
    BodyTestMeasure mMeasure = getMeasure();
    BodyTestBody addBodyTestBean = new BodyTestBody();
    addBodyTestBean.model = mCurModel;
    addBodyTestBean.id = mCurId;
    addBodyTestBean.created_at = testDate.getContent() + "T00:00:00";
    if (!TextUtils.isEmpty(mMeasure.bmi)) {
      addBodyTestBean.bmi = mMeasure.bmi;
    }
    if (!TextUtils.isEmpty(mMeasure.weight)) {
      addBodyTestBean.weight = mMeasure.weight;
    }
    if (!TextUtils.isEmpty(mMeasure.height)) {
      addBodyTestBean.height = mMeasure.height;
    }
    if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
      addBodyTestBean.body_fat_rate = mMeasure.body_fat_rate;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
      addBodyTestBean.circumference_of_left_calf = mMeasure.circumference_of_left_calf;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
      addBodyTestBean.circumference_of_right_calf = mMeasure.circumference_of_right_calf;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
      addBodyTestBean.circumference_of_chest = mMeasure.circumference_of_chest;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
      addBodyTestBean.circumference_of_left_thigh = mMeasure.circumference_of_left_thigh;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
      addBodyTestBean.circumference_of_right_thigh = mMeasure.circumference_of_right_thigh;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
      addBodyTestBean.circumference_of_left_upper = mMeasure.circumference_of_left_upper;
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
      addBodyTestBean.circumference_of_right_upper = mMeasure.circumference_of_right_upper;
    }
    if (!TextUtils.isEmpty(mMeasure.hipline)) {
      addBodyTestBean.hipline = mMeasure.hipline;
    }
    if (!TextUtils.isEmpty(mMeasure.waistline)) {
      addBodyTestBean.waistline = mMeasure.waistline;
    }
    //        if (!mModel.equalsIgnoreCase("service")) {
    List<BodyTestExtra> extras = new ArrayList<>();
    for (int i = 0; i < otherData.getChildCount(); i++) {
      CommonInputView v = (CommonInputView) otherData.getChildAt(i);
      BodyTestExtra extra = new BodyTestExtra();
      extra.id = (String) v.getTag(R.id.tag_1);
      extra.unit = (String) v.getTag(R.id.tag_2);
      extra.name = (String) v.getTag(R.id.tag_0);
      extra.value = v.getContent();
      extras.add(extra);
    }
    addBodyTestBean.extra = new Gson().toJson(extras);
    addBodyTestBean.photos = datas;
    addBodyTestBean.user_id = studentBean.id();

    if (!TextUtils.isEmpty(measureId)) {//修改
      if (gymWrapper.inBrand()) {
        new MaterialDialog.Builder(getContext()).content("请到场馆中修改会员体测数据")
            .autoDismiss(true)
            .positiveText(R.string.common_comfirm)
            .show();
      } else {
        presenter.modifyBodyTest(measureId, gymWrapper.id(), gymWrapper.model(), addBodyTestBean);
      }
    } else { //添加
      showLoading();
      presenter.addBodyTest(addBodyTestBean, mCurId, mCurModel);
    }
  }

  public void OnDel() {
    if (serPermisAction.checkMuti(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE,
        studentBean.getStudentBean().getSupportIdList())) {
      // TODO: 2018/5/30  review

      DialogUtils.showConfirm(getActivity(), "", "是否删除此条体测信息?", (dialog, action) -> {
        dialog.dismiss();
      });
    } else {
      showAlert(getString(R.string.alert_permission_forbid));
    }
  }

  public List<String> getImages() {
    List<String> images = new ArrayList<>();
    for (BodyTestBody.Photo p : datas) {
      images.add(p.photo);
    }
    return images;
  }

  @Override public void onShowBase(BodyTestTemplateBase mBase) {
    hideLoading();
    bmi.setVisibility(mBase.show_bmi ? View.VISIBLE : View.GONE);
    bodyFatRate.setVisibility(mBase.show_body_fat_rate ? View.VISIBLE : View.GONE);
    leftCalf.setVisibility(mBase.show_circumference_of_left_calf ? View.VISIBLE : View.GONE);
    rightCalf.setVisibility(mBase.show_circumference_of_right_calf ? View.VISIBLE : View.GONE);
    chest.setVisibility(mBase.show_circumference_of_chest ? View.VISIBLE : View.GONE);
    leftThigh.setVisibility(mBase.show_circumference_of_left_thigh ? View.VISIBLE : View.GONE);
    rightThigh.setVisibility(mBase.show_circumference_of_right_thigh ? View.VISIBLE : View.GONE);
    leftUpper.setVisibility(mBase.show_circumference_of_left_upper ? View.VISIBLE : View.GONE);
    rightUpper.setVisibility(mBase.show_circumference_of_right_upper ? View.VISIBLE : View.GONE);
    height.setVisibility(mBase.show_height ? View.VISIBLE : View.GONE);
    hipline.setVisibility(mBase.show_hipline ? View.VISIBLE : View.GONE);
    waistline.setVisibility(mBase.show_waistline ? View.VISIBLE : View.GONE);
    weight.setVisibility(mBase.show_weight ? View.VISIBLE : View.GONE);
  }

  @Override public void onShowMeasure(BodyTestMeasure mMeasure) {
    hideLoading();
    testDate.setContent(
        DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(mMeasure.created_at)));

    if (!TextUtils.isEmpty(mMeasure.bmi)) {
      bmi.setVisibility(View.VISIBLE);
      bmi.setContent(String.format("%s", mMeasure.bmi));
    }
    if (!TextUtils.isEmpty(mMeasure.weight)) {
      weight.setVisibility(View.VISIBLE);
      weight.setContent(String.format("%s", mMeasure.weight));
    }
    if (!TextUtils.isEmpty(mMeasure.height)) {
      height.setVisibility(View.VISIBLE);
      height.setContent(String.format("%s", mMeasure.height));
    }
    if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
      bodyFatRate.setVisibility(View.VISIBLE);
      bodyFatRate.setContent(String.format("%s", mMeasure.body_fat_rate));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
      leftCalf.setVisibility(View.VISIBLE);
      leftCalf.setContent(String.format("%s", mMeasure.circumference_of_left_calf));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
      rightCalf.setVisibility(View.VISIBLE);
      rightCalf.setContent(String.format("%s", mMeasure.circumference_of_right_calf));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
      chest.setVisibility(View.VISIBLE);
      chest.setContent(String.format("%s", mMeasure.circumference_of_chest));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
      rightThigh.setVisibility(View.VISIBLE);
      rightThigh.setContent(String.format("%s", mMeasure.circumference_of_right_thigh));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
      leftThigh.setVisibility(View.VISIBLE);
      leftThigh.setContent(String.format("%s", mMeasure.circumference_of_left_thigh));
    }

    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
      leftUpper.setVisibility(View.VISIBLE);
      leftUpper.setContent(String.format("%s", mMeasure.circumference_of_left_upper));
    }
    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
      rightUpper.setVisibility(View.VISIBLE);
      rightUpper.setContent(String.format("%s", mMeasure.circumference_of_right_upper));
    }
    if (!TextUtils.isEmpty(mMeasure.hipline)) {
      hipline.setVisibility(View.VISIBLE);
      hipline.setContent(String.format("%s", mMeasure.hipline));
    }
    if (!TextUtils.isEmpty(mMeasure.waistline)) {
      waistline.setVisibility(View.VISIBLE);
      waistline.setContent(String.format("%s", mMeasure.waistline));
    }
  }

  @Override public void onShowExtra(boolean show, List<BodyTestBody.Photo> photos,
      List<BodyTestExtra> extras) {
    if (show) {
      imageGridAdapter.setIsEditable(true);
      photosTitle.setVisibility(View.VISIBLE);
      if (extras != null) {
        for (BodyTestExtra extra : extras) {
          CommonInputView commonInputView = new CommonInputView(getContext());

          commonInputView.setTag(R.id.tag_0, extra.name);
          commonInputView.setTag(R.id.tag_1, extra.id);
          commonInputView.setTag(R.id.tag_2, extra.unit);
          commonInputView.setEditable(true);
          commonInputView.setMaxLines(100);
          otherData.addView(commonInputView);
          commonInputView.setContentColor(ContextCompat.getColor(getContext(), R.color.text_dark));
          if (!TextUtils.isEmpty(extra.unit)) {
            commonInputView.setUnit(extra.unit);
          }
          commonInputView.setLabel(extra.name);
          commonInputView.setContent(extra.value);
        }
      }
      if (photos != null) {
        datas.clear();
        datas.addAll(photos);
        imageGridAdapter.notifyDataSetChanged();
      }
    } else {
      datas.clear();
      imageGridAdapter.setIsEditable(false);
      imageGridAdapter.notifyDataSetChanged();
      photosTitle.setVisibility(View.GONE);
    }
  }

  @Override public void onFailed(String s) {
    hideLoading();
    ToastUtils.show(s);
  }

  @Override public void onSuccess() {
    hideLoading();
    ToastUtils.showS("操作成功");
    getActivity().onBackPressed();
  }

  @Override public String getFragmentName() {
    return ModifyBodyTestFragment.class.getName();
  }
}
