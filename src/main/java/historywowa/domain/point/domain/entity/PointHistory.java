package historywowa.domain.point.domain.entity;

import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.point.domain.entity.type.PointAction;
import historywowa.domain.point.domain.entity.type.PointCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

@Getter
//@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "point_history")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_category", nullable = false)
    @Comment("포인트별 타입 ex) 이벤트, 문화재")
    private PointCategory pointCategory;


    @Enumerated(EnumType.STRING)
    @Column(name = "point_action", nullable = false)
    @Comment("포인트 액션 타입 ex) EARN, USE")
    private PointAction pointAction;

    @Column(name = "amount", nullable = false)
    @Comment("포인트 변동량")
    private Long amount;

    @Column(name = "balance", nullable = false)
    @Comment("포인트 총 보유량")
    private Long balance;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public PointHistory(Member member, Long amount, Long balance, PointCategory pointCategory, PointAction pointAction) {
        this.member = member;
        this.amount = amount;
        this.balance= balance;
        this.pointCategory = pointCategory;
        this.pointAction = pointAction;
    }

}
