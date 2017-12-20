package cn.qingchengfit.staffkit.views.setting.brand;

import cn.qingchengfit.model.body.ChangeBrandCreatorBody;

/**
 * Created by fb on 2017/12/4.
 */

public class ChangeOwenerEvent {

  private ChangeBrandCreatorBody body;

  public ChangeOwenerEvent(ChangeBrandCreatorBody body) {
    this.body = body;
  }

  public ChangeBrandCreatorBody getBody() {
    return body;
  }
}
