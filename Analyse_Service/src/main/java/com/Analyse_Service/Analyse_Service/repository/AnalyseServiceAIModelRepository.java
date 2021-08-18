package com.Analyse_Service.Analyse_Service.repository;

import com.Analyse_Service.Analyse_Service.dataclass.AIModel;
import com.Analyse_Service.Analyse_Service.dataclass.AIType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalyseServiceAIModelRepository extends JpaRepository<AIModel,Long> {

    @Query(
            value = "SELECT MAX(accuracy) FROM AIModel a WHERE P.type = ?1 ",
            nativeQuery = true)
    List<AIModel> findHighestAccuracyByType(AIType type);
}
