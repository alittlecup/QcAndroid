package cn.qingchengfit.saasbase.course.course.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

public class JacketManageAdapter extends FlexibleAdapter {

    public JacketManageAdapter(@NonNull List items) {
        super(items);
    }

    public JacketManageAdapter(@NonNull List items, @Nullable Object listeners) {
        super(items, listeners);
    }

    @Override public boolean shouldMove(int fromPosition, int toPosition) {
        return !(toPosition == 0 || toPosition == getItemCount() - 1);
    }
}