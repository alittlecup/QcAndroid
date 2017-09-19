package cn.qingchengfit.staffkit.views.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.model.CardTypeWrapper;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.BuyCardNextEvent;
import cn.qingchengfit.staffkit.views.card.buy.CompletedBuyFragment;
import cn.qingchengfit.staffkit.views.card.buy.RealCardBuyFragment;
import cn.qingchengfit.staffkit.views.student.MutiChooseStudentFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.LinkedList;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 16/4/2 2016.
 */
public class BuyCardActivity extends BaseActivity implements FragCallBack {
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.frag) FrameLayout frag;
    @BindView(R.id.down) ImageView down;

    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @Inject CardTypeWrapper cardTypeWrapper;
    private Observable<BuyCardNextEvent> ObStep;
    private CreateCardBody buyCardBody; //
    private CardTpl card_tpl;      //选中的卡种类

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        buyCardBody = new CreateCardBody();
        Intent it = getIntent();
        card_tpl = it.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
        cardTypeWrapper.setCardTpl(card_tpl);
        String stuId = it.getStringExtra(Configs.EXTRA_STUDENT_ID);
        buyCardBody.card_tpl_id = card_tpl.getId();
        if (!TextUtils.isEmpty(stuId)) buyCardBody.user_ids = stuId;

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.frag, MutiChooseStudentFragment.newInstance(buyCardBody.user_ids))
            .commit();

        ObStep = RxBus.getBus().register(BuyCardNextEvent.class);
        ObStep.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BuyCardNextEvent>() {
            @Override public void call(BuyCardNextEvent buyCardNextEvent) {
                if (buyCardNextEvent.step == 1) {//选择会员
                    buyCardBody.user_ids = buyCardNextEvent.info;
                    buyCardBody.user_name = buyCardNextEvent.info2;
                    getSupportFragmentManager().beginTransaction().add(getFragId(), new RealCardBuyFragment()).addToBackStack("").commit();
                } else if (buyCardNextEvent.step == 2) {
                    getSupportFragmentManager().beginTransaction().add(getFragId(), new CompletedBuyFragment()).addToBackStack("").commit();
                }
            }
        });
    }

    @Override protected void onDestroy() {
        cardTypeWrapper.setCardTpl(null);
        RxBus.getBus().unregister(BuyCardNextEvent.class.getName(), ObStep);
        super.onDestroy();
    }

    public CreateCardBody getBuyCardBody() {
        return buyCardBody;
    }

    public void setBuyCardBody(CreateCardBody buyCardBody) {
        this.buyCardBody = buyCardBody;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {//选择会员卡类型

            }
        }
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        toolbarList.clear();
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            toolbarTitile.setOnClickListener(bar.onClickListener);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void onBackPressed() {
        if (toolbarList.size() > 1) {
            toolbarList.removeLast();
            setBar(toolbarList.getLast());
        }

        super.onBackPressed();
    }
}
