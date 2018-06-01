package cn.qingchengfit.staffkit.views.student.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.ScoreHistory;
import cn.qingchengfit.model.responese.StudentScoreHistoryBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.student.score.item.HistoryItem;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2016/12/26.
 */
public class ScoreDetailFragment extends BaseFragment implements ScoreDetailPresenter.PresenterView {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentBean;
    @Inject ScoreDetailPresenter presenter;
    @Inject ScoreModifyFragment scoreModifyFragment;

	TextView userName;
	TextView tvScore;
	RecycleViewWithNoImg recycleview;
    private String score;
    private CommonFlexAdapter flexibleAdapter;
    private List<AbstractFlexibleItem> items = new ArrayList();
    private List<StudentScoreHistoryBean> datas = new ArrayList<>();

    @Inject public ScoreDetailFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score_detail, container, false);
      userName = (TextView) view.findViewById(R.id.user_name);
      tvScore = (TextView) view.findViewById(R.id.score);
      recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recycleview);
      view.findViewById(R.id.ll_student_score_sub).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ScoreDetailFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.ll_student_score_plus).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ScoreDetailFragment.this.onClick(v);
        }
      });

      delegatePresenter(presenter, this);
        initTitle();
        initView();
        presenter.getScore();
        return view;
    }

    private void initTitle() {
        mCallbackActivity.setToolbar("积分详情", false, null, 0, null);
    }

    private void initView() {

        userName.setText(studentBean.username());
        //

        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        flexibleAdapter = new CommonFlexAdapter(items, this);
        recycleview.setAdapter(flexibleAdapter);
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.getScoreHistory();
            }
        });
    }

    @Override public String getFragmentName() {
        return ScoreDetailFragment.class.getName();
    }

 public void onClick(View view) {
        scoreModifyFragment.setCurScore(score);
        switch (view.getId()) {
            case R.id.ll_student_score_sub:
                scoreModifyFragment.setTargetFragment(this, 11);
                scoreModifyFragment.type = ScoreModifyFragment.TYPE_SUB;
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), scoreModifyFragment)
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.ll_student_score_plus:
                scoreModifyFragment.setTargetFragment(this, 12);
                scoreModifyFragment.type = ScoreModifyFragment.TYPE_PLUS;
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), scoreModifyFragment)
                    .addToBackStack(null)
                    .commit();
                break;
        }
    }

    @Override public void onHsitory(List<ScoreHistory> scoreHistories) {
        hideLoading();
        recycleview.stopLoading();
        datas.clear();
        items.clear();
        if (scoreHistories != null && !scoreHistories.isEmpty()) {
            for (ScoreHistory scoreHistory : scoreHistories) {
                datas.add(scoreHistory.toStudentScoreHistoryBean());
                items.add(new HistoryItem(scoreHistory.toStudentScoreHistoryBean()));
            }
        }
        flexibleAdapter.updateDataSet(items);
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        recycleview.stopLoading();
        ToastUtils.show(e);
    }

    @Override public void onStudentScore(String score) {
        this.score = score;
        tvScore.setText(score);
        presenter.getScoreHistory();
    }

    @Override public void onStudentScoreFail(String s) {
        hideLoading();
        recycleview.stopLoading();
        ToastUtils.show(s);
    }
}
