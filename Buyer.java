import java.time.LocalDateTime;
import java.util.ArrayList;

public class Buyer extends User {
  private ArrayList<String> appointmentIdList;

  public Buyer(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  public ArrayList<String> getAppointmentIdList() {
    return appointmentIdList;
  }

  public ArrayList<Appointment> getAppointments() {
    ArrayList<Appointment> appointmentList = AppointmentDatabase.read();
    ArrayList<Appointment> buyerAppointment = new ArrayList<>();
    for (String id : appointmentIdList) {
      for (Appointment appointment : appointmentList) {
        if (appointment.getAppointmentId().equals(id)) {
          buyerAppointment.add(appointment);
        }
      }
    }
    return buyerAppointment;
  }

  public void setAppointmentIdList(ArrayList<String> appointmentIdList) {
    this.appointmentIdList = appointmentIdList;
  }

  public void addAppointment(Appointment appointment) {
    appointmentIdList.add(appointment.getAppointmentId());
  }

  public String display() {
    // display brief information of the buyer
    String str = "";
    str += "Full Name : " + this.getFullName() + "\n";
    str += "Email Address : " + this.getEmailAddress() + "\n";
    str += "Phone Number : " + this.getPhoneNum() + "\n";
    return str;
  }

  public String fileString() {
    String str = "";
    str += this.getCredential().getUsername() + Helper.writeFileSplitter;
    str += this.getCredential().getPassword() + Helper.writeFileSplitter;
    str += this.getFullName() + Helper.writeFileSplitter;
    str += this.getEmailAddress() + Helper.writeFileSplitter;
    str += this.getDOB().format(Helper.dateFormat) + Helper.writeFileSplitter;
    str += this.getPhoneNum() + Helper.writeFileSplitter;
    str += "[";
    for (int i = 0; i < appointmentIdList.size(); i++) {
      String appointmentId = appointmentIdList.get(i);
      str += appointmentId;
      if (i != appointmentIdList.size() - 1) {
        str += Helper.arraySplitter;
      }
    }
    str += "]" + "\n";
    return str;
  }
}
