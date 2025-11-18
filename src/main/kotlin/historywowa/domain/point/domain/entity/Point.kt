package historywowa.domain.point.domain.entity

import historywowa.domain.member.domain.entity.Member
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@Table(name = "point")
@DynamicUpdate
class Point(
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id", nullable = false, unique = true)
        val member: Member,

        @Column(name = "balance", nullable = false)
        var balance: Long = 2L,

        @Column(name = "apply_event_count", nullable = false)
        @Comment("이벤트 응모권 수 하루 최대 5회")
        var applyEventCount: Long = 0L
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime? = null

    fun addApplyEventCnt() {
        this.applyEventCount += 1
    }

    fun initApplyEventCnt() {
        this.applyEventCount = 0L
    }

    fun minusBalance() {
        this.balance -= 1
    }

    fun subtractBalance(amount: Long) {
        this.balance -= amount
    }

}