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
    // get appointment object from buyer's appointmentIdList
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

  public Appointment hasAppointmentOnProperty(String propertyId) {
    ArrayList<Appointment> appointmentList = getAppointments();
    for (Appointment appointment : appointmentList) {
      if (appointment.getPropertyId().equals(propertyId) &&
          (appointment.getStatus().equals(Appointment.ONGOING_STATUS) ||
              appointment.getStatus().equals(Appointment.PENDING_STATUS))) {
        return appointment;
      }
    }
    return null;
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
    str += getCredential().getUsername() + "|";
    str += getCredential().getPassword() + "|";
    str += getFullName() + "|";
    str += getEmailAddress() + "|";
    str += getDOB().format(Helper.dateFormat) + "|";
    str += getPhoneNum() + "|";
    str += "[";
    for (int i = 0; i < appointmentIdList.size(); i++) {
      str += appointmentIdList.get(i);
      if (i != appointmentIdList.size() - 1) {
        str += ",";
      }
    }
    str += "]" + "\n";
    return str;
  }
}
