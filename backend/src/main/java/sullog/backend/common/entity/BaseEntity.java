package sullog.backend.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

import java.time.Instant;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity {

    private final Instant createdAt;

    private final Instant updatedAt;

    private final Instant deletedAt;

    public BaseEntity(
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public BaseEntity() {
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

}