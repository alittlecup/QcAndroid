package cn.qingchengfit.writeoff.vo;

public interface ITicketDetailData  extends IWriteOffItemData{
  String getTicketNumber();
  String getTicketStatus();
  String getTicketStartEnd();
  String getTicketBuyTime();
  String getTicketGymName();
  String getTicketBatchType();
  String getTicketBatch();
  String getTicketBatchTrainer();
  String getTicketBatchMemberCount();
  String getTicketBatchTime();
  boolean isTicketChecked();

}
