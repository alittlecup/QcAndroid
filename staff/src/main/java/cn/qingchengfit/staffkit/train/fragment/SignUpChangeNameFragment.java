package cn.qingchengfit.staffkit.train.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import javax.inject.Inject;

/**
 * Created by fb on 2017/3/23.
 */
@FragmentWithArgs public class SignUpChangeNameFragment extends BaseFragment implements SignUpView {

	EditText editGroupName;
	ImageView imageClearName;
	Toolbar toolbar;

    @Inject SignUpGroupDetailPresenter presenter;
	TextView toolbarTitile;

    @Arg String teamName;
    @Arg String teamId;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_change_name, container, false);
      editGroupName = (EditText) view.findViewById(R.id.edit_group_name);
      imageClearName = (ImageView) view.findViewById(R.id.image_clear_name);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.edit_group_name).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onEdit(v);
        }
      });
      view.findViewById(R.id.image_clear_name).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onEdit(v);
        }
      });

      delegatePresenter(presenter, this);
        setToolbar();
        initEdit();

        imageClearName.setVisibility(View.GONE);
        editGroupName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString().trim())) {
                    imageClearName.setVisibility(View.VISIBLE);
                } else {
                    imageClearName.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void initEdit() {
        editGroupName.setText(teamName);
        editGroupName.setTextColor(getResources().getColor(R.color.qc_text_grey));
    }

    private void setToolbar() {
        initToolbar(toolbar);
        toolbarTitile.setText("组名");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (editGroupName.getText().toString().equals("")) {
                    DialogUtils.showAlert(getContext(), "请填写群名称");
                    return false;
                } else {
                    presenter.operationGroup(teamId, editGroupName.getText().toString(), null);
                }
                return false;
            }
        });
    }

 public void onEdit(View view) {
        switch (view.getId()) {
            case R.id.image_clear_name:
                editGroupName.setText("");
                initEdit();
                break;
        }
    }

    @Override public String getFragmentName() {
        return SignUpChangeNameFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onGetSignUpDataSuccess(Object data) {

    }

    @Override public void onFailed(String msg) {
        super.onShowError(msg);
    }

    @Override public void onSuccess() {
        ToastUtils.show("修改成功");
        getActivity().onBackPressed();
    }

    @Override public void onDelSuccess() {

    }
}
