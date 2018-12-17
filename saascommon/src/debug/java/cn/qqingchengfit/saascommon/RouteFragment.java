package cn.qqingchengfit.saascommon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.databinding.RouteFragmentBinding;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.BundleBuilder;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Iterator;
import java.util.Map;

@Leaf(module = "saascommon", path = "/route/page") public class RouteFragment
    extends SaasCommonFragment {
  RouteFragmentBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = RouteFragmentBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.btnStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeToPage(mBinding.editModel.getText().toString(), mBinding.editPath.getText().toString(),
            getBundleFromJson(mBinding.editBundle.getText().toString()));
      }
    });
  }

  private Bundle getBundleFromJson(String text) {
    JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
    BundleBuilder bundleBuilder = new BundleBuilder();
    while (iterator.hasNext()) {
      Map.Entry<String, JsonElement> next = iterator.next();
      bundleBuilder.withObject(next.getKey(), next.getValue());
    }
    return bundleBuilder.build();
  }

  private void routeToPage(String model, String path, Bundle bundle) {
    RouteUtil.routeTo(getContext(), model, path, bundle);
  }
}
