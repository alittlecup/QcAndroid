package cn.qingchengfit.pos.di;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import java.util.HashMap;

/**
 * Created by fb on 2017/10/19.
 */

public class PosGymWrapper extends GymWrapper {

  public PosGymWrapper(Builder builder) {
    super(builder);
  }

  @Override public HashMap<String, Object> getParams() {
    HashMap<String, Object> params = new HashMap<>();
    if (getCoachService() != null) {
      params.put("gym_id", getCoachService().getId());
    }
    return params;
  }

  public static final class Builder extends GymWrapper.Builder {

    private CoachService coachService;
    private Brand brand;

    public Builder() {
    }

    public Builder coachService(CoachService val) {
      coachService = val;
      return this;
    }

    public Builder brand(Brand val) {
      brand = val;
      return this;
    }

    @Override public PosGymWrapper build() {
      return new PosGymWrapper(this);
    }
  }
}
