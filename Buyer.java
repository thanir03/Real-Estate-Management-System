import java.time.LocalDateTime;
import java.util.ArrayList;

public class Buyer extends User {
  private int[] budgetRange; // array of size 2
  private PropertyPreference propertyPreference;
  private ArrayList<String> appointmentIdList;

  public Buyer(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  public int[] getBudgetRange() {
    return budgetRange;
  }

  public PropertyPreference getPropertyPreference() {
    return propertyPreference;
  }

  public void setBudgetRange(int[] budgetRange) {
    this.budgetRange = budgetRange;
  }

  public void setPropertyPreference(PropertyPreference propertyPreference) {
    this.propertyPreference = propertyPreference;
  }

  public ArrayList<String> getAppointmenIdtList() {
    // map the appoinment list of buyer using appoinment list
    return appointmentIdList;
  }

  public void setAppointmentIdList(ArrayList<String> appointmentIdList) {
    this.appointmentIdList = appointmentIdList;
  }

}
