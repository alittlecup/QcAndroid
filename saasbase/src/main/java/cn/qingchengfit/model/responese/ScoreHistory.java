package cn.qingchengfit.model.responese;

import android.text.TextUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.utils.StringUtils;

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
 * //   ┃　　　┃
 * //   ┃　　　┃
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/28.
 */

public class ScoreHistory {
    public String id;
    public Shop shop;
    public Student user;
    public String change_score; // 变动积分
    public String after_score;  // 变动后积分
    public String type;
    public String remarks;
    public String created_at;
    public Staff created_by;
    public String username;
    public String favor_id;

    public StudentScoreHistoryBean toStudentScoreHistoryBean() {
        StudentScoreHistoryBean historyBean = new StudentScoreHistoryBean();
        historyBean.title = StringUtils.studentScoreHistotyTitle(type);
        String createdName = TextUtils.isEmpty(created_by.username) ? "" : created_by.username;
        historyBean.info = new StringBuilder().append(created_at.replace("T", " ")).append(" ").append(createdName).toString();

        historyBean.award = TextUtils.isEmpty(favor_id) ? "无" : new StringBuilder().append("奖励规则").append(favor_id).toString();
        historyBean.gym = shop.name;
        historyBean.commont = remarks;
        historyBean.score = change_score;
        historyBean.curScore = after_score;
        historyBean.type = type;
        return historyBean;
    }
}
