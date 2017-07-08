package cn.qingchengfit.recruit.network.body;

import cn.qingchengfit.recruit.model.Job;
import java.util.List;

/**
 * Created by fb on 2017/7/7.
 */

public class InviteBody {

  public List<String> job_ids;
  public String resume_id;
  public String fair_id;

  public InviteBody(List<String> job_ids, String resume_id) {
    this.job_ids = job_ids;
    this.resume_id = resume_id;
  }

  public InviteBody(List<String> job_ids, String resume_id, String fair_id) {
    this.job_ids = job_ids;
    this.resume_id = resume_id;
    this.fair_id = fair_id;
  }

  public static InviteBody build(List<String> job_ids, String resume_id){
    return new InviteBody(job_ids, resume_id);
  }

  public static InviteBody build(List<String> job_ids, String resume_id, String fair_id){
    return new InviteBody(job_ids, resume_id, fair_id);
  }
}
