package cn.qingchengfit.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 组件调用链，用于管理拦截器的运行顺序
 * @author billy.qi
 */
public class Chain {
    private final List<IQCInterceptor> interceptors = new ArrayList<>();
    private final QC qc;
    private int index;

    Chain(QC qc) {
        this.qc = qc;
        this.index = 0;
    }

    void addInterceptors(Collection<? extends IQCInterceptor> interceptors) {
        if (interceptors != null && !interceptors.isEmpty()) {
            this.interceptors.addAll(interceptors);
        }
    }
    void addInterceptor(IQCInterceptor interceptor) {
        if (interceptor != null) {
            this.interceptors.add(interceptor);
        }
    }

    public QCResult proceed() {
        if (index >= interceptors.size()) {
            return QCResult.defaultNullResult();
        }
        IQCInterceptor interceptor = interceptors.get(index++);
        //处理异常情况：如果为拦截器为null，则执行下一个
        if (interceptor == null) {
            return proceed();
        }
        String name = interceptor.getClass().getName();
        String callId = qc.getCallId();
        QCResult result;
        if (qc.isFinished()) {
            //timeout, cancel, QC.sendqcResult(callId, qcResult), qc.setResult, etc...
            result = qc.getResult();
        } else {
            if (QC.VERBOSE_LOG) {
                QC.verboseLog(callId, "start interceptor:" + name);
            }
            try {
                result = interceptor.intercept(this);
            } catch(Throwable e) {
                //防止拦截器抛出异常
                result = QCResult.defaultExceptionResult(e);
            }
            if (QC.VERBOSE_LOG) {
                QC.verboseLog(callId, "end interceptor:" + name + ".QCResult:" + result);
            }
        }
        //拦截器理论上不应该返回null，但为了防止意外(自定义拦截器返回null，此处保持qcResult不为null
        //消灭NPE
        if (result == null) {
            result = QCResult.defaultNullResult();
        }
        qc.setResult(result);
        return result;
    }

    public QC getQC() {
        return qc;
    }
}
