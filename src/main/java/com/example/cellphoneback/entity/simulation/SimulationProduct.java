package com.example.cellphoneback.entity.simulation;

import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.entity.operation.Product;
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
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;


}
