package historywowa.domain.point.domain.repository

import historywowa.domain.member.domain.entity.Member
import historywowa.domain.point.domain.entity.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PointRepository : JpaRepository<Point, Long> {

    fun findByMember(member: Member): Optional<Point>

    @Modifying
    @Query("UPDATE Point p SET p.balance = :value")
    fun updateAllBalanceToFixedValue(@Param("value") value: Int)
}