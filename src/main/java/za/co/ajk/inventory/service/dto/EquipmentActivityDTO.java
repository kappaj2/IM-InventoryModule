package za.co.ajk.inventory.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the EquipmentActivity entity.
 */
public class EquipmentActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String activityTypeCode;

    @NotNull
    private String activityDescription;

    @NotNull
    private Instant activityDate;

    @NotNull
    private String activityBy;

    private Long equipmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityTypeCode() {
        return activityTypeCode;
    }

    public void setActivityTypeCode(String activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Instant getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Instant activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityBy() {
        return activityBy;
    }

    public void setActivityBy(String activityBy) {
        this.activityBy = activityBy;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EquipmentActivityDTO equipmentActivityDTO = (EquipmentActivityDTO) o;
        if(equipmentActivityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipmentActivityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EquipmentActivityDTO{" +
            "id=" + getId() +
            ", activityTypeCode='" + getActivityTypeCode() + "'" +
            ", activityDescription='" + getActivityDescription() + "'" +
            ", activityDate='" + getActivityDate() + "'" +
            ", activityBy='" + getActivityBy() + "'" +
            "}";
    }
}
