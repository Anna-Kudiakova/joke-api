package com.example.securetracktraining.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "flags")
@NoArgsConstructor
@AllArgsConstructor
public class Flags {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean nsfw;

    @Column(nullable = false)
    private boolean religious;

    @Column(nullable = false)
    private boolean political;

    @Column(nullable = false)
    private boolean racist;

    @Column(nullable = false)
    private boolean sexist;

    @Column(nullable = false)
    private boolean explicit;


}
