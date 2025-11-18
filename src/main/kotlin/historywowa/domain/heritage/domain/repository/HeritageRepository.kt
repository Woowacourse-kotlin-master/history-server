package historywowa.domain.heritage.domain.repository

import historywowa.domain.heritage.domain.entity.Heritage
import historywowa.domain.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HeritageRepository : JpaRepository<Heritage, Long> {

    fun findByMember(member: Member): List<Heritage>
}
