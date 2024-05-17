package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ziwg.czy_dojade_backend.models.Accident;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Report;
import ziwg.czy_dojade_backend.repositories.AccidentRepository;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;
import ziwg.czy_dojade_backend.repositories.ReportRepository;
import ziwg.czy_dojade_backend.services.interfaces.IReportService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository reportRepository;
    private final AccidentRepository accidentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    @Scheduled(cron = "0 */15 * * * ?") //  every 15 minutes
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteExpiredReports() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeHoursAgo = now.minusHours(3);

        List<Report> reports = reportRepository
                .findAllByTimeOfReportBefore(
                        threeHoursAgo
                );

        reports.forEach(report -> {
            AppUser user = report.getUser();
            Accident accident = report.getAccident();

            if (user != null){
                user.getReports().remove(report);
                appUserRepository.saveAndFlush(user);
            }
            if (accident != null){
                accident.getReports().remove(report);
                if (accident.getReports().isEmpty()){
                    accidentRepository.delete(accident);
                }
                else{
                    accidentRepository.saveAndFlush(accident);
                }
            }

            reportRepository.delete(report);
        });

    }
}
