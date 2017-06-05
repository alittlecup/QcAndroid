package cn.qingchengfit.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.ArticleCommentItem;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.presenters.ArticleCommentsPresenter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.LoginActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
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
@FragmentWithArgs public class ArticleCommentsListFragment extends BaseFragment
    implements ArticleCommentsPresenter.MVPView, FlexibleAdapter.EndlessScrollListener, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    @Inject ArticleCommentsPresenter presenter;
    @BindView(R.id.tv_total_count) TextView tvTotalCount;
    @BindView(R.id.input_et) EditText inputEt;
    @BindView(R.id.btn_send) Button btnSend;
    LinearLayoutManager linearLayoutManager;
    @Arg String newsId ;
    @Arg(required = false) String replyId ;
    @Arg(required = false) String replyName;
    @Inject LoginStatus loginStatus;
    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private CommonFlexAdapter commonFlexAdapter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_comments, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        commonFlexAdapter = new CommonFlexAdapter(items, this);
        commonFlexAdapter.setEndlessScrollListener(this, new ProgressItem(getContext()));
        recyclerview.setAdapter(commonFlexAdapter);

        //presenter.queryCommenList(newsId, 1);
        RxTextView.textChangeEvents(inputEt).subscribe(new Action1<TextViewTextChangeEvent>() {
            @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                btnSend.setEnabled(textViewTextChangeEvent.text().toString().trim().length() > 0);
            }
        });
        inputEt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(inputEt.getViewTreeObserver(), this);
                if (TextUtils.isEmpty(replyId)) {
                    inputEt.setHint("评论：");
                    inputEt.requestFocus();
                    AppUtils.showKeyboard(getContext(), inputEt);
                } else {
                    inputEt.setHint("回复" + replyName + ":");
                    inputEt.requestFocus();
                    AppUtils.showKeyboard(getContext(), inputEt);
                }
            }
        });
        if (loginStatus.isLogined())
            presenter.queryCommenList(newsId, 1);
        else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class),1);
        }
        return view;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (loginStatus.isLogined()){
                presenter.queryCommenList(newsId, 1);
            }else getActivity().finish();
        }
    }

    @Override public void initToolbar(@NonNull Toolbar tobar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.t_article_comment_list);
    }

    @Override public String getFragmentName() {
        return ArticleCommentsListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onAddCommentSuccess() {
        inputEt.setText("");
        AppUtils.hideKeyboard(getActivity());
        presenter.queryCommenList(newsId, 1);
    }

    @Override public void onCommentTotal(int count, int pages) {
        tvTotalCount.setText(getString(R.string.all_comments_count, count));
        commonFlexAdapter.setEndlessPageSize(pages);
    }

    @Override public void onCommentList(List<ArticleComment> comments, int curPage) {
        if (comments != null) {
            if (curPage == 1) items.clear();
            for (int i = 0; i < comments.size(); i++) {
                items.add(new ArticleCommentItem(comments.get(i)));
            }
            if (commonFlexAdapter.getItemCount() == 0) {
                commonFlexAdapter.addItem(new CommonNoDataItem(R.drawable.vd_no_comment, "还没人评论，快来抢沙发"));
            }

            commonFlexAdapter.notifyDataSetChanged();
            if (curPage == 1) linearLayoutManager.scrollToPosition(0);
        }
    }

    @Override public void noMoreLoad(int i) {
        ToastUtils.show(getString(R.string.no_more_loading));
    }

    @Override public void onLoadMore(int i, int i1) {
        if (commonFlexAdapter.getItem(commonFlexAdapter.getItemCount() - 1) instanceof ProgressItem) {
            commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount() - 1);
        }
        presenter.queryCommenList(newsId, i1);
    }

    /**
     * 发送评论
     */
    @OnClick(R.id.btn_send) public void onViewClicked() {
        presenter.addComment(newsId, replyId, inputEt.getText().toString().trim());
    }

    @Override public boolean onItemClick(int i) {
        if (commonFlexAdapter.getItem(i) instanceof ArticleCommentItem) {
            ArticleComment articleComment = ((ArticleCommentItem) commonFlexAdapter.getItem(i)).getComment();
            if (articleComment.user != null) {
                inputEt.setHint("回复" + articleComment.user.getUsername() + "：");
                replyId = articleComment.id;
                inputEt.requestFocus();
                AppUtils.showKeyboard(getContext(), inputEt);
            }
        }
        return false;
    }

    @Override public void onDetach() {
        Intent ret = new Intent();
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getData() != null) {
            String aciton = getActivity().getIntent().getData().getHost() + getActivity().getIntent().getData().getPath();
            ret.putExtra("web_action", aciton);
            getActivity().setResult(Activity.RESULT_OK, ret);
        }
        super.onDetach();
    }
}
