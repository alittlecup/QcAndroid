package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;

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

public class ScoreRank {
    public Staff user;
    public String score;

    public Student toStudent() {
        Student student = new Student();
        student.id = this.user.id;
        student.username = this.user.username;
        student.phone = this.user.phone;
        student.score = this.score;
        student.gender = this.user.gender;
        return student;
    }
}
