package com.tencent.qcloud.timchat.chatmodel;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;
import com.tencent.qcloud.timchat.adapters.ChatItem;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {

  private String TAG = getClass().getSimpleName();

  private final int TYPE_TYPING = 14;
  private final int TYPE_RECRUIT = 1001;      //顶部的职位信息 -- 求职端
  private final int TYPE_RESUME = 1002;       //发送的简历信息 --求职端
  private final int TYPE_SEND_RECRUIT = 1003; //发送的邀约信息 -- 招聘端
  private final int TYPE_TOP_RESUME = 1004;   //顶部的简历信息 -- 招聘端


  private Type type;
  private String desc;
  private String data;

  public CustomMessage(TIMMessage message) {
    this.message = message;
    TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
    parse(elem.getData());
  }

  public CustomMessage(Type type) {
    message = new TIMMessage();
    String data = "";
    JSONObject dataJson = new JSONObject();
    Gson gson = new Gson();
    try {
      switch (type) {
        case TYPING:
          dataJson.put("userAction", TYPE_TYPING);
          dataJson.put("actionParam", "EIMAMSG_InputStatus_Ing");
          data = dataJson.toString();
          break;
        default:
          break;
      }
    } catch (JSONException e) {
      Log.e(TAG, "generate json error");
    }
    TIMCustomElem elem = new TIMCustomElem();
    elem.setData(data.getBytes());
    message.addElement(elem);
  }

  public CustomMessage(Type type, String jsonStr) {
    message = new TIMMessage();
    TIMCustomElem elem = new TIMCustomElem();
    this.type = type;
    if (type == Type.RECRUIT || type == Type.RESUME) {
      elem.setData(jsonStr.getBytes());
    }
    message.addElement(elem);
  }

  public Object getData() {
    Object dataObj = null;
    String json = new String(((TIMCustomElem) (message.getElement(0))).getData());
    try {
      JSONObject jsonObject = new JSONObject(json);
      int action = jsonObject.getInt("userAction");
      JSONObject dataObject = jsonObject.getJSONObject("data");
      switch (action) {
        case TYPE_RESUME:
        case TYPE_TOP_RESUME:
          ResumeModel resumeModel = new ResumeModel();
          if (dataObject.has("id")) resumeModel.id = dataObject.getString("id");
          if (dataObject.has("max_education")) {
            resumeModel.max_education = dataObject.getInt("max_education");
          }
          if (dataObject.has("birthday")) resumeModel.birthday = dataObject.getString("birthday");
          if (dataObject.has("gender")) resumeModel.gender = dataObject.getInt("gender");
          if (dataObject.has("avatar")) resumeModel.avatar = dataObject.getString("avatar");
          if (dataObject.has("work_year")) resumeModel.work_year = dataObject.getInt("work_year");
          if (dataObject.has("username")) resumeModel.username = dataObject.getString("username");
          if (dataObject.has("city")) resumeModel.city = dataObject.getString("city");
          if (dataObject.has("min_salary")) resumeModel.min_salary = dataObject.getInt("min_salary");
          if (dataObject.has("max_salary")) resumeModel.max_salary = dataObject.getInt("max_salary");
          if (dataObject.has("height")) resumeModel.height = String.valueOf(dataObject.get("height"));
          if (dataObject.has("weight")) resumeModel.weight = String.valueOf(dataObject.get("height"));

          dataObj = resumeModel;
          break;
        case TYPE_RECRUIT:
        case TYPE_SEND_RECRUIT:
          RecruitModel recruitModel = new RecruitModel();
          recruitModel.id = dataObject.getString("id");
          recruitModel.address = dataObject.getString("address");
          recruitModel.gender = dataObject.getInt("gender");
          recruitModel.photo = dataObject.getString("photo");
          recruitModel.max_age = dataObject.getInt("max_age");
          recruitModel.min_age = dataObject.getInt("min_age");
          recruitModel.max_height = dataObject.getInt("max_height");
          recruitModel.min_height = dataObject.getInt("min_height");
          recruitModel.max_salary = dataObject.getInt("max_salary");
          recruitModel.min_salary = dataObject.getInt("min_salary");
          recruitModel.max_work_year = dataObject.getInt("max_work_year");
          recruitModel.min_work_year = dataObject.getInt("min_work_year");
          recruitModel.name = dataObject.getString("name");
          dataObj = recruitModel;
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return dataObj;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  private void parse(byte[] data) {
    type = Type.INVALID;
    try {
      String str = new String(data, "UTF-8");
      JSONObject jsonObj = new JSONObject(str);
      int action = jsonObj.getInt("userAction");
      switch (action) {
        case TYPE_TYPING:
          type = Type.TYPING;
          this.data = jsonObj.getString("actionParam");
          if (this.data.equals("EIMAMSG_InputStatus_End")) {
            type = Type.INVALID;
          }
          break;
        case TYPE_RECRUIT:
          type = Type.RECRUIT;
          break;
        case TYPE_RESUME:
          type = Type.RESUME;
          break;
        case TYPE_SEND_RECRUIT:
          type = Type.SEND_RECRUIT;
          break;
        case TYPE_TOP_RESUME:
          type = Type.TOP_RESUME;
          break;
      }
    } catch (IOException | JSONException e) {
      Log.e(TAG, "parse json error");
    }
  }

  /**
   * 显示消息
   *
   * @param viewHolder 界面样式
   * @param context 显示消息的上下文
   */
  @Override public void showMessage(ChatItem.ViewHolder viewHolder, Context context) {

  }

  /**
   * 获取消息摘要
   */
  @Override public String getSummary() {
    if (type == Type.RECRUIT){
      return "对【" + ((RecruitModel) getData()).name + "】感兴趣";
    }else if (type == Type.RESUME){
      return "简历投递【" + ((ResumeModel) getData()).username + "的简历】";
    }else if (type == Type.TOP_RESUME){
      return "对【" + ((ResumeModel) getData()).username + "的简历】感兴趣";
    }else if(type == Type.SEND_RECRUIT){
      return "职位邀请【" + ((RecruitModel) getData()).name + "】";
    }else {
      return "";
    }
  }

  /**
   * 保存消息或消息文件
   */
  @Override public void save() {

  }

  public enum Type {
    TYPING, INVALID, RECRUIT, RESUME, SEND_RECRUIT, TOP_RESUME,
  }
}
