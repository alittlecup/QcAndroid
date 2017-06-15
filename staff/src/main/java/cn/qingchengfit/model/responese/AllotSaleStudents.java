package cn.qingchengfit.model.responese;

import java.util.List;

/**
 * Created by yangming on 16/10/19.
 */

public class AllotSaleStudents extends QcResponsePage {
    /**
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public List<Student> users;
}
