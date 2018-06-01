package cn.qingchengfit.saasbase.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.article.presenter.ArticleReplyPresenter;
import cn.qingchengfit.saasbase.items.ArticleReplyItem;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * Created by Paper on 2017/4/13.
 */
public class ArticleReplyFragment extends BaseFragment
  implements ArticleReplyPresenter.MVPView, FlexibleAdapter.EndlessScrollListener {

	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout toolbarLayout;
	RecyclerView recyclerview;

  @Inject ArticleReplyPresenter presenter;

  private List<AbstractFlexibleItem> items = new ArrayList<>();
  private CommonFlexAdapter commonFlexAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_article_reply, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    commonFlexAdapter = new CommonFlexAdapter(items);
    commonFlexAdapter.setEndlessScrollListener(this, new ProgressItem(getContext()));

    recyclerview.setAdapter(commonFlexAdapter);

    RxBusAdd(EventRecycleClick.class).subscribe(eventRecycleClick -> {
      if (commonFlexAdapter.getItem(eventRecycleClick.postion) instanceof ArticleReplyItem) {
        ArticleReplyItem item =
          (ArticleReplyItem) commonFlexAdapter.getItem(eventRecycleClick.postion);

        if (eventRecycleClick.viewId == R.id.btn_reply) {
          try {
            getFragmentManager().beginTransaction()
              .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_fade_out,
                R.anim.slide_fade_in, R.anim.slide_right_out)
              .replace(mCallbackActivity.getFragId(),
                ArticleCommentsListFragment.newInstance(item.getArticleComment().news.id,
                  item.getArticleComment().id, item.getArticleComment().user.username))
                  .addToBackStack(null)
                  .commit();
          } catch (Exception e) {
            CrashUtils.sendCrash(e);
          }
        } else {
          if (item != null
            && item.getArticleComment() != null
            && item.getArticleComment().news != null
            && !TextUtils.isEmpty(item.getArticleComment().news.id)) {
            WebActivity.startWeb(item.getArticleComment().news.url, getContext());
          }
        }
      }
    }); presenter.queryReplies(1);
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
    if (list != null) {
      if (page == 1) items.clear();
      for (int i = 0; i < list.size(); i++) {
        items.add(new ArticleReplyItem(list.get(i)));
      }
      commonFlexAdapter.updateDataSet(items);
    }
  }

  @Override public void onTotalCount(int count, int pages) {
    commonFlexAdapter.setEndlessPageSize(pages);
  }

  @Override public void noMoreLoad(int i) {
    ToastUtils.show(getString(R.string.no_more_loading));
  }

  @Override public void onLoadMore(int i, int i1) {
    if (commonFlexAdapter.getItem(commonFlexAdapter.getItemCount() - 1) instanceof ProgressItem) {
      commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount() - 1);
    }
    presenter.queryReplies(i1);
  }
}
