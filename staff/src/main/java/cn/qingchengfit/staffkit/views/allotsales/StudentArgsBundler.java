package cn.qingchengfit.staffkit.views.allotsales;

import android.os.Bundle;
import cn.qingchengfit.model.base.StudentBean;
import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/10/20.
 */

public class StudentArgsBundler implements ArgsBundler<List<StudentBean>> {
    @Override public void put(String key, List<StudentBean> value, Bundle bundle) {
        bundle.putParcelableArrayList(key, (ArrayList<StudentBean>) value);
    }

    @Override public List<StudentBean> get(String key, Bundle bundle) {
        return bundle.getParcelableArrayList(key);
    }
}
