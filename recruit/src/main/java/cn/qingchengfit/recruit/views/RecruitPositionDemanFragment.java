package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.databinding.FragmentPositionWorkExBinding;
import cn.qingchengfit.recruit.event.EventPulishPosition;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fb on 2017/7/3.
 */

@FragmentWithArgs public class RecruitPositionDemanFragment extends BaseFragment {

  private final int MIN_WEIGHT = 30;

  @Arg JobBody jobBody;
  private TwoScrollPicker twoScrollPicker;
  private SimpleScrollPicker simpleScrollPicker;
  //private HashMap<String, Object> map = new HashMap<>();
  FragmentPositionWorkExBinding db;
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitPositionDemanFragmentBuilder.injectArguments(this);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    twoScrollPicker = new TwoScrollPicker(getContext());
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    db = FragmentPositionWorkExBinding.inflate(inflater);
    initView();
    initToolbar();
    setBackPress();
    return db.getRoot();
  }

  private void initToolbar() {
    super.initToolbar(db.layoutToolbar.toolbar);
    ToolbarModel tbm = new ToolbarModel("职位要求");
    tbm.setMenu(R.menu.menu_save);
    db.setToolbarModel(tbm);
    db.layoutToolbar.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxBus.getBus().post(new EventPulishPosition(jobBody));
        getFragmentManager().popBackStackImmediate();
        return false;
      }
    });
  }

  private void initView() {
    db.civWorkExp.setContent(
        RecruitBusinessUtils.getWorkYear(jobBody.min_work_year, jobBody.max_work_year));
    db.civWorkGender.setContent(RecruitBusinessUtils.getGender(jobBody.gender));
    db.civWorkAge.setContent(RecruitBusinessUtils.getAge(jobBody.min_age, jobBody.max_age));
    db.civWorkEducation.setContent(RecruitBusinessUtils.getDegree(getContext(), jobBody.education));
    db.civWorkHeight.setContent(
        RecruitBusinessUtils.getHeight(jobBody.min_height, jobBody.max_height));
    db.civWorkWeight.setContent(
        RecruitBusinessUtils.getWeight(jobBody.min_weight, jobBody.max_weight));

    db.civWorkExp.setOnClickListener(view -> onSelectWorkExp());
    db.civWorkGender.setOnClickListener(view -> onSelectWorkGender());
    db.civWorkAge.setOnClickListener(view -> onSelectWorkAge());
    db.civWorkEducation.setOnClickListener(view -> onSelectWorkEducation());
    db.civWorkHeight.setOnClickListener(view -> onSelectWorkHeight());
    db.civWorkWeight.setOnClickListener(view -> onSelectWorkWeight());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  //选择工作经验
  public void onSelectWorkExp() {
    //String[] workexpStr = getContext().getResources().getStringArray(R.array.work_exp);
    //final ArrayList<String> d = new ArrayList<>(Arrays.asList(workexpStr));
    //twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
    //  @Override public void onSelectItem(int left, int right) {
    //    if ((right <= left && right > 0) || (left == 0 && right > 0)) {
    //      ToastUtils.show("请选择正确的条件区间");
    //      return;
    //    } else {
    //      civWorkExp.setContent(d.get(left) + "-" + d.get(right));
    //      jobBody.min_work_year = left - 1  ;
    //      jobBody.max_work_year = right - 1;
    //    }
    //  }
    //});
    //twoScrollPicker.show(d, d, jobBody.min_work_year + 1, jobBody.max_work_year + 1);//// TODO: 2017/6/8 选择时当前的位置
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.filter_work_year_write)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        db.civWorkExp.setContent(d.get(pos));
        switch (pos) {
          case 1:
            jobBody.min_work_year = 0;
            jobBody.max_work_year = 0;
            break;
          case 2:
            jobBody.min_work_year = 1;
            jobBody.max_work_year = 3;
            break;
          case 3:
            jobBody.min_work_year = 3;
            jobBody.max_work_year = 5;
            break;
          case 4:
            jobBody.min_work_year = 5;
            jobBody.max_work_year = 8;
            break;
          case 5:
            jobBody.min_work_year = 8;
            jobBody.max_work_year = 10;
            break;
          case 6:
            jobBody.min_work_year = 10;
            jobBody.max_work_year = -1;
            break;
          default:
            jobBody.min_work_year = -1;
            jobBody.max_work_year = -1;
            break;
        }
      }
    });
    int po = 0;
    if (jobBody != null && jobBody.min_work_year != null) {
      switch (jobBody.min_work_year) {
        case 0:
          po = 1;
          break;
        case 1:
          po = 2;
          break;
        case 3:
          po = 3;
          break;
        case 5:
          po = 4;
          break;
        case 8:
          po = 5;
          break;
        case 10:
          po = 6;
          break;
        default:
          po = 0;
          break;
      }
    }
    simpleScrollPicker.show(d, po);
  }

  //选择性别
  public void onSelectWorkGender() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.filter_gender)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        db.civWorkGender.setContent(d.get(pos));
        jobBody.gender = pos - 1;
      }
    });
    simpleScrollPicker.show(d, jobBody.gender + 1);
  }

  //选择年龄
  public void onSelectWorkAge() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 100; i++) {
      d.add(i + "岁");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left != 0 && right != 0 && left >= right) {
          ToastUtils.show("请选择正确的条件区间");
          return;
        }
        jobBody.min_age = left - 1;
        jobBody.max_age = right - 1;
        db.civWorkAge.setContent(RecruitBusinessUtils.getAge(jobBody.min_age, jobBody.max_age));
      }
    });
    twoScrollPicker.show(d, d, jobBody.min_age + 1, jobBody.max_age + 1);
  }

  //选择学历
  public void onSelectWorkEducation() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        db.civWorkEducation.setContent(d.get(pos));
        if (pos == 0) {
          jobBody.education = pos - 1;
        } else {
          jobBody.education = pos;
        }
      }
    });
    simpleScrollPicker.show(d, jobBody.education);
  }

  //选择身高
  public void onSelectWorkHeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i <= 60; i++) {
      d.add(i + 140 + "cm");
    }

    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left != 0 && right != 0 && left >= right) {
          ToastUtils.show("请选择正确的条件区间");
        } else {
          if (left == 0){
            jobBody.min_height = -1f;
          }else {
            jobBody.min_height = left + 139f;
          }
          if (right == 0){
            jobBody.max_height = -1f;
          }else {
            jobBody.max_height = right + 139f;
          }
          db.civWorkHeight.setContent(
              RecruitBusinessUtils.getHeight(jobBody.min_height, jobBody.max_height));
        }
      }
    });
    if (TextUtils.isEmpty(jobBody.name)){
      twoScrollPicker.show(d, d, jobBody.min_height.intValue() + 22 , jobBody.max_height.intValue() + 42);
    }else {
      twoScrollPicker.show(d, d, jobBody.min_height.intValue(), jobBody.max_height.intValue());
    }
  }

  //选择体重
  public void onSelectWorkWeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");

    for (int i = 0; i <= 60; i++) {
      d.add(i + MIN_WEIGHT + "kg");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left != 0 && right != 0 && left >= right) {
          ToastUtils.show("请选择正确的条件区间");
        } else {
          if (left == 0){
            jobBody.min_weight = -1f;
          }else {
            jobBody.min_weight = left + MIN_WEIGHT -1f;
          }
          if (right == 0){
            jobBody.max_weight = -1f;
          }else {
            jobBody.max_weight = right + MIN_WEIGHT -1f;
          }
          db.civWorkWeight.setContent(
              RecruitBusinessUtils.getWeight(jobBody.min_weight, jobBody.max_weight));
        }
      }
    });
    if (TextUtils.isEmpty(jobBody.name)){
      twoScrollPicker.show(d, d, jobBody.min_weight.intValue() + 10 , jobBody.max_weight.intValue() + 40);
    }else {
      twoScrollPicker.show(d, d, jobBody.min_weight.intValue(), jobBody.max_weight.intValue());
    }
  }

  @Override public boolean onFragmentBackPress() {
    if (jobBody != null && !jobBody.isPositionDemanEmpty()) {
      DialogUtils.instanceDelDialog(getContext(), "确定放弃所做修改？",
          new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              dialog.dismiss();
              getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
          }).show();
      return true;
    } else {
      return false;
    }
  }

}
