package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.bean.GymSettingInfo;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.model.base.PartnerStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.HomeStatement;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/1 2016.
 */
public interface GymDetailView extends PView {
    void setGymInfo();//设置健身房基础信息

    void renew();//续费

    void initFunction();//初始化功能

    void studentPreview(String url, String copy);//会员页面预览

    void guideToWechat(String url);//如何对接到公众账号

    void setBanner(List<Banner> strings);

    void setInfo(HomeStatement stats);

    void setRecharge(GymDetail.Recharge recharge,boolean hasFirst,String price);

    void showOutDataTime();

    void hideOutDateTime();

    void onGymInfo(CoachService coachService);

    void onModule(Object module);

    void onFailed();
    void onPartnter(PartnerStatus status);

    void onNotiCount(int count);

    void onManageBrand();

    void onSpecialPoint(int count);
    void onMiniProgram(MiniProgram miniProgram);

    void onQuitGym();

    void onSuperUser(Staff su);

    void showGymFirstSettingDialog(GymSettingInfo data);
}
