package com.ngvanphuc.identify_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime; // xoá bản ghi hết hạn tránh bảng chứa nhiều bản ghi
}
