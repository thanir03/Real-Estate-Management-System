import java.time.LocalDateTime;

public class User {
  private String fullName;
  private String emailAddress;
  private String phoneNum;
  private LocalDateTime dob;
  private Credential credential;

  public User(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    this.fullName = fullName;
    this.emailAddress = emailAddress;
    this.phoneNum = phoneNum;
    this.dob = dob;
    this.credential = credential;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public void setDob(LocalDateTime dob) {
    this.dob = dob;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public LocalDateTime getDOB() {
    return dob;
  }

  public Credential getCredential() {
    return credential;
  }

}