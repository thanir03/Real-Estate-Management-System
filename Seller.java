import java.time.LocalDateTime;
import java.util.ArrayList;

// Seller class

public class Seller extends User {
  private ArrayList<String> propertyListId;

  public Seller(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  public void setPropertyList(ArrayList<String> propertyList) {
    this.propertyListId = propertyList;
  }

  public ArrayList<Property> getPropertyList() {
    ArrayList<Property> properties = PropertyDatabase.read();
    ArrayList<Property> sellerProperties = new ArrayList<>();
    for (Property property : properties) {
      if (property.getSellerId().equals(getCredential().getUsername())) {
        sellerProperties.add(property);
      }
    }
    return sellerProperties;
  }

  public ArrayList<String> getPropertyListId() {
    return propertyListId;
  }

  public boolean addProperty(String propertyId) {
    propertyListId.add(propertyId);
    return true;
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
    for (int i = 0; i < propertyListId.size(); i++) {
      str += propertyListId.get(i) + (i == propertyListId.size() - 1 ? "]" : Helper.arraySplitter);
    }
    if (propertyListId.size() == 0)
      str += "]";
    str += "\n";
    return str;
  }

  @Override
  public String toString() {
    String str = "Name : " + getFullName() + " , Email Address : " + getEmailAddress() + " , Phone Number : "
        + getPhoneNum();
    return str;
  }

}
