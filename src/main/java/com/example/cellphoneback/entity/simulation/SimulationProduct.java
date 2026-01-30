package com.example.cellphoneback.entity.simulation;

import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.entity.operation.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SimulationProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JsonIgnore
    private Simulation simulation;


    @ManyToOne
    private Product product;



}
