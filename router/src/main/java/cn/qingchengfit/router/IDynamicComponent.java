package cn.qingchengfit.router;

/**
 * 动态组件
 *  静态组件会在编译时代码扫描生成注册代码，将以单例方式运行；
 *  动态组件则可以在运行时动态地通过以下代码来进行注册与反注册
 *  {@link QC#registerComponent(IDynamicComponent)} {@link QC#unregisterComponent(IDynamicComponent)}
 * @author billy.qi
 * @since 17/7/24 16:34
 */
public interface IDynamicComponent extends IComponent {
}
