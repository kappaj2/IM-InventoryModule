package za.co.ajk.inventory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A EquipmentActivity.
 */
@Entity
@Table(name = "equipment_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "equipmentactivity")
public class EquipmentActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "activity_type_code", nullable = false)
    private String activityTypeCode;

    @NotNull
    @Column(name = "activity_description", nullable = false)
    private String activityDescription;

    @NotNull
    @Column(name = "activity_date", nullable = false)
    private Instant activityDate;

    @NotNull
    @Column(name = "activity_by", nullable = false)
    private String activityBy;

    @ManyToOne
    private Equipment equipment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityTypeCode() {
        return activityTypeCode;
    }

    public EquipmentActivity activityTypeCode(String activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
        return this;
    }

    public void setActivityTypeCode(String activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public EquipmentActivity activityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
        return this;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Instant getActivityDate() {
        return activityDate;
    }

    public EquipmentActivity activityDate(Instant activityDate) {
        this.activityDate = activityDate;
        return this;
    }

    public void setActivityDate(Instant activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityBy() {
        return activityBy;
    }

    public EquipmentActivity activityBy(String activityBy) {
        this.activityBy = activityBy;
        return this;
    }

    public void setActivityBy(String activityBy) {
        this.activityBy = activityBy;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public EquipmentActivity equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EquipmentActivity equipmentActivity = (EquipmentActivity) o;
        if (equipmentActivity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipmentActivity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EquipmentActivity{" +
            "id=" + getId() +
            ", activityTypeCode='" + getActivityTypeCode() + "'" +
            ", activityDescription='" + getActivityDescription() + "'" +
            ", activityDate='" + getActivityDate() + "'" +
            ", activityBy='" + getActivityBy() + "'" +
            "}";
    }
}
