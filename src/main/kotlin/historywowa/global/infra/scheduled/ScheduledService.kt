package historywowa.global.infra.scheduled;

import historywowa.domain.point.domain.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduledService {

    private final PointRepository pointRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetPointsAtMidnight() {
        pointRepository.updateAllBalanceToFixedValue(2);
    }

}
