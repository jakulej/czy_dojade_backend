package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsById(Long reportId);
    Optional<Report> findById(Long reportId);
    List<Report> findAllByAccident_Id(Long accidentId);
    @Query("SELECT r FROM Report r WHERE r.timeOfReport <= :endTime")
    List<Report> findAllByTimeOfReportBefore(@Param("endTime") LocalDateTime endTime);
}
