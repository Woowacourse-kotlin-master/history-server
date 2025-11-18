package historywowa.domain.point.domain.repository;

import feign.Param;
import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.point.domain.entity.Point;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMember(Member member);

    @Modifying
    @Query("UPDATE Point p SET p.balance = :value")
    void updateAllBalanceToFixedValue(@Param("value") int value);
}
