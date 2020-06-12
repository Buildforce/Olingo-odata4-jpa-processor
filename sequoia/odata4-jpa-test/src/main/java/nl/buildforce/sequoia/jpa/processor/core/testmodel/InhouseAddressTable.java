package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@EdmIgnore
@Entity
@Table(schema = "\"OLINGO\"", name = "\"InhouseAddress\"")
public class InhouseAddressTable {

  @Id
  @Column(name = "\"ID\"")
  private String iD;

  @Column(name = "\"Task\"", length = 32, nullable = false) // Workaround Olingo problem
  private String taskID;
  @Column(name = "\"Building\"", length = 10)
  private String building;
  @Column(name = "\"Floor\"")
  private Short floor;
  @Column(name = "\"RoomNumber\"")
  private Integer roomNumber;

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