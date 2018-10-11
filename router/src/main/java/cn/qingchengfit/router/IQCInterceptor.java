package cn.qingchengfit.router;

/**
 * 拦截器接口
 *
 * @author billy.qi
 */
public interface IQCInterceptor {

    /**
     * 拦截器方法
     * chain.getCC() 来获取qc对象
     * 调用chain.proceed()来传递调用链
     * 也可通过不调用chain.proceed()来中止调用链的传递
     * 通过qc.getParams()等方法来获取参数信息，并可以修改params
     * @param chain 链条
     * @return 调用结果
     */
    QCResult intercept(Chain chain);

}
