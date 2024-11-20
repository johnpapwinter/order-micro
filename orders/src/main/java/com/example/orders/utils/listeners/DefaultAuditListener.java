package com.example.orders.utils.listeners;

import com.example.orders.model.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class DefaultAuditListener {

    @PrePersist
    public void setCreatedOn(Object entity) {
        if (entity != null) {
            if (entity instanceof Auditable auditable) {
                LocalDateTime now = LocalDateTime.now();
                auditable.setCreatedDate(now);
            }
        }
    }

    @PreUpdate
    public void setModifiedOn(Object entity) {
        if (entity != null) {
            if (entity instanceof Auditable auditable) {
                LocalDateTime now = LocalDateTime.now();
                auditable.setLastModificationDate(now);
            }
        }
    }

}
