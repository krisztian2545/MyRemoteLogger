package hu.learningproject.myremotelogger.model;

import java.util.Date;

public class LogMessage {

  public String AppName;
  public String Message;
  public String Time;
  
  public String getAppName() {
    return AppName;
  }
  
  public void setAppName(String appName) {
    AppName = appName;
  }
  
  public String getMessage() {
    return Message;
  }
  
  public void setMessage(String message) {
    Message = message;
  }
  
  public String getTime() {
    return Time;
  }
  
  public void setTime(String time) {
    Time = time;
  }
}
