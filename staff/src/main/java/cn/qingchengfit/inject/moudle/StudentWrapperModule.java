package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.inject.model.StudentWrapper;
import dagger.Module;
import dagger.Provides;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/19 2016.
 */
@Module public class StudentWrapperModule {
    StudentWrapper studentWrapper;

    public StudentWrapperModule() {
        studentWrapper = new StudentWrapper();
    }

    public StudentWrapperModule(StudentWrapper studentBean) {
        this.studentWrapper = studentBean;
    }

    public StudentWrapper getStudentWrapper() {
        return studentWrapper;
    }

    public void setStudentWrapper(StudentWrapper studentWrapper) {
        this.studentWrapper = studentWrapper;
    }

    @Provides StudentWrapper provideStudent() {
        return this.studentWrapper;
    }
}
