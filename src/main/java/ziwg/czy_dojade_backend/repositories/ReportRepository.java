package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Report;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReportId(Long reportId);
    Optional<Report> findByReportId(Long reportId);
    List<Report> findAllByAccident_Id(Long accidentId);

    @Query("SELECT r FROM Report r WHERE r.timeOfReport BETWEEN :startTime AND :endTime")
    List<Report> findAllByTimeOfReportBetween(String startTime, String endTime);
}
