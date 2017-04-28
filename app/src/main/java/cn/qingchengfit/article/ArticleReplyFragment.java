package cn.qingchengfit.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.ArticleReplyItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.presenters.ArticleReplyPresenter;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.event.EventRecycleClick;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/4/13.
 */
public class ArticleReplyFragment extends BaseFragment implements ArticleReplyPresenter.MVPView,FlexibleAdapter.EndlessScrollListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;


    @Inject ArticleReplyPresenter presenter;

    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private CommonFlexAdapter commonFlexAdapter;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_reply, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter,this);
        initToolbar(toolbar);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        commonFlexAdapter = new CommonFlexAdapter(items);
        commonFlexAdapter.setEndlessScrollListener(this,new ProgressItem(getContext()));
        recyclerview.setAdapter(commonFlexAdapter);
        RxBusAdd(EventRecycleClick.class)
            .subscribe(new Action1<EventRecycleClick>() {
                @Override public void call(EventRecycleClick eventRecycleClick) {
                    if (commonFlexAdapter.getItem(eventRecycleClick.postion) instanceof ArticleReplyItem) {
                        ArticleReplyItem item = (ArticleReplyItem)commonFlexAdapter.getItem(eventRecycleClick.postion);

                        switch (eventRecycleClick.viewId) {
                            case R.id.btn_reply:
                                try {
                                    getFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_fade_out, R.anim.slide_fade_in, R.anim.slide_right_out)
                                        .replace(R.id.frag,
                                            new ArticleCommentsListFragmentBuilder(item.getArticleComment().news.id).replyId(item.getArticleComment().id)
                                                .replyName(item.getArticleComment().user.username)
                                                .build())
                                        .addToBackStack(null)
                                        .commit();
                                }catch (Exception e){
                                    CrashUtils.sendCrash(e);
                                }
                                break;
                            default://跳去文章
                                WebActivity.startWeb(item.getArticleComment().url,getActivity());
                                break;
                        }
                    }
                }
            });
        presenter.queryReplies(1);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.t_article_reply_list);
    }

    @Override public String getFragmentName() {
        return ArticleReplyFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onArticleReplies(List<ArticleComment> list, int page) {
        if (list != null){
            if (page == 1)
                items.clear();
            for (int i = 0; i < list.size(); i++) {
                items.add(new ArticleReplyItem(list.get(i)));
            }
            commonFlexAdapter.notifyDataSetChanged();
        }
    }

    @Override public void onTotalCount(int count, int pages) {
        commonFlexAdapter.setEndlessPageSize(pages);
    }

    @Override public void noMoreLoad(int i) {
        ToastUtils.show(getString(R.string.no_more_loading));
    }

    @Override public void onLoadMore(int i, int i1) {
        if (commonFlexAdapter.getItem(commonFlexAdapter.getItemCount()-1) instanceof  ProgressItem)
            commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount()-1);
        presenter.queryReplies(i1);
    }
}
