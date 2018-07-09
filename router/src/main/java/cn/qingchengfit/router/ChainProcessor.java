package cn.qingchengfit.router;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 启动拦截器调用链
 * @author billy.qi
 */
class ChainProcessor implements Callable<QCResult> {

    private final Chain chain;

    ChainProcessor(Chain chain) {
        this.chain = chain;
    }

    @Override
    public QCResult call() {
        QC qc = chain.getQC();
        String callId = qc.getCallId();
        //从开始调用的时候就开始进行监控，也许时间设置的很短，可能都不需要执行拦截器调用链
        QCMonitor.addMonitorFor(qc);
        QCResult result;
        try {
            if (QC.VERBOSE_LOG) {
                int poolSize = ((ThreadPoolExecutor) ComponentManager.QC_THREAD_POOL).getPoolSize();
                QC.verboseLog(callId, "process qc at thread:"
                        + Thread.currentThread().getName() + ", pool size=" + poolSize);
            }
            if (qc.isFinished()) {
                //timeout, cancel, QC.sendqcResult(callId, qcResult)
                result = qc.getResult();
            } else {
                try {
                    QC.verboseLog(callId, "start interceptor chain");
                    result = chain.proceed();
                    if (QC.VERBOSE_LOG) {
                        QC.verboseLog(callId, "end interceptor chain.QCResult:" + result);
                    }
                } catch(Exception e) {
                    result = QCResult.defaultExceptionResult(e);
                }
            }
        } catch(Exception e) {
            result = QCResult.defaultExceptionResult(e);
        } finally {
            QCMonitor.removeById(callId);
        }
        //返回的结果，永不为null，默认为QCResult.defaultNullResult()
        if (result == null) {
            result = QCResult.defaultNullResult();
        }
        qc.setResult(result);
        performCallback(qc, result);
        return result;
    }

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static void performCallback(QC qc, QCResult result) {
        IComponentCallback callback = qc.getCallback();
        if (QC.VERBOSE_LOG) {
            QC.verboseLog(qc.getCallId(), "perform callback:" + qc.getCallback()
                    + ", QCResult:" + result);
        }
        if (callback == null) {
            return;
        }
        if (qc.isCallbackOnMainThread()) {
            HANDLER.post(new CallbackRunnable(callback, qc, result));
        } else {
            try {
                callback.onResult(qc, result);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static class CallbackRunnable implements Runnable {
        private final QC qc;
        private IComponentCallback callback;
        private QCResult result;

        CallbackRunnable(IComponentCallback callback, QC qc, QCResult result) {
            this.qc = qc;
            this.callback = callback;
            this.result = result;
        }

        @Override
        public void run() {
            try {
                callback.onResult(qc, result);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
