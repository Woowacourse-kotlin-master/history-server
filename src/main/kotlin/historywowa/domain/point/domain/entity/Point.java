package historywowa.domain.point.domain.entity;

import historywowa.domain.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "apply_event_count", nullable = false)
    @Comment("이벤트 응모권 수 하루 최대 5회")
    private Long applyEventCount;


    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Point(Member member) {
        this.member = member;
        this.balance = 2L;
        this.applyEventCount = 0L;
    }

    public void addApplyEventCnt() {
        this.applyEventCount += 1;
    }

    public void initApplyEventCnt() {
        this.applyEventCount = 0L;
    }


    public void minusBalance() {
        this.balance -= 1;
    }

    public void subtractBalance(Long amount) {
        this.balance -= amount;
    }

}
