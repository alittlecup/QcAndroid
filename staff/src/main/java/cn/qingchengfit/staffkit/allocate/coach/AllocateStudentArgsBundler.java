package cn.qingchengfit.staffkit.allocate.coach;

import android.os.Bundle;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/5/5.
 */

public class AllocateStudentArgsBundler implements ArgsBundler<List<CommonAllocateDetailItem>> {

    @Override public List<CommonAllocateDetailItem> get(String key, Bundle bundle) {
        return bundle.getParcelableArrayList(key);
    }

    @Override public void put(String key, List<CommonAllocateDetailItem> value, Bundle bundle) {
        bundle.putParcelableArrayList(key, (ArrayList<CommonAllocateDetailItem>) value);
    }
}
