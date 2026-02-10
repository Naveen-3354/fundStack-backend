package com.test.FundStack.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Naveen
 *
 * History
 * -03-03-2025 <NaveenDhanasekaran> BaseEntity
 *      - InitialVersion
 * -05-03-2025 <NaveenDhanasekaran>
 *      - Added Json Property
 * -14-10-2025 <NaveenDhanasekaran>
 *      - Added created by and updated by fields
 */

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class BaseEntity {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_by")
    private String updatedBy;
}