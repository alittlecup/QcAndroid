package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotiDetailFragment extends Fragment {
    public static final String TAG = NotiDetailFragment.class.getName();
    @Bind(R.id.notidetail_time)
    TextView notidetailTime;
    @Bind(R.id.notidetail_sender)
    TextView notidetailSender;
    @Bind(R.id.notidetail_img)
    ImageView notidetailImg;
    @Bind(R.id.notidetail_content)
    TextView notidetailContent;
    private int id = 0;

    public NotiDetailFragment() {
    }

    public static NotiDetailFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        NotiDetailFragment fragment = new NotiDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noti_detail, container, false);
        ButterKnife.bind(this, view);
        if (id != 0)
            QcCloudClient.getApi().getApi.qcGetMsgDetails(id).subscribe(
                    qcNotiDetailResponse -> {
                        getActivity().runOnUiThread(() -> {
                            notidetailTime.setText(qcNotiDetailResponse.getData().getCreated_at());
//                    notidetailContent.setText(qcNotiDetailResponse.getData());
                        });
                    }
            );
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
