package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.staffkit.views.custom.LargeInputView;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.widgets.CommonInputView;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/2/4 2016.
 */
public class AddAccountTypeFragment extends BaseDialogFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.name) CommonInputView name;
    @BindView(R.id.type) CommonInputView type;
    @BindView(R.id.instruction) LargeInputView instruction;
    @BindView(R.id.comfirm) Button comfirm;
    DialogSheet sheet;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private int cardtype = 1;
    private TextWatcher textChange = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            comfirm.setEnabled(!TextUtils.isEmpty(name.getContent()));
        }
    };

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static AddAccountTypeFragment newInstance() {
        Bundle args = new Bundle();
        AddAccountTypeFragment fragment = new AddAccountTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_accounttype, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(getString(R.string.title_add_card_type));
        name.addTextWatcher(textChange);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.comfirm) public void onComfirm() {
        //        Logger.d(" name:" + name.getContent() + cardtype);
        Intent it = IntentUtils.instanceStringIntent(name.getContent());
        it.putExtra("type", cardtype);
        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK, it);
        this.dismiss();
    }

    @OnClick(R.id.type) public void onCardType() {
        if (sheet == null) {
            sheet = DialogSheet.builder(getContext()).addButton(getString(R.string.cardtype_value), new View.OnClickListener() {
                @Override public void onClick(View v) {
                    sheet.dismiss();
                    type.setContent(getString(R.string.cardtype_value));
                    cardtype = 1;
                }
            }).addButton(getString(R.string.cardtype_times), new View.OnClickListener() {
                @Override public void onClick(View v) {
                    sheet.dismiss();
                    type.setContent(getString(R.string.cardtype_value));
                    cardtype = 2;
                }
            }).addButton(getString(R.string.cardtype_date), new View.OnClickListener() {
                @Override public void onClick(View v) {
                    sheet.dismiss();
                    type.setContent(getString(R.string.cardtype_value));
                    cardtype = 3;
                }
            });
        }
        sheet.show();
    }
}
