package cn.qingchengfit.saasbase.course.batch.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkoutPlan implements Parcelable {
  private String id;
  private String logo;
  private String name;
  private Workout workout;

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WorkoutPlan plan = (WorkoutPlan) o;

    return id.equals(plan.id);
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Workout getWorkout() {
    return workout;
  }

  public void setWorkout(Workout workout) {
    this.workout = workout;
  }

  public static class Workout implements Parcelable {
    String name;
    String id;
    String logo;

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Workout workout = (Workout) o;

      return id.equals(workout.id);
    }

    @Override public int hashCode() {
      return id.hashCode();
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getLogo() {
      return logo;
    }

    public void setLogo(String logo) {
      this.logo = logo;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.name);
      dest.writeString(this.id);
      dest.writeString(this.logo);
    }

    public Workout() {
    }

    protected Workout(Parcel in) {
      this.name = in.readString();
      this.id = in.readString();
      this.logo = in.readString();
    }

    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
      @Override public Workout createFromParcel(Parcel source) {
        return new Workout(source);
      }

      @Override public Workout[] newArray(int size) {
        return new Workout[size];
      }
    };
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.logo);
    dest.writeString(this.name);
    dest.writeParcelable(this.workout, flags);
  }

  public WorkoutPlan() {
  }

  protected WorkoutPlan(Parcel in) {
    this.id = in.readString();
    this.logo = in.readString();
    this.name = in.readString();
    this.workout = in.readParcelable(Workout.class.getClassLoader());
  }

  public static final Parcelable.Creator<WorkoutPlan> CREATOR =
      new Parcelable.Creator<WorkoutPlan>() {
        @Override public WorkoutPlan createFromParcel(Parcel source) {
          return new WorkoutPlan(source);
        }

        @Override public WorkoutPlan[] newArray(int size) {
          return new WorkoutPlan[size];
        }
      };
}
