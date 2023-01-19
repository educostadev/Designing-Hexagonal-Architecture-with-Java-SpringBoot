package dev.educosta.framework.adapters.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "city")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityDTO {

    @Id
    @Column
    String id;
    @Column
    String name;
    @Column
    String state;
}
