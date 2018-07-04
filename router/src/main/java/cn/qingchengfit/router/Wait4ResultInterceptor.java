package cn.qingchengfit.router;

/**
 * 等待异步调用CC.sendQCResult(callId, ccResult)
 * @author billy.qi
 */
class Wait4ResultInterceptor implements IQCInterceptor {

    //-------------------------单例模式 start --------------
    /** 单例模式Holder */
    private static class Wait4ResultInterceptorHolder {
        private static final Wait4ResultInterceptor INSTANCE = new Wait4ResultInterceptor();
    }
    private Wait4ResultInterceptor (){}
    /** 获取Wait4ResultInterceptor的单例对象 */
    static Wait4ResultInterceptor getInstance() {
        return Wait4ResultInterceptorHolder.INSTANCE;
    }
    //-------------------------单例模式 end --------------

    @Override
    public QCResult intercept(Chain chain) {
        QC cc = chain.getQC();
        cc.wait4Result();
        return cc.getResult();
    }
}
