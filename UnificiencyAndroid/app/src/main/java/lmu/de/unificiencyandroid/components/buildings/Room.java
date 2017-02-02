package lmu.de.unificiencyandroid.components.buildings;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class Room {
  private String name;
  private String level;
  private String address;
  private LocalTime freeFrom;
  private LocalTime freeUntil;
  private Boolean available;
  private Long interval;

  public enum State {
    NOW_FREE, SOON_FREE, LONG_WAITING, TAKEN
  }

  private State state;

  public Room(String name, String level, String address, LocalTime freeFrom, LocalTime freeUntil) {
    this.name = name;
    this.level = level;
    this.address = address;
    this.freeFrom = freeFrom;
    this.freeUntil = freeUntil;
    this.interval = Math.abs(TimeUnit.MILLISECONDS.toMinutes(freeFrom.getMillisOfDay() - freeUntil.getMillisOfDay()));
    evalState();
  }

  public void evalState(){
    long untilFreeMinutes = untilFreeMinutes();
    boolean freeFromNow = freeFromNowOn();
    if(freeFromNow) {
      state = State.NOW_FREE;
    }
    else if(untilFreeMinutes > 0 && untilFreeMinutes <= 35) {
      state = State.SOON_FREE;
    } else if (untilFreeMinutes > 0 && untilFreeMinutes > 35 && untilFreeMinutes <= 70) {
      state = State.LONG_WAITING;
    } else {
      state = State.TAKEN;
    }
  }

  public State getState(){
    evalState();
    return state;
  }

  @Override
  public String toString() {
    return name + " (" + level + ")";
  }

  public long untilFreeMinutes(){
    DateTime dt = new DateTime();
    // if negative, current time is later than freeFrom
    return TimeUnit.MILLISECONDS.toMinutes(this.freeFrom.getMillisOfDay() - dt.getMillisOfDay());
  }

  public String availabilityString() {
    evalState();
    String message;
    switch (state) {
      case NOW_FREE:
        message = "noch " + freeForMinutes() + " mins frei";
        break;
      case SOON_FREE: case LONG_WAITING:
        message = "in " + untilFreeMinutes() +" mins für " + interval + " mins frei";
        break;
      case TAKEN:
        message = "nicht mehr frei";
        break;
      default:
        message = "Keine Informationen";
    }
      return message;

  }

  public Boolean freeFromNowOn() {
    Interval interval = new Interval(this.freeFrom.toDateTimeToday(), this.freeUntil.toDateTimeToday());
    return interval.containsNow();
  }

  public Integer freeForMinutes() {
    Integer minutes = Minutes.minutesBetween(new DateTime().toLocalTime(), this.freeUntil).getMinutes();
    return minutes;
  }

}
