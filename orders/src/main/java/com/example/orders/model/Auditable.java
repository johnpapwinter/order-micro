package com.example.orders.model;

import com.example.orders.utils.listeners.DefaultAuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(DefaultAuditListener.class)
public abstract class Auditable {

    @CreatedDate
    @Column(name = "created_date")
    @NotAudited
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    protected LocalDateTime lastModificationDate;


}
