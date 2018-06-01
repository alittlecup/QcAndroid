package cn.qingchengfit.staffkit.views.gym;

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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.LargeInputView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;

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
 * Created by Paper on 16/1/28 2016.
 */
public class WriteDescFragment extends BaseDialogFragment {

	LargeInputView descripe;
	Button btn;
	Toolbar toolbar;
	TextView toolbarTitile;
    private TextChange textChange;

    public static void start(Fragment fragment, int requestCode, String title, String hint) {
        WriteDescFragment f = newInstance(title, hint);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static void start(Fragment fragment, int requestCode, String title, String hint, String content) {
        WriteDescFragment f = newInstance(title, hint, content);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static WriteDescFragment newInstance(String title, String hint) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("hint", hint);
        WriteDescFragment fragment = new WriteDescFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WriteDescFragment newInstance(String title, String hint, String content) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("hint", hint);
        args.putString("content", content);
        WriteDescFragment fragment = new WriteDescFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_descript, container, false);
      descripe = (LargeInputView) view.findViewById(R.id.descripe);
      btn = (Button) view.findViewById(R.id.btn);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbarTitile.setText(getArguments().getString("title"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //                dismiss();
                //                getActivity().onBackPressed();
                dismiss();
            }
        });
        descripe.setLabel(getArguments().getString("hint"));
        //        descripe.addTextWatcher(textChange);
        //        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //        inputMethodManager.toggleSoftInputFromWindow ( descripe.getEditText().getWindowToken (),
        //                InputMethodManager.SHOW_FORCED,
        //                InputMethodManager.HIDE_IMPLICIT_ONLY );
        descripe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(descripe.getViewTreeObserver(), this);
                AppUtils.showKeyboard(getActivity(), descripe.getEditText());
                // inputMethodManager.toggleSoftInputFromWindow(descripe.getEditText().getWindowToken(),
                //                        InputMethodManager.SHOW_FORCED,
                //                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        if (!TextUtils.isEmpty(getArguments().getString("content"))) {
            descripe.setContent(getArguments().getString("content"));
        }
        return view;
    }

 public void onComfirm() {
        if (!TextUtils.isEmpty(descripe.getContent())) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), android.app.Activity.RESULT_OK,
                IntentUtils.instanceStringIntent(descripe.getContent()));
        }
        this.dismiss();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

    class TextChange implements TextWatcher {

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            //            btn.setEnabled((!TextUtils.isEmpty(descripe.getContent())));
        }
    }
}
