package consumo.worpress.clases.repository;


import consumo.worpress.clases.entity.IntegrationLog;
import consumo.worpress.clases.enums.LogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, Long> {

    // Spring Data JPA genera la implementación automáticamente
    // basándose en el nombre del metodo (Query Methods)

    List<IntegrationLog> findByTargetSystem(String targetSystem);

    List<IntegrationLog> findByStatus(LogStatus status);

    List<IntegrationLog> findByTargetSystemAndStatus(String system, LogStatus status);

}