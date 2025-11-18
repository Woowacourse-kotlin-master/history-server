package historywowa.global.jwt.domain.repository;


import historywowa.domain.oauth2.domain.entity.SocialProvider;
import historywowa.global.jwt.domain.entity.SocialToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialTokenRepository extends CrudRepository<SocialToken, String> {
    Optional<SocialToken> findByUserIdAndProvider(String userId, SocialProvider provider);

    void deleteByUserIdAndProvider(String userId, SocialProvider provider);

    void deleteByUserId(String userId);
}