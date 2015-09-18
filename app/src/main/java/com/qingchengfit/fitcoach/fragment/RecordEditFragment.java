package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddCertificate;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecordEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordEditFragment extends BaseSettingFragment {
    public static final String TAG = RecordEditFragment.class.getName();
    private static final String TITLE = "param1";
    private static final String CONTENT = "param2";
    @Bind(R.id.recordedit_host)
    CommonInputView recordeditHost;

    private boolean mTitle;
    private String mContent;

    public RecordEditFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1  是否为编辑
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordEditFragment newInstance(boolean param1, String param2) {
        RecordEditFragment fragment = new RecordEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(TITLE, param1);
        args.putString(CONTENT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getBoolean(TITLE);
            mContent = getArguments().getString(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_edit, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(mTitle ? R.menu.menu_delete : 0, 0, mTitle ? "编辑认证信息" : "添加认证");
        fragmentCallBack.onToolbarClickListener(item1 -> false);//TODO 删除该条记录

        return view;
    }

    @OnClick(R.id.recordedit_host)
    public void onHost() {
        fragmentCallBack.onFragmentChange(SearchFragment.newInstance(SearchFragment.TYPE_ORGANASITON));
    }

    @OnClick(R.id.recordedit_comfirm_btn)
    public void onComplete() {
        QcCloudClient.getApi().postApi.qcAddCertificate(
                new AddCertificate(App.coachid, 1, "哈哈哈哈", "1", "2015-09-30", "100", "")
        ).subscribeOn(Schedulers.newThread()).subscribe(qcResponse -> {

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
