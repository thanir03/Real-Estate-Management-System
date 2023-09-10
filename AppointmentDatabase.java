import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

// functions for reading and writing to Appointment.txt 

public class AppointmentDatabase {
  private static final String fileDir = "Files/Appointment.txt";

  private static final String appointmentFileHeader = "appointmentId | propertyId | buyerId | dateOfAppointment | status\n\n";

  public static ArrayList<Appointment> read() {
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    try {
      File file = new File(fileDir);
      Scanner scanner = new Scanner(file);
      scanner.nextLine();
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        appointmentList.add(processLine(line));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Appointment File not found");
    }

    return appointmentList;
  }

  private static Appointment processLine(String str) {
    String[] strlist = str.split(Helper.readfileSplitter);
    String appoinmentId = strlist[0];
    String propertyId = strlist[1];
    String buyerId = strlist[2];
    LocalDateTime dateOfAppointment = LocalDateTime.parse(strlist[3], Helper.dateFormat);
    String status = strlist[4];
    Appointment appoinment = new Appointment(appoinmentId, buyerId, propertyId);
    appoinment.setDateOfAppointment(dateOfAppointment);
    appoinment.setStatus(status);
    return appoinment;
  }

  public static void write(ArrayList<Appointment> appointmentList) {
    try {
      FileWriter fileWriter = new FileWriter(fileDir);
      fileWriter.write(appointmentFileHeader);
      for (Appointment appointment : appointmentList) {
        fileWriter.write(appointment.fileString());
      }
      fileWriter.close();
    } catch (IOException e) {
      System.out.println("Appointment File not found");
    }
  }
}
