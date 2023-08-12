import java.time.LocalDateTime;

public class Buyer extends User {
  private int[] budgetRange; // array of size 2
  private PropertyPreference propertyPreference;

  public Buyer(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential,
      int[] budgetRange, PropertyPreference propertyPreference) {
    super(fullName, emailAddress, phoneNum, dob, credential);
    this.budgetRange = budgetRange;
    this.propertyPreference = propertyPreference;
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

}
