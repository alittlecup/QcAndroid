package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventPulishPosition;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
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

  @BindView(R2.id.civ_work_exp) CommonInputView civWorkExp;
  @BindView(R2.id.civ_work_gender) CommonInputView civWorkGender;
  @BindView(R2.id.civ_work_age) CommonInputView civWorkAge;
  @BindView(R2.id.civ_work_education) CommonInputView civWorkEducation;
  @BindView(R2.id.civ_work_height) CommonInputView civWorkHeight;
  @BindView(R2.id.civ_work_weight) CommonInputView civWorkWeight;
  @Arg JobBody jobBody;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  private TwoScrollPicker twoScrollPicker;
  private SimpleScrollPicker simpleScrollPicker;
  //private HashMap<String, Object> map = new HashMap<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitPositionDemanFragmentBuilder.injectArguments(this);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    twoScrollPicker = new TwoScrollPicker(getContext());
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_position_work_ex, container, false);
    unbinder = ButterKnife.bind(this, view);
    initView();
    initToolbar();
    setBackPress();
    return view;
  }

  private void initToolbar() {
    super.initToolbar(toolbar);
    toolbarTitle.setText("职位要求");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxBus.getBus().post(new EventPulishPosition(jobBody));
        getActivity().onBackPressed();
        return false;
      }
    });
  }

  private void initView() {
    civWorkExp.setContent(
        RecruitBusinessUtils.getWorkYear(jobBody.min_work_year, jobBody.max_work_year));
    civWorkGender.setContent(RecruitBusinessUtils.getGender(jobBody.gender));
    civWorkAge.setContent(RecruitBusinessUtils.getAge(jobBody.min_age, jobBody.max_age));
    civWorkEducation.setContent(RecruitBusinessUtils.getDegree(getContext(), jobBody.education));
    civWorkHeight.setContent(
        RecruitBusinessUtils.getHeight(jobBody.min_height, jobBody.max_height));
    civWorkWeight.setContent(
        RecruitBusinessUtils.getWeight(jobBody.min_weight, jobBody.max_weight));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  //选择工作经验
  @OnClick(R2.id.civ_work_exp) public void onSelectWorkExp() {
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
        civWorkExp.setContent(d.get(pos));
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
  @OnClick(R2.id.civ_work_gender) public void onSelectWorkGender() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.filter_gender)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWorkGender.setContent(d.get(pos));
        jobBody.gender = pos - 1;
      }
    });
    simpleScrollPicker.show(d, jobBody.gender + 1);
  }

  //选择年龄
  @OnClick(R2.id.civ_work_age) public void onSelectWorkAge() {
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
        civWorkAge.setContent(RecruitBusinessUtils.getAge(jobBody.min_age, jobBody.max_age));
      }
    });
    twoScrollPicker.show(d, d, jobBody.min_age + 1, jobBody.max_age + 1);
  }

  //选择学历
  @OnClick(R2.id.civ_work_education) public void onSelectWorkEducation() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWorkEducation.setContent(d.get(pos));
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
  @OnClick(R2.id.civ_work_height) public void onSelectWorkHeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "cm");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left != 0 && right != 0 && left >= right) {
          ToastUtils.show("请选择正确的条件区间");
        } else {
          jobBody.min_height = left - 1f;
          jobBody.max_height = right - 1f;
          civWorkHeight.setContent(
              RecruitBusinessUtils.getHeight(jobBody.min_height, jobBody.max_height));
        }
      }
    });
    twoScrollPicker.show(d, d, jobBody.min_height.intValue() + 1,
        jobBody.max_height.intValue() + 1);
  }

  //选择体重
  @OnClick(R2.id.civ_work_weight) public void onSelectWorkWeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "Kg");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left != 0 && right != 0 && left >= right) {
          ToastUtils.show("请选择正确的条件区间");
        } else {
          jobBody.min_weight = left - 1f;
          jobBody.max_weight = right - 1f;
          civWorkWeight.setContent(
              RecruitBusinessUtils.getWeight(jobBody.min_weight, jobBody.max_weight));
        }
      }
    });
    twoScrollPicker.show(d, d, jobBody.min_weight.intValue() + 1,
        jobBody.max_weight.intValue() + 1);
  }
}
