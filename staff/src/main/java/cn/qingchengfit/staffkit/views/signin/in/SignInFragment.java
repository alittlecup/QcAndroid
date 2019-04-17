package cn.qingchengfit.staffkit.views.signin.in;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInManualEvent;
import cn.qingchengfit.staffkit.views.signin.SignInStudentListFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangming on 16/8/25.
 */
public class SignInFragment extends BaseFragment {

    private SignInListFragment signInListFragment;
    private SignInStudentListFragment signInStudentListFragment;

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        initView();
        initRxBus();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

    private void initView() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (signInListFragment == null) {
            signInListFragment = new SignInListFragment();
        }
        if (signInStudentListFragment == null) {
            signInStudentListFragment = SignInStudentListFragment.newInstance(true);
        }
        if (!signInListFragment.isAdded()) {
            transaction.add(R.id.signin_frag, signInListFragment);
        }
        if (!signInStudentListFragment.isAdded()) {
            transaction.add(R.id.signin_frag, signInStudentListFragment);
        }
        transaction.hide(signInStudentListFragment).show(signInListFragment).commit();
    }

    private void initRxBus() {
        RxBusAdd(SignInManualEvent.class).observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInManualEvent>() {
                @Override public void call(SignInManualEvent signInManualEvent) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    if (!signInManualEvent.isSignIn()) {
                        return;
                    }

                    if (signInManualEvent.isCancel()) {
                        if (signInListFragment == null) {
                            signInListFragment = new SignInListFragment();
                        }
                        if (!signInListFragment.isAdded()) {
                            transaction.hide(signInStudentListFragment).add(R.id.signin_frag, signInListFragment).commit();
                        } else {
                            transaction.hide(signInStudentListFragment).show(signInListFragment).commit();
                        }
                    } else {
                        if (signInStudentListFragment == null) {
                            signInStudentListFragment = SignInStudentListFragment.newInstance(true);
                        }
                        if (!signInStudentListFragment.isAdded()) {
                            transaction.hide(signInListFragment).add(R.id.signin_frag, signInStudentListFragment).commit();
                        } else {
                            transaction.hide(signInListFragment).show(signInStudentListFragment).commit();
                        }
                    }
                }
            });
    }
}
