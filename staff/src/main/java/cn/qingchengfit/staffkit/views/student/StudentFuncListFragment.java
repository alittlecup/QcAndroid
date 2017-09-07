package cn.qingchengfit.staffkit.views.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.student.model.FuncIconModel;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by fb on 2017/9/6.
 */

public class StudentFuncListFragment extends BaseFragment{

  @BindView(R.id.viewpager) ViewPager viewpager;
  @BindView(R.id.indicator) CircleIndicator indicator;
  @Inject GymWrapper gymWrapper;
  private ArrayList<AbstractFlexibleItem> datas = new ArrayList<>();
  private ArrayList<Fragment> fs = new ArrayList<>();
  private FragmentAdapter adapter;

  private boolean proGym = false;

  private ArrayList<FuncIconModel> funList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_student_operation, container, false);
    unbinder = ButterKnife.bind(this, view);
    adapter = new FragmentAdapter(getChildFragmentManager(), fs);
    viewpager.setAdapter(adapter);
    initView();
    return view;
  }

  private void initView(){
    RxRegiste(GymBaseInfoAction.getGymByModel(gymWrapper.id(), gymWrapper.model())
        .filter(new Func1<List<CoachService>, Boolean>() {
          @Override public Boolean call(List<CoachService> list) {
            return list != null && list.size() > 0;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<CoachService>>() {
          @Override public void call(List<CoachService> list) {
            CoachService now = list.get(0);
            proGym = GymUtils.getSystemEndDay(now) >= 0;
            datas.clear();
            funList.add(new FuncIconModel(R.string.qc_student_allotsale, R.drawable.vector_student_management_sales, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_allot_coach, R.drawable.vector_student_management_coach, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_follow_up, R.drawable.vector_student_management_follow, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_follow_transfer, R.drawable.vd_student_transfer, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_attendance, R.drawable.vector_student_management_attend, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_send_sms, R.drawable.vector_student_send_sms, proGym, true));
            funList.add(new FuncIconModel(R.string.fun_name_import_export, R.drawable.vector_student_send_sms, proGym, true));
            funList.add(new FuncIconModel(R.string.qc_student_birthday_notice, R.drawable.vector_student_management_birthday, proGym, false));
            funList.add(new FuncIconModel(R.string.qc_student_vip, R.drawable.vector_student_management_tag, proGym, false));
            checkFunList();
          }
        }));
  }

  public void checkFunList(){
    ArrayList<FuncIconModel> tempList = new ArrayList<>();
    while (funList.size() > 8){
      fs.add(StudentOperationFragment.newInstance(new ArrayList<FuncIconModel>(funList.subList(0,8))));
      tempList.addAll(funList);
      funList.clear();
      funList.addAll(tempList.subList(8, tempList.size()));
      tempList.clear();
    }
    if (funList.size() <= 8){
      fs.add(StudentOperationFragment.newInstance(funList));
    }
    adapter.notifyDataSetChanged();
    indicator.setViewPager(viewpager);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
