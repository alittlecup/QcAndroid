package cn.qingchengfit.recruit;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/6/17.
 */

public class RecruitConstants {
  public static final int RESUME_COMPLETED = 50;
  public static final int JOB_SEARCH = 101;         //求职端 -- 发送简历
  public static final int RECRUIT = 102;            //招聘端 -- 发送邀约

  public static final String RESUME_WEB_PATH = "/mobile/resume/?id=";

  public static final String IDENTIFY = "identify";
  public static final String CONVERSATION_TYPE = "conversation_type";                 //会话类型
  public static final String TEMP_CONVERSATION_TYPE = "temp_conversation_type";
  //临时会话类型（不可以直接传会话类型时使用）
  public static final String CHAT_AVATAR = "conversation_avatar";
  public static final String CHAT_NICKNAME = "conversation_nick_name";
  public static final String CHAT_JOB_RESUME = "conversation_job_resume";         //简历信息
  public static final String CHAT_RECRUIT = "conversation_job_recruit";           //求职信息
  public static final String CHAT_RECRUIT_STATE = "conversation_recruit_state";   //是否发送过简历

  public static final String CHAT_JOB_ID = "send_resume_job_id";          //jobId
  public static final String PREFRENCE_USERSIG = "prefrence_usersig";
  public static final String VALUE_USERSIG = "usersig";
  public static final String PREFRENCE_AVATAR = "prefrence_avatar";
  public static final String VALUE_AVATAR = "avatar";
  public static final String PREFRENCE_IDENTIFY = "prefrence_identify";
  public static final String VALUE_IDENTIFY = "identify";
  public static final String SEND_RESUME = "send_resume";               //打开聊天页面是否需要同时发送简历
  public static final String CHAT_JOB_SEARCH_OR_RECRUIT = "job_search_or_recruit";
  //聊天是求职端还是招聘端

  public static final int C2C = 1080;
  public static final int GROUP = 1081;
}
