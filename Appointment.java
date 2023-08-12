import java.time.LocalDateTime;

public class Appointment {
  private String propertyId;
  private String buyerId;
  private LocalDateTime dateOfAppointment;
  private String status;

  public Appointment(String buyerId, String propertyId) {
    this.buyerId = buyerId;
    this.propertyId = propertyId;
  }

  public void setBuyerId(String buyerId) {
    this.buyerId = buyerId;
  }

  public void setDateOfAppointment(LocalDateTime dateOfAppointment) {
    this.dateOfAppointment = dateOfAppointment;
  }

  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getBuyerId() {
    return buyerId;
  }

  public LocalDateTime getDateOfAppointment() {
    return dateOfAppointment;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public String getStatus() {
    return status;
  }

}
