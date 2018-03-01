package cn.qingchengfit.saasbase.mvvm_student.di;

import cn.qingchengfit.saasbase.di.BindStudentActivity;
import dagger.Module;

/**
 * Created by huangbaole on 2018/2/26.
 */
@Module(includes = { BindStudentActivity.class }) public abstract class StudentModule {
}
