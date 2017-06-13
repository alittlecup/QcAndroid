package cn.qingchengfit.staffkit.views.student.sendmsgs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.presenters.ShortMsgPresentersPresenter;
import cn.qingchengfit.staffkit.views.FlexableListFragment;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ShortMsgItem;
import cn.qingchengfit.utils.CompatUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/3/14.
 *
 *
 * {@link }
 */
public class SendMsgHomeFragment extends BaseFragment implements ShortMsgPresentersPresenter.MVPView {

    @BindView(R.id.tabview) TabLayout tabview;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.et_search) EditText etSearch;

    FlexableListFragment allFragment;
    FlexableListFragment sendedFragment;
    FlexableListFragment scriptFragment;

    List<AbstractFlexibleItem> mDatasAll = new ArrayList<>();
    List<AbstractFlexibleItem> mDatasScript = new ArrayList<>();
    List<AbstractFlexibleItem> mDatasSended = new ArrayList<>();

    @Inject ShortMsgPresentersPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    SendMsgHomeAdapter adapter;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_msg_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        if (allFragment == null) {
            allFragment = new FlexableListFragment();
            allFragment.customNoStr = "点击右下方按钮新建短信";
            allFragment.customNoStrTitle = "暂无短信记录";
            allFragment.customNoImage = R.drawable.img_empty_msg;
        }
        if (sendedFragment == null) {
            sendedFragment = new FlexableListFragment();
            sendedFragment.customNoStr = "点击右下方按钮新建短信";
            sendedFragment.customNoStrTitle = "暂无短信记录";
            sendedFragment.customNoImage = R.drawable.img_empty_msg;
        }
        if (scriptFragment == null) {
            scriptFragment = new FlexableListFragment();
            scriptFragment.customNoStr = "点击右下方按钮新建短信";
            scriptFragment.customNoStrTitle = "暂无短信记录";
            scriptFragment.customNoImage = R.drawable.img_empty_msg;
        }

        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        etSearch.setHint(getString(R.string.search_sms_hint));
        RxTextView.afterTextChangeEvents(etSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                    sendedFragment.setFilterString(etSearch.getText().toString().trim());
                    allFragment.setFilterString(etSearch.getText().toString().trim());
                    scriptFragment.setFilterString(etSearch.getText().toString().trim());

                    presenter.queryShortMsgList(null, etSearch.getText().toString().trim());
                    searchviewClear.setVisibility(etSearch.getText().toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
                }
            });
        RxView.clicks(searchviewClear).subscribe(new Action1<Void>() {
            @Override public void call(Void aVoid) {
                etSearch.setText("");
            }
        });
        allFragment.setItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int position) {
                if (mDatasAll.get(position) instanceof ShortMsgItem) {
                    ShortMsgItem item = (ShortMsgItem) mDatasAll.get(position);
                    getFragmentManager().beginTransaction()
                        .replace(R.id.frag, new ShortMsgDetailFragmentBuilder(item.getShortMsg()).build())
                        .addToBackStack(null)
                        .commit();
                }
                return false;
            }
        });
        sendedFragment.setItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int position) {
                if (mDatasSended.get(position) instanceof ShortMsgItem) {
                    ShortMsgItem item = (ShortMsgItem) mDatasSended.get(position);
                    getFragmentManager().beginTransaction()
                        .replace(R.id.frag, new ShortMsgDetailFragmentBuilder(item.getShortMsg()).build())
                        .addToBackStack(null)
                        .commit();
                }
                return false;
            }
        });
        scriptFragment.setItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int position) {
                if (mDatasScript.get(position) instanceof ShortMsgItem) {
                    ShortMsgItem item = (ShortMsgItem) mDatasScript.get(position);
                    getFragmentManager().beginTransaction()
                        .replace(R.id.frag, new ShortMsgDetailFragmentBuilder(item.getShortMsg()).build())
                        .addToBackStack(null)
                        .commit();
                }
                return false;
            }
        });
        viewpager.setOffscreenPageLimit(2);
        if (adapter == null) adapter = new SendMsgHomeAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabview.setupWithViewPager(viewpager);
        tabview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(tabview.getViewTreeObserver(), this);
                presenter.queryShortMsgList(null, null);
            }
        });

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("群发短信");
    }

    @Override protected void onVisible() {
        super.onVisible();
    }

    @Override public String getFragmentName() {
        return SendMsgHomeFragment.class.getName();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onShortMsgList(List<ShortMsg> list) {
        if (list != null) {
            mDatasAll.clear();
            mDatasScript.clear();
            mDatasSended.clear();
            for (int i = 0; i < list.size(); i++) {
                ShortMsg s = list.get(i);
                if (s.status == 1) {
                    mDatasSended.add(new ShortMsgItem(s));
                } else {
                    mDatasScript.add(new ShortMsgItem(s));
                }
                mDatasAll.add(new ShortMsgItem(s));
            }
            allFragment.setData(mDatasAll);
            sendedFragment.setData(mDatasSended);
            scriptFragment.setData(mDatasScript);
        }
    }

    @Override public void onShortMsgDetail(ShortMsg detail) {

    }

    @Override public void onPostSuccess() {

    }

    @Override public void onPutSuccess() {

    }

    @Override public void onDelSuccess() {

    }

    @OnClick(R.id.fab_add) public void addMsg() {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_fade_out, R.anim.slide_fade_in, R.anim.slide_bottom_out)
            .replace(R.id.frag, new MsgSendFragmentFragment())
            .addToBackStack(null)
            .commit();
    }

    class SendMsgHomeAdapter extends FragmentStatePagerAdapter {

        public SendMsgHomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return allFragment;
                case 1:
                    return sendedFragment;
                default:
                    return scriptFragment;
            }
        }

        @Override public int getCount() {
            return 3;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "全部";
                case 1:
                    return "已发送";
                default:
                    return "草稿箱";
            }
        }
    }
}
