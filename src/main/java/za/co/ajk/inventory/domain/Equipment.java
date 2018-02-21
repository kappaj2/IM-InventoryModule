package za.co.ajk.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "equipment_id", nullable = false)
    private Integer equipmentId;

    @NotNull
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @NotNull
    @Column(name = "equipment_group", nullable = false)
    private String equipmentGroup;

    @Column(name = "equipment_serial_number")
    private String equipmentSerialNumber;

    @Column(name = "equipment_barcode")
    private String equipmentBarcode;

    @Column(name = "equipment_description")
    private String equipmentDescription;

    @NotNull
    @Column(name = "currently_available", nullable = false)
    private Boolean currentlyAvailable;

    @NotNull
    @Column(name = "date_added_to_stock", nullable = false)
    private Instant dateAddedToStock;

    @NotNull
    @Column(name = "added_by", nullable = false)
    private String addedBy;

    @Column(name = "date_removed_from_stock")
    private Instant dateRemovedFromStock;

    @Column(name = "removed_by")
    private String removedBy;

    @OneToMany(mappedBy = "equipment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EquipmentActivity> equipmentActivities = new HashSet<>();

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public Equipment equipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
        return this;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Equipment equipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        return this;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentGroup() {
        return equipmentGroup;
    }

    public Equipment equipmentGroup(String equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
        return this;
    }

    public void setEquipmentGroup(String equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
    }

    public String getEquipmentSerialNumber() {
        return equipmentSerialNumber;
    }

    public Equipment equipmentSerialNumber(String equipmentSerialNumber) {
        this.equipmentSerialNumber = equipmentSerialNumber;
        return this;
    }

    public void setEquipmentSerialNumber(String equipmentSerialNumber) {
        this.equipmentSerialNumber = equipmentSerialNumber;
    }

    public String getEquipmentBarcode() {
        return equipmentBarcode;
    }

    public Equipment equipmentBarcode(String equipmentBarcode) {
        this.equipmentBarcode = equipmentBarcode;
        return this;
    }

    public void setEquipmentBarcode(String equipmentBarcode) {
        this.equipmentBarcode = equipmentBarcode;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public Equipment equipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
        return this;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public Boolean isCurrentlyAvailable() {
        return currentlyAvailable;
    }

    public Equipment currentlyAvailable(Boolean currentlyAvailable) {
        this.currentlyAvailable = currentlyAvailable;
        return this;
    }

    public void setCurrentlyAvailable(Boolean currentlyAvailable) {
        this.currentlyAvailable = currentlyAvailable;
    }

    public Instant getDateAddedToStock() {
        return dateAddedToStock;
    }

    public Equipment dateAddedToStock(Instant dateAddedToStock) {
        this.dateAddedToStock = dateAddedToStock;
        return this;
    }

    public void setDateAddedToStock(Instant dateAddedToStock) {
        this.dateAddedToStock = dateAddedToStock;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Equipment addedBy(String addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Instant getDateRemovedFromStock() {
        return dateRemovedFromStock;
    }

    public Equipment dateRemovedFromStock(Instant dateRemovedFromStock) {
        this.dateRemovedFromStock = dateRemovedFromStock;
        return this;
    }

    public void setDateRemovedFromStock(Instant dateRemovedFromStock) {
        this.dateRemovedFromStock = dateRemovedFromStock;
    }

    public String getRemovedBy() {
        return removedBy;
    }

    public Equipment removedBy(String removedBy) {
        this.removedBy = removedBy;
        return this;
    }

    public void setRemovedBy(String removedBy) {
        this.removedBy = removedBy;
    }

    public Set<EquipmentActivity> getEquipmentActivities() {
        return equipmentActivities;
    }

    public Equipment equipmentActivities(Set<EquipmentActivity> equipmentActivities) {
        this.equipmentActivities = equipmentActivities;
        return this;
    }

    public Equipment addEquipmentActivity(EquipmentActivity equipmentActivity) {
        this.equipmentActivities.add(equipmentActivity);
        equipmentActivity.setEquipment(this);
        return this;
    }

    public Equipment removeEquipmentActivity(EquipmentActivity equipmentActivity) {
        this.equipmentActivities.remove(equipmentActivity);
        equipmentActivity.setEquipment(null);
        return this;
    }

    public void setEquipmentActivities(Set<EquipmentActivity> equipmentActivities) {
        this.equipmentActivities = equipmentActivities;
    }

    public Company getCompany() {
        return company;
    }

    public Equipment company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Equipment equipment = (Equipment) o;
        if (equipment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Equipment{" +
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
