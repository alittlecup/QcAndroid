package cn.qingchengfit.staffkit.views.student.attendance;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.attendance.item.NotSignClassItem;
import cn.qingchengfit.staffkit.views.student.attendance.model.NotSignStudent;
import cn.qingchengfit.staffkit.views.student.attendance.presenter.NotSignPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static android.view.View.GONE;

/**
 * Created by fb on 2017/9/18.
 */

public class AttendanceNotSignFragment extends BaseFragment
    implements NotSignPresenter.MVPView, FilterCustomFragment.OnBackFilterDataListener {

  @BindView(R.id.text_not_sign_filter_time) TextView textNotSignFilterTime;
  @BindView(R.id.image_not_sign_filter_time) ImageView imageNotSignFilterTime;
  @BindView(R.id.layout_not_sign_time) LinearLayout layoutNotSignTime;
  @BindView(R.id.text_not_sign_filter_count) TextView textNotSignFilterCount;
  @BindView(R.id.image_not_sign_filter_count) ImageView imageNotSignFilterCount;
  @BindView(R.id.text_filter_total) TextView textFilterTotal;
  @BindView(R.id.recycler_not_sign) RecyclerView recyclerNotSign;
  @Inject NotSignPresenter presenter;
  @BindView(R.id.frag_not_sign_filter) FrameLayout fragNotSignFilter;
  @BindView(R.id.layout_not_sign_count) LinearLayout layoutNotSignCount;
  @BindView(R.id.fragment_not_sign_count) FrameLayout fragLayoutCount;
  @BindView(R.id.shadow) View shadow;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private CommonFlexAdapter adapter;
  private FilterFragment timeFilterFragment;
  private FilterFragment countFilterFragment;
  private FilterCustomFragment filterCustomFragment;
  private String start;    //开始日期
  private String end;     //结束日期
  private int count = 5;    //默认5节以上

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_not_sign_class, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initView();
    initFilter();
    return view;
  }

  private void initView() {
    start = DateUtils.minusDay(new Date(), 6);
    end = DateUtils.getStringToday();
    textNotSignFilterTime.setText(getResources().getString(R.string.text_filter_not_sign_time, 7));
    textNotSignFilterCount.setText(
        getResources().getString(R.string.text_filter_not_sign_count, 5));
    timeFilterFragment = new FilterFragment();
    countFilterFragment = new FilterFragment();
    getChildFragmentManager().beginTransaction()
        .add(R.id.frag_not_sign_filter, timeFilterFragment)
        .commit();
    getChildFragmentManager().beginTransaction()
        .add(R.id.fragment_not_sign_count, countFilterFragment)
        .commit();

    ViewGroup.LayoutParams params = fragNotSignFilter.getLayoutParams();
    params.height = 0;
    fragNotSignFilter.setLayoutParams(params);

    ViewGroup.LayoutParams params1 = fragLayoutCount.getLayoutParams();
    params1.height = 0;
    fragLayoutCount.setLayoutParams(params1);

    shadow.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(fragNotSignFilter.getLayoutParams().height > 0) {
          timeFilterFragment.setFilterAnimation(fragNotSignFilter, false);
          startShadowAnim(false);
        }
        if (fragLayoutCount.getLayoutParams().height > 0) {
          countFilterFragment.setFilterAnimation(fragLayoutCount, false);
          startShadowAnim(false);
        }
      }
    });

    presenter.getNotSignStudent(start, end, count);
    adapter = new CommonFlexAdapter(itemList);
    recyclerNotSign.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerNotSign.addItemDecoration(
        new FlexibleItemDecoration(getContext())
            .addItemViewType(R.layout.item_not_sign_class, MeasureUtils.dpToPx(120f, getResources()), 0, 0, 0)
            .withDivider(R.drawable.divider_card_list)
            .withBottomEdge(true));
    recyclerNotSign.setAdapter(adapter);
  }

  private void initFilter() {
    timeFilterFragment.setMaxHeight(MeasureUtils.dpToPx(432f, getResources()));
    countFilterFragment.setMaxHeight(MeasureUtils.dpToPx(432f, getResources()));
    //自定义筛选
    filterCustomFragment = FilterCustomFragmentBuilder.newFilterCustomFragment("时间段");
    filterCustomFragment.setSelectTime(true);
    filterCustomFragment.setOnBackFilterDataListener(this);
    List<FilterCommonLinearItem> timeFilterList = new ArrayList<>();
    timeFilterList.add(new FilterCommonLinearItem(
        getResources().getString(R.string.text_filter_not_sign_time, 7)));
    timeFilterList.add(new FilterCommonLinearItem(
        getResources().getString(R.string.text_filter_not_sign_time, 30)));
    timeFilterList.add(new FilterCommonLinearItem("自定义"));
    timeFilterFragment.setItemList(timeFilterList);
    timeFilterFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        switch (position) {
          case 0:
            textNotSignFilterTime.setText(
                getResources().getString(R.string.text_filter_not_sign_time, 7));
            showLoadingTrans();
            shadow.setVisibility(GONE);
            startShadowAnim(false);
            start = DateUtils.minusDay(new Date(), 6);
            end = DateUtils.getStringToday();
            showLoadingTrans();
            presenter.getNotSignStudent(start, end, count);
            break;
          case 1:
            textNotSignFilterTime.setText(
                getResources().getString(R.string.text_filter_not_sign_time, 30));
            showLoadingTrans();
            start = DateUtils.minusDay(new Date(), 29);
            end = DateUtils.getStringToday();
            startShadowAnim(false);
            timeFilterFragment.setFilterAnimation(fragNotSignFilter, false);
            showLoadingTrans();
            presenter.getNotSignStudent(start, end, count);
            break;
          case 2:
            getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                    R.anim.slide_left_in, R.anim.slide_right_out)
                .replace(R.id.frag_not_sign_filter, filterCustomFragment)
                .addToBackStack(null)
                .commit();
            break;
        }
      }
    });
    final List<FilterCommonLinearItem> countFilterList = new ArrayList<>();
    for (int i = 0; i <= 30; i++) {
      countFilterList.add(new FilterCommonLinearItem(
          getResources().getString(R.string.text_filter_not_sign_count, i)));
    }
    countFilterFragment.setItemList(countFilterList);
    countFilterFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        startShadowAnim(false);
        countFilterFragment.setFilterAnimation(fragLayoutCount, false);
        count = position;
        textNotSignFilterCount.setText(
            getResources().getString(R.string.text_filter_not_sign_count, position));
        showLoadingTrans();
        presenter.getNotSignStudent(start, end, count);
      }
    });
  }

  public void onTextSearch(String text) {
    if (adapter.hasNewSearchText(text)) {
      adapter.setSearchText(text);
      adapter.filterItems(itemList, 100);
    }
  }

  @OnClick(R.id.layout_not_sign_time) public void onShowTimeFilter() {
    if (fragNotSignFilter.getLayoutParams().height > MeasureUtils.dpToPx(432f, getResources())) {
      ViewGroup.LayoutParams params = fragNotSignFilter.getLayoutParams();
      params.height = MeasureUtils.dpToPx(432f, getResources());
      fragNotSignFilter.setLayoutParams(params);
    }
    if (fragLayoutCount.getLayoutParams().height > 0){
      countFilterFragment.setFilterAnimation(fragLayoutCount, false);
    }
    if (fragNotSignFilter.getLayoutParams().height == 0){
      startShadowAnim(true);
    }else{
      startShadowAnim(false);
    }
    timeFilterFragment.setFilterAnimation(fragNotSignFilter,
        fragNotSignFilter.getLayoutParams().height == 0);
  }

  private void startShadowAnim(boolean isStart) {

    AlphaAnimation animation;
    if (isStart) {
      shadow.setVisibility(View.VISIBLE);
      animation = new AlphaAnimation(0f, 0.6f);
    } else {
      shadow.setVisibility(GONE);
      animation = new AlphaAnimation(0.6f, 0f);
    }
    animation.setFillAfter(true);
    animation.setDuration(200);
    shadow.startAnimation(animation);
  }

  @OnClick(R.id.layout_not_sign_count) public void onShowCountFilter() {
    if (fragLayoutCount.getLayoutParams().height > MeasureUtils.dpToPx(432f, getResources())) {
      ViewGroup.LayoutParams params = fragLayoutCount.getLayoutParams();
      params.height = MeasureUtils.dpToPx(432f, getResources());
      fragLayoutCount.setLayoutParams(params);
    }
    if (fragNotSignFilter.getLayoutParams().height > 0){
      timeFilterFragment.setFilterAnimation(fragNotSignFilter, false);
    }
    if (fragLayoutCount.getLayoutParams().height == 0){
      startShadowAnim(true);
    }else{
      startShadowAnim(false);
    }
    countFilterFragment.setFilterAnimation(fragLayoutCount,
        fragLayoutCount.getLayoutParams().height == 0);
  }

  @Override public String getFragmentName() {
    return getClass().getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onGetNotSign(List<NotSignStudent> studentList) {
    hideLoadingTrans();
    if (studentList.size() >= 0) {
      itemList.clear();
    }
    if (studentList.size() == 0) {
      itemList.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe,
          getResources().getString(R.string.text_empty_title_not_sign),
          getResources().getString(R.string.text_empty_content_not_sign)));
    }
    for (NotSignStudent student : studentList) {
      itemList.add(new NotSignClassItem(student, new NotSignClassItem.OnClickContactListener() {
        @Override public void onContact(final String phone) {
          new MaterialDialog.Builder(getContext()).autoDismiss(true)
              .content(new StringBuilder().append("确定呼叫号码\n").append(phone).append("吗？").toString())
              .positiveText(R.string.common_comfirm)
              .negativeText(R.string.common_cancel)
              .autoDismiss(true)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull
                    DialogAction which) {

                  new RxPermissions(getActivity()).request(
                      Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                      if (aBoolean) {
                        Uri uri = Uri.parse(new StringBuilder().append("tel:").append(phone).toString());
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
        }
      }));
    }
    adapter.notifyDataSetChanged();
    textFilterTotal.setText(
        getResources().getString(R.string.text_not_sign_tip, DateUtils.interval(start, end) + 1,
            count, studentList.size()));
  }

  @Override public void onSettingData(String start, String end) {
    showLoadingTrans();
    this.start = start;
    this.end = end;
    presenter.getNotSignStudent(start, end, count);
    textNotSignFilterTime.setText(
        start.substring(5, start.length()) + "至" + end.substring(5, end.length()));
  }

  @Override public void onBack() {
    getChildFragmentManager().popBackStackImmediate();
  }
}
