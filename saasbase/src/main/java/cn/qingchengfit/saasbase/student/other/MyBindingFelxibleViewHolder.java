package cn.qingchengfit.saasbase.student.other;

import android.databinding.ViewDataBinding;
import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by yangming on 16/10/11.
 */

public class MyBindingFelxibleViewHolder extends FlexibleViewHolder {
    private ViewDataBinding binding;

    public MyBindingFelxibleViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
    }

    public MyBindingFelxibleViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
        super(view, adapter, stickyHeader);
    }

    public ViewDataBinding getBinding() {
        return this.binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
}