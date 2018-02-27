package cn.qingchengfit.saasbase.utils;

import cn.qingchengfit.model.base.Personage;
import java.util.Comparator;

public class QcPersonalComparator implements Comparator<Personage> {
    @Override public int compare(Personage lhs, Personage rhs) {
        return lhs.getHead().compareTo(rhs.getHead());
    }
}
