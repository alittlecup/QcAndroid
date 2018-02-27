package cn.qingchengfit.model.responese;

import cn.qingchengfit.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   阿弥陀佛
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * // 体侧数据列表页 item data bean
 * // Created by yangming on 16/11/17.
 */

public class BodyTestPreview {
    @SerializedName("created_at") public String created_at;
    @SerializedName("id") public String id;

    public BodyTestBean toBodyTestBean() {
        BodyTestBean bodyTestBean = new BodyTestBean();
        bodyTestBean.data = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(created_at)) + "体测数据";
        bodyTestBean.id = id;
        return bodyTestBean;
    }
}
