package cn.qingchengfit.saasbase.report.bean;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GymCardtpl {
    @SerializedName("card_tpls") public List<CardTpl> card_tpls;
    @SerializedName("service") public CoachService service;
}