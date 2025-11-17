package historywowa.domain.heritage.domain.repository;

import historywowa.domain.heritage.domain.entity.Heritage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {
}
