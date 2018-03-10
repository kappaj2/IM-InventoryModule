package za.co.ajk.inventory.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Equipment entity.
 */
public class EquipmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer equipmentId;

    @NotNull
    private String equipmentName;

    @NotNull
    private String equipmentGroup;

    private String equipmentSerialNumber;

    private String equipmentBarcode;

    private String equipmentDescription;

    @NotNull
    private Boolean currentlyAvailable;

    @NotNull
    private Instant dateAddedToStock;

    @NotNull
    private String addedBy;

    private Instant dateRemovedFromStock;

    private String removedBy;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentGroup() {
        return equipmentGroup;
    }

    public void setEquipmentGroup(String equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
    }

    public String getEquipmentSerialNumber() {
        return equipmentSerialNumber;
    }

    public void setEquipmentSerialNumber(String equipmentSerialNumber) {
        this.equipmentSerialNumber = equipmentSerialNumber;
    }

    public String getEquipmentBarcode() {
        return equipmentBarcode;
    }

    public void setEquipmentBarcode(String equipmentBarcode) {
        this.equipmentBarcode = equipmentBarcode;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public Boolean isCurrentlyAvailable() {
        return currentlyAvailable;
    }

    public void setCurrentlyAvailable(Boolean currentlyAvailable) {
        this.currentlyAvailable = currentlyAvailable;
    }

    public Instant getDateAddedToStock() {
        return dateAddedToStock;
    }

    public void setDateAddedToStock(Instant dateAddedToStock) {
        this.dateAddedToStock = dateAddedToStock;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Instant getDateRemovedFromStock() {
        return dateRemovedFromStock;
    }

    public void setDateRemovedFromStock(Instant dateRemovedFromStock) {
        this.dateRemovedFromStock = dateRemovedFromStock;
    }

    public String getRemovedBy() {
        return removedBy;
    }

    public void setRemovedBy(String removedBy) {
        this.removedBy = removedBy;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EquipmentDTO equipmentDTO = (EquipmentDTO) o;
        if(equipmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EquipmentDTO{" +
            "id=" + getId() +
            ", equipmentId=" + getEquipmentId() +
            ", equipmentName='" + getEquipmentName() + "'" +
            ", equipmentGroup='" + getEquipmentGroup() + "'" +
            ", equipmentSerialNumber='" + getEquipmentSerialNumber() + "'" +
            ", equipmentBarcode='" + getEquipmentBarcode() + "'" +
            ", equipmentDescription='" + getEquipmentDescription() + "'" +
            ", currentlyAvailable='" + isCurrentlyAvailable() + "'" +
            ", dateAddedToStock='" + getDateAddedToStock() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            ", dateRemovedFromStock='" + getDateRemovedFromStock() + "'" +
            ", removedBy='" + getRemovedBy() + "'" +
            "}";
    }
}
