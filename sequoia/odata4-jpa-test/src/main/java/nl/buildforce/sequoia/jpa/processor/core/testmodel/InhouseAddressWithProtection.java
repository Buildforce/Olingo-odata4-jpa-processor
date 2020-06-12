package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InhouseAddressWithProtection {

  @Column(name = "\"Task\"", length = 32, nullable = false) // Workaround olingo problem
  private String taskID;
  @Column(name = "\"Building\"", length = 10)
  @EdmProtectedBy(name = "BuildingNumber")
  private String building;
  @Column(name = "\"Floor\"")
  private Short floor;
  @Column(name = "\"RoomNumber\"")
  private Integer roomNumber;

  public InhouseAddressWithProtection() {
    // Needed by JPA
  }

  public InhouseAddressWithProtection(final String taskID, final String building) {
    this.setTaskID(taskID);
    this.setBuilding(building);
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public Short getFloor() {
    return floor;
  }

  public void setFloor(Short floor) {
    this.floor = floor;
  }

  public Integer getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(Integer roomNumber) {
    this.roomNumber = roomNumber;
  }

  public String getTaskID() {
    return taskID;
  }

  public void setTaskID(String taskID) {
    this.taskID = taskID;
  }

}