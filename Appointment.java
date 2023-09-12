import java.time.LocalDateTime;
import java.util.ArrayList;

// Mutator methods are only available for dateOfAppointment and status

public class Appointment {
  public static String PENDING_STATUS = "pending";
  public static String COMPLETED_STATUS = "completed";
  public static String ONGOING_STATUS = "on-going";
  public static String CANCELLED_STATUS = "cancelled";

  private String appointmentId;
  private String propertyId;
  private String buyerId;
  private LocalDateTime dateOfAppointment;
  private String status;

  public Appointment(String appointmentId, String buyerId, String propertyId) {
    this.appointmentId = appointmentId;
    this.buyerId = buyerId;
    this.propertyId = propertyId;
  }

  // mutator methods
  public void setDateOfAppointment(LocalDateTime dateOfAppointment) {
    this.dateOfAppointment = dateOfAppointment;
  }

  public void setStatus(String status) {
    if (status.equals(PENDING_STATUS)) {
      this.status = PENDING_STATUS;
    } else if (status.equals(COMPLETED_STATUS)) {
      this.status = COMPLETED_STATUS;
    } else if (status.equals(ONGOING_STATUS)) {
      this.status = ONGOING_STATUS;
    } else if (status.equals(CANCELLED_STATUS)) {
      this.status = CANCELLED_STATUS;
    } else {
      this.status = null;
      System.out.println("Invalid status");
    }
  }

  public String getBuyerId() {
    return buyerId;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public Buyer getBuyer() {
    ArrayList<Buyer> buyerList = BuyerDatabase.read();
    for (Buyer buyer : buyerList) {
      if (buyer.getCredential().getUsername().equals(buyerId))
        return buyer;
    }
    return null;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public LocalDateTime getDateOfAppointment() {
    return dateOfAppointment;
  }

  public Property getProperty() {
    ArrayList<Property> propertyList = PropertyDatabase.read();
    for (Property property : propertyList) {
      if (property.getPropertyId().equals(propertyId))
        return property;
    }
    return null;
  }

  public String getStatus() {
    return status;
  }

  public String fileString() {
    String str = "";
    str += appointmentId + Helper.writeFileSplitter;
    str += propertyId + Helper.writeFileSplitter;
    str += buyerId + Helper.writeFileSplitter;
    str += dateOfAppointment.format(Helper.dateFormat) + Helper.writeFileSplitter;
    str += status + "\n";
    return str;
  }

  public String toString() {
    String str = "";
    str += "Appointment ID : " + appointmentId + "\n";
    str += "Status : " + getStatus() + "\n";
    str += "Date of Appointment : " + dateOfAppointment.format(Helper.dateFormat);
    str += "\n\nBuyer Details \n" + getBuyer().toString() + "\n";
    str += "Property Details \n\n" + getProperty().toString() + "\n";
    return str;
  }

}
