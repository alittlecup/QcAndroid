package cn.qingchengfit.staffkit.views.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;

/**
 * Created by Paper on 16/6/16.
 * <p/>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public class FixRealcardNumFragment extends BaseFragment implements CommonPView {

    @Inject RealcardWrapper realCard;

    @Inject FixRealcardNumPresenter presenter;
    @BindView(R.id.real_card_no) CommonInputView realCardNo;

    @Inject public FixRealcardNumFragment() {
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fix_cardnum, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        mCallbackActivity.setToolbar("修改实体卡号", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                showLoading();
                presenter.fixCardNum(realCardNo.getContent().trim());
                return true;
            }
        });

        realCardNo.setContent(realCard.getRealCard().getCard_no());
        AppUtils.showKeyboard(getContext(), realCardNo.getEditText());
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboardFore(getContext());
        presenter.unattachView();
        super.onDestroyView();
    }

    @OnClick(R.id.clear) public void onClick() {
        realCardNo.setContent("");
    }

    @Override public void onSuccess() {
        hideLoading();
        realCard.getRealCard().setCard_no(realCardNo.getContent().trim());
        getActivity().onBackPressed();
    }

    @Override public void onFailed(String s) {
        hideLoading();
    }
}
