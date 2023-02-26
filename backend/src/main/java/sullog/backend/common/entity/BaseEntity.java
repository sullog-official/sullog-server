package sullog.backend.common.entity;

import lombok.ToString;

import java.time.Instant;

@ToString
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