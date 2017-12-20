package cn.qingchengfit.bean;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/11/20 2015.
 */
public class StudentCardBean {
    public String cardname;
    public String balance;
    public String gymName;
    public String students;
    public String timelimit;
    public String url;

    public StudentCardBean(String cardname, String balance, String gymName, String students, String timelimit, String url) {
        this.cardname = cardname;
        this.balance = balance;
        this.gymName = gymName;
        this.students = students;
        this.url = url;
        this.timelimit = timelimit;
    }
}
