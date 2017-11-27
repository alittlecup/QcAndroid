package cn.qingchengfit.saasbase.cards.bean;

public class NotifyIsOpen {


    public String id;
    public String value;
    public boolean editable;
    public boolean readable;
    public String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //    public void setValue(boolean value) {
    //        this.value = value;
    //    }

    //    public boolean isValue() {
    //        return value;
    //    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
