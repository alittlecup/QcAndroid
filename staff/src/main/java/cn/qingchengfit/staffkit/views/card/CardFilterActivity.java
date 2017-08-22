package cn.qingchengfit.staffkit.views.card;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.staffkit.views.card.filter.FilterFragment;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/18 2016.
 */
public class CardFilterActivity extends AppCompatActivity implements FragCallBack {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_no_toolbar);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.student_frag,
                FilterFragment.newInstance(getIntent().getStringExtra("cardtpl_id"), getIntent().getStringExtra("cardtpl_name"),
                    getIntent().getIntExtra("card_status", 0), (CoachService) getIntent().getParcelableExtra(Configs.EXTRA_GYM_SERVICE),
                    (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND)

                ))
            .commit();
    }

    @Override public int getFragId() {
        return R.id.student_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }

    //    @OnClick({R.id.card_name, R.id.card_status, R.id.menu_comfirm, R.id.clear_option})
    //    public void onClick(View view) {
    //        switch (view.getId()) {
    //            case R.id.card_name:
    //                //选择会员卡
    //                break;
    //            case R.id.card_status:
    //
    //                break;
    //            case R.id.menu_comfirm:
    //                setResult(RESULT_OK, IntentUtils.instanceStringIntent("筛选数据"));
    //                break;
    //            case R.id.clear_option:
    //                cardName.setContent(getString(R.string.common_please_choose));
    //                cardStatus.setContent(getString(R.string.student_card_status_all));
    //                break;
    //        }
    //    }
    //
}
