package cn.qingchengfit.router;

/**
 * 调用当前app内组件的拦截器<br>
 * 如果本地找不到该组件，则添加{@link RemoteQCInterceptor}来处理<br>
 * 如果组件onCall方法执行完之前未调用{@link QC#sendQCResult(String, QCResult)}方法，则按返回值来进行以下处理：<br>
 *  返回值为false: 回调状态码为 {@link QCResult#CODE_ERROR_CALLBACK_NOT_INVOKED} 的错误结果给调用方<br>
 *  返回值为true: 添加{@link Wait4ResultInterceptor}来等待组件调用{@link QC#sendQCResult(String, QCResult)}方法
 * @author billy.qi
 */
class LocalQCInterceptor implements IQCInterceptor {

    //-------------------------单例模式 start --------------
    /** 单例模式Holder */
    private static class LocalCCInterceptorHolder {
        private static final LocalQCInterceptor INSTANCE = new LocalQCInterceptor();
    }
    private LocalQCInterceptor(){}
    /** 获取LocalCCInterceptor的单例对象 */
    static LocalQCInterceptor getInstance() {
        return LocalCCInterceptorHolder.INSTANCE;
    }
    //-------------------------单例模式 end --------------

    @Override
    public QCResult intercept(Chain chain) {
        QC qc = chain.getQC();
        IComponent component = ComponentManager.getComponentByName(qc.getComponentName());
        if (component == null) {
            QC.verboseLog(qc.getCallId(), "component not found in this app. maybe 2 reasons:"
                    + "\n1. QC.enableRemoteCC changed to false"
                    + "\n2. Component named \"%s\" is a IDynamicComponent but now is unregistered"
            );
            return QCResult.error(QCResult.CODE_ERROR_NO_COMPONENT_FOUND);
        }
        //是否需要wait：异步调用且未设置回调，则不需要wait
        boolean callbackNecessary = !qc.isAsync() || qc.getCallback() != null;
        try {
            String callId = qc.getCallId();
            boolean callbackDelay = component.onCall(qc);
            if (QC.VERBOSE_LOG) {
                QC.verboseLog(callId, component.getName() + ":"
                        + component.getClass().getName()
                        + ".onCall(cc) return:" + callbackDelay
                        );
            }
            //兼容异步调用时等待回调结果（同步调用时，此时CC.sendQCResult(callId, result)方法已调用）
            if (!qc.isFinished() && callbackNecessary) {
                //component.onCall(cc)没报exception并且指定了要延时回调结果才进入正常wait流程
                if (callbackDelay) {
                    return chain.proceed();
                } else {
                    QC.logError("component.onCall(cc) return false but QC.sendQCResult(...) not called!"
                            + "\nmaybe: actionName error"
                            + "\nor if-else not call QC.sendQCResult"
                            + "\nor switch-case-default not call QC.sendQCResult"
                            + "\nor try-catch block not call QC.sendQCResult."
                    );
                    //没有返回结果，且不是延时回调（也就是说不会收到结果了）
                    return QCResult.error(QCResult.CODE_ERROR_CALLBACK_NOT_INVOKED);
                }
            }
        } catch(Exception e) {
            return QCResult.defaultExceptionResult(e);
        }
        return qc.getResult();
    }

}
