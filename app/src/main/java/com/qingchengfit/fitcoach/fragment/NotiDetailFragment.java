package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotiDetailFragment extends Fragment {
    public static final String TAG = NotiDetailFragment.class.getName();
    @BindView(R.id.notidetail_time)
    TextView notidetailTime;
    @BindView(R.id.notidetail_sender)
    TextView notidetailSender;
    @BindView(R.id.notidetail_img)
    ImageView notidetailImg;
    @BindView(R.id.notidetail_content)
    TextView notidetailContent;
    @BindView(R.id.notidetail_title)
    TextView notidetailTitle;
    @BindView(R.id.notidetail_content_webview)
    WebView notidetailContentWebview;
    private int id = 0;
    private Unbinder unbinder;

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
        unbinder=ButterKnife.bind(this, view);

        if (id != 0)
            QcCloudClient.getApi().postApi.qcClearOneNotification(App.coachid,id+"").subscribeOn(Schedulers.io())
            .subscribe();
            QcCloudClient.getApi().getApi.qcGetMsgDetails(id).subscribe(
                    qcNotiDetailResponse -> {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                notidetailTime.setText(qcNotiDetailResponse.getData().getCreated_at().replace("T", " "));
                                Glide.with(App.AppContex).load(PhotoUtils.getSmall(qcNotiDetailResponse.getData().getPhoto())).into(notidetailImg);
                                notidetailSender.setText(qcNotiDetailResponse.getData().getSender());
                                notidetailTitle.setText(qcNotiDetailResponse.getData().getTitle());
                                notidetailContentWebview.loadUrl(qcNotiDetailResponse.getData().getUrl());
//                    notidetailContent.setText(qcNotiDetailResponse.getData());
                            });
                        }
                    }, throwable -> {
                    }, () -> {
                    }
            );
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
