package cn.qingchengfit.student.common.mvvm;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huangbaole on 2017/12/6.
 */

public class ActionLiveEvent extends MutableLiveData<Void> {

    @MainThread
    public void observe(LifecycleOwner owner, final Observer<Void> observer) {

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void t) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    public void setValue(@Nullable Void t) {
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}
