package cn.qingchengfit.staffkit.views.signin.out;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.SignInManualEvent;
import cn.qingchengfit.staffkit.views.signin.SignInStudentListFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangming on 16/8/30.
 */
public class SignOutFragment extends BaseFragment {

    private SignOutListFragment signOutListFragment;
    private SignInStudentListFragment signInStudentListFragment;

    public SignOutFragment() {
    }

    public static SignOutFragment newInstance() {
        SignOutFragment fragment = new SignOutFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        initView();

        initRxBus();

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignOutFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (signOutListFragment == null) {
            signOutListFragment = new SignOutListFragment();
        }
        if (signInStudentListFragment == null) {
            signInStudentListFragment = SignInStudentListFragment.newInstance(false);
        }
        //        transaction.replace(R.id.signin_frag, signOutListFragment)
        //                .addToBackStack(null)
        //                .commit();

        if (!signOutListFragment.isAdded()) {
            transaction.add(R.id.signout_frag, signOutListFragment);
        }
        if (!signInStudentListFragment.isAdded()) {
            transaction.add(R.id.signout_frag, signInStudentListFragment);
        }
        transaction.hide(signInStudentListFragment).show(signOutListFragment).commit();
    }

    private void initRxBus() {
        Observable<SignInManualEvent> observable = RxBusAdd(SignInManualEvent.class);
      observable.observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(new Action1<SignInManualEvent>() {
            @Override public void call(SignInManualEvent signInManualEvent) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                if (signInManualEvent.isSignIn()) {
                    return;
                }
                if (signInManualEvent.isCancel()) {
                    if (signOutListFragment == null) {
                        signOutListFragment = new SignOutListFragment();
                    }
                    if (!signOutListFragment.isAdded()) {
                        transaction.hide(signInStudentListFragment)
                            .add(R.id.signout_frag, signOutListFragment)
                            .show(signOutListFragment)
                            .commit();
                    } else {
                        transaction.hide(signInStudentListFragment).show(signOutListFragment).commit();
                    }
                    //                            transaction.replace(R.id.signin_frag, signOutListFragment)
                    //                                    .addToBackStack(null)
                    //                                    .commit();
                } else {
                    if (signInStudentListFragment == null) {
                        signInStudentListFragment = SignInStudentListFragment.newInstance(false);
                    }
                    if (!signInStudentListFragment.isAdded()) {
                        transaction.hide(signOutListFragment)
                            .add(R.id.signout_frag, signInStudentListFragment)
                            .show(signInStudentListFragment)
                            .commit();
                    } else {
                        transaction.hide(signOutListFragment).show(signInStudentListFragment).commit();
                    }
                    //                            transaction.replace(R.id.signin_frag, signInStudentListFragment)
                    //                                    .addToBackStack(null)
                    //                                    .commit();
                }
            }
        });
    }
}
