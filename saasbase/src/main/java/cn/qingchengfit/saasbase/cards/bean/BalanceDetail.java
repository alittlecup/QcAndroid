package cn.qingchengfit.saasbase.cards.bean;

public class BalanceDetail {

    public String id;
    public float value;
    public String key;

    public BalanceDetail() {
    }

    public BalanceDetail(String id, float value, String key) {
        this.id = id;
        this.value = value;
        this.key = key;
    }
}
