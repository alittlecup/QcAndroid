package cn.qingchengfit.router;

/**
 * 组件回调
 * @author billy.qi
 * @since 17/6/29 11:34
 */
public interface IComponentCallback {
    /**
     * call when cc is received QCResult
     * @param qc cc
     * @param result the QCResult
     */
    void onResult(QC qc, QCResult result);
}
