package com.pw.facetnav.model.attribute;

import com.pw.facetnav.model.ad.Ad;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "value"})
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer value;

    @ManyToMany(mappedBy = "attributes")
    private Set<Ad> ads;
}
