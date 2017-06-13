package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.utils.AppUtils;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/7.
 */

public class ContactTaDialog extends BaseBottomSheetDialogFragment {

    @BindView(R.id.tv_contact_phone) TextView tvContactPhone;
    private String phone;

    public static void start(Fragment fragment, int requestCode, String phone) {
        ContactTaDialog f = newInstance(phone);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ContactTaDialog newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString("phone", phone);
        ContactTaDialog fragment = new ContactTaDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void onPause() {
        super.onPause();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1 通过样式定义
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Mydialog);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog_contact_ta, container, false);
        unbinder = ButterKnife.bind(this, view);
        phone = getArguments().getString("phone");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvContactPhone.setText(new StringBuilder().append("呼叫").append(phone).toString());
    }

    @OnClick({ R.id.tv_contact_phone, R.id.tv_contact_sms }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_contact_phone:
                Uri uri = Uri.parse(new StringBuilder().append("tel:").append(phone).toString());
                Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(dialntent);
                break;
            case R.id.tv_contact_sms:
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.putExtra("address", phone);
                //intent.setType("vnd.android-dir/mms-sms");
                //startActivity(intent);
                AppUtils.doSendSMSTo(getContext(), phone);
                break;
        }
    }
}
