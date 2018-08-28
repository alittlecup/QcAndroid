package cn.qingchengfit.router;

/**
 * 组件接口
 * 注意：
 *      1. 此接口的实现类代表的是一个组件暴露给外部调用的入口
 *      2. 实现类必须含有一个无参构造方法，以供自动注册插件进行代码注入
 *      3. 实现类有且只有一个对象会被注册到组件库中，故不能为Activity、Fragment等(可以改用动态组件注册{@link IDynamicComponent})
 *  动态组件：  {@link IDynamicComponent}
 * @author billy.qi
 */
public interface IComponent {

    /**
     * 定义组件名称
     * @return 组件的名称
     */
    String getName();

    /**
     * 调用此组件时执行的方法（此方法只在LocalCCInterceptor中被调用）
     * 注：执行完成后必须调用QC.sendQCResult(callId, QCResult.success(result));
     * qc.getContext() android的context
     * qc.getAction() 调用的action
     * qc.getParams() 调用参数
     * qc.getCallId() 调用id，用于取消调用
     * @param qc 调用信息
     * @return 是否延迟回调结果 {@link QC#sendQCResult(String, QCResult)}
     */
    boolean onCall(QC qc);

}
