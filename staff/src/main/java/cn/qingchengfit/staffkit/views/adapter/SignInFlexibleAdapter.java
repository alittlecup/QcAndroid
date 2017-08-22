package cn.qingchengfit.staffkit.views.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInFlexibleAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public SignInFlexibleAdapter(@NonNull List<AbstractFlexibleItem> items) {
        super(items);
    }

    public SignInFlexibleAdapter(@NonNull List<AbstractFlexibleItem> items, @Nullable Object listeners) {
        super(items, listeners);
    }
}
