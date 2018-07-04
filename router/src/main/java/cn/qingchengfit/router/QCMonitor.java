package cn.qingchengfit.router;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CC监控系统
 * 每个CC对象有自己的超时时间点，
 * 维护一个待监控的CC列表，当列表不为空时，启动一个线程进行监控
 * @author billy.qi
 */
class QCMonitor {

    static final ConcurrentHashMap<String, QC> QC_MAP = new ConcurrentHashMap<>();

    private static final AtomicBoolean STOPPED = new AtomicBoolean(true);
    private static volatile long minTimeoutAt = Long.MAX_VALUE;
    private static final byte[] LOCK = new byte[0];

    static void addMonitorFor(QC qc) {
        if (qc != null) {
            QC_MAP.put(qc.getCallId(), qc);
            qc.addCancelOnFragmentDestroyIfSet();
            long timeoutAt = qc.timeoutAt;
            if (timeoutAt > 0) {
                if (minTimeoutAt > timeoutAt) {
                    minTimeoutAt = timeoutAt;
                    //如果最小timeout时间有变化，且监控线程在wait，则唤醒监控线程
                    synchronized (LOCK) {
                        LOCK.notifyAll();
                    }
                }
                if (STOPPED.compareAndSet(true, false)) {
                    new TimeoutMonitorThread().start();
                }
            }
            if (QC.VERBOSE_LOG) {
                QC.verboseLog(qc.getCallId(), "totalCC count=" + QC_MAP.size()
                        + ". add monitor for:" + qc);
            }
        }
    }

    static QC getById(String callId) {
        return QC_MAP.get(callId);
    }

    static void removeById(String callId) {
        QC_MAP.remove(callId);
    }

    /**
     * CC超时监控线程
     */
    private static class TimeoutMonitorThread extends Thread {
        @Override
        public void run() {
            if (STOPPED.get()) {
                return;
            }
            while(QC_MAP.size() > 0 || minTimeoutAt == Long.MAX_VALUE) {
                try {
                    long millis = minTimeoutAt - System.currentTimeMillis();
                    if (millis > 0) {
                        synchronized (LOCK) {
                            LOCK.wait(millis);
                        }
                    }
                    //next cc timeout
                    long min = Long.MAX_VALUE;
                    long now = System.currentTimeMillis();
                    for (QC cc : QC_MAP.values()) {
                        if (!cc.isFinished()) {
                            long timeoutAt = cc.timeoutAt;
                            if (timeoutAt > 0) {
                                if (timeoutAt < now) {
                                    cc.timeout();
                                } else if (timeoutAt < min) {
                                    min = timeoutAt;
                                }
                            }
                        }
                    }
                    minTimeoutAt = min;
                } catch (InterruptedException ignored) {
                }
            }
            STOPPED.set(true);
        }
    }

    /**
     * activity lifecycle monitor
     *
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    static class ActivityMonitor implements Application.ActivityLifecycleCallbacks {
        @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }
        @Override public void onActivityStarted(Activity activity) { }
        @Override public void onActivityResumed(Activity activity) { }
        @Override public void onActivityPaused(Activity activity) { }
        @Override public void onActivityStopped(Activity activity) { }
        @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
        @Override public void onActivityDestroyed(Activity activity) {
            Collection<QC> values = QC_MAP.values();
            for (QC qc : values) {
                if (!qc.isFinished() && qc.cancelOnDestroyActivity != null
                        && qc.cancelOnDestroyActivity.get() == activity) {
                    qc.cancelOnDestroy(activity);
                }
            }
        }
    }

    static class FragmentMonitor extends FragmentManager.FragmentLifecycleCallbacks {
        WeakReference<QC> reference;

        FragmentMonitor(QC cc) {
            this.reference = new WeakReference<>(cc);
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            if (reference != null) {
                QC qc = reference.get();
                if (qc != null && !qc.isFinished()) {
                    WeakReference<Fragment> fragReference = qc.cancelOnDestroyFragment;
                    if (fragReference != null) {
                        Fragment fragment = fragReference.get();
                        if (f == fragment) {
                            qc.cancelOnDestroy(f);
                        }
                    }
                }
            }
        }
    }
}