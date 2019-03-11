package com.schinta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.schinta.domain.WxUser;
import com.schinta.domain.*; // for static metamodels
import com.schinta.repository.WxUserRepository;
import com.schinta.service.dto.WxUserCriteria;

/**
 * Service for executing complex queries for WxUser entities in the database.
 * The main input is a {@link WxUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WxUser} or a {@link Page} of {@link WxUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WxUserQueryService extends QueryService<WxUser> {

    private final Logger log = LoggerFactory.getLogger(WxUserQueryService.class);

    private final WxUserRepository wxUserRepository;

    public WxUserQueryService(WxUserRepository wxUserRepository) {
        this.wxUserRepository = wxUserRepository;
    }

    /**
     * Return a {@link List} of {@link WxUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WxUser> findByCriteria(WxUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WxUser> specification = createSpecification(criteria);
        return wxUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WxUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WxUser> findByCriteria(WxUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WxUser> specification = createSpecification(criteria);
        return wxUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WxUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WxUser> specification = createSpecification(criteria);
        return wxUserRepository.count(specification);
    }

    /**
     * Function to convert WxUserCriteria to a {@link Specification}
     */
    private Specification<WxUser> createSpecification(WxUserCriteria criteria) {
        Specification<WxUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WxUser_.id));
            }
            if (criteria.getWxNickName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxNickName(), WxUser_.wxNickName));
            }
            if (criteria.getWxGender() != null) {
                specification = specification.and(buildSpecification(criteria.getWxGender(), WxUser_.wxGender));
            }
            if (criteria.getWxCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxCountry(), WxUser_.wxCountry));
            }
            if (criteria.getWxProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxProvince(), WxUser_.wxProvince));
            }
            if (criteria.getWxCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxCity(), WxUser_.wxCity));
            }
            if (criteria.getWxHeadimgurl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxHeadimgurl(), WxUser_.wxHeadimgurl));
            }
            if (criteria.getWxUnionid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxUnionid(), WxUser_.wxUnionid));
            }
            if (criteria.getWxLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWxLanguage(), WxUser_.wxLanguage));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), WxUser_.gender));
            }
            if (criteria.getUserStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStatus(), WxUser_.userStatus));
            }
            if (criteria.getRegisterChannel() != null) {
                specification = specification.and(buildSpecification(criteria.getRegisterChannel(), WxUser_.registerChannel));
            }
//            if (criteria.getRegisterDateTime() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getRegisterDateTime(), WxUser_.registerDateTime));
//            }
            if (criteria.getPushLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPushLimit(), WxUser_.pushLimit));
            }
//            if (criteria.getPropertiesId() != null) {
//                specification = specification.and(buildReferringEntitySpecification(criteria.getPropertiesId(), WxUser_.properties, UserProperty_.id));
//            }
//            if (criteria.getDemandsId() != null) {
//                specification = specification.and(buildReferringEntitySpecification(criteria.getDemandsId(), WxUser_.demands, UserDemand_.id));
//            }
            if (criteria.getSubmitsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSubmitsId(), WxUser_.submits, FormSubmit_.id));
            }
            if (criteria.getAMatchesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAMatchesId(), WxUser_.aMatches, UserMatch_.id));
            }
            if (criteria.getBMatchesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBMatchesId(), WxUser_.bMatches, UserMatch_.id));
            }
            if (criteria.getPushRecordsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPushRecordsId(), WxUser_.pushRecords, PushRecord_.id));
            }
            if (criteria.getBrokerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBrokerId(), WxUser_.broker, Broker_.id));
            }
        }
        return specification;
    }
}
