package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.CitiesChooser;
import cn.qingchengfit.staffkit.views.custom.LargeInputView;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.widgets.CommonInputView;

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
public class WriteAddressFragment extends BaseDialogFragment {
    public static final String TAG = WriteAddressFragment.class.getSimpleName();

	CommonInputView city;
	LargeInputView address;
	Button btn;
	Toolbar toolbar;
	TextView toolbarTitile;
    private CitiesChooser citiesChooser;

    public static void start(Fragment fragment, int requestCode, String city, String address) {
        BaseDialogFragment f = newInstance(city, address);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static WriteAddressFragment newInstance(String city, String address) {
        Bundle args = new Bundle();
        WriteAddressFragment fragment = new WriteAddressFragment();
        args.putString("city", city);
        args.putString("address", address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        citiesChooser = new CitiesChooser(getContext());
    }

 public void onCity() {
        citiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
            @Override public void onCityChoosen(String provice, String city, String district, int id) {
                String content;
                if (provice.endsWith(city)) {
                    content = provice + district;
                } else {
                    content = provice + city + district;
                }
                WriteAddressFragment.this.city.setContent(content);
            }
        });
        citiesChooser.show(getView());
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_address, container, false);
      city = (CommonInputView) view.findViewById(R.id.city);
      address = (LargeInputView) view.findViewById(R.id.address);
      btn = (Button) view.findViewById(R.id.btn);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.city).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onCity();
        }
      });
      view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(getString(R.string.title_write_address));
        city.setContent("北京朝阳区");
        if (!TextUtils.isEmpty(getArguments().getString("city"))) city.setContent(getArguments().getString("city"));
        if (!TextUtils.isEmpty(getArguments().getString("address"))) address.setContent(getArguments().getString("address"));
        return view;
    }

 public void onComfirm() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
            IntentUtils.instanceStringIntent(city.getContent(), address.getContent()));
        this.dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
