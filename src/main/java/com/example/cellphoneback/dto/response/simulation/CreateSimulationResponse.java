
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.simulation.Simulation;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateSimulationResponse {
    Simulation simulation;

}
