package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInLogEvent;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by yangming on 16/8/25.
 */
public class SignInPermisionFragment extends BaseFragment {

	TextView tvSigninFooter;

    public SignInPermisionFragment() {
    }

    public static SignInPermisionFragment newInstance() {
        SignInPermisionFragment fragment = new SignInPermisionFragment();
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_permision, container, false);
      tvSigninFooter = (TextView) view.findViewById(R.id.tv_signin_footer);
      view.findViewById(R.id.tv_signin_footer).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          SignInPermisionFragment.this.onClick(v);
        }
      });

      return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInPermisionFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onClick(View view) {
        RxBus.getBus().post(new SignInLogEvent());
    }
}
