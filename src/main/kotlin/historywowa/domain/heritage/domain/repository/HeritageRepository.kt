package historywowa.domain.heritage.domain.repository;

import historywowa.domain.heritage.domain.entity.Heritage;
import historywowa.domain.member.domain.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {

    List<Heritage> findByMember(Member member);
}
