//package org.aidtracker.backend.dao;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.aidtracker.backend.AccountEnvBaseTest;
//import org.aidtracker.backend.domain.demand.Demand;
//import org.aidtracker.backend.domain.demand.DemandStatusEnum;
//import org.aidtracker.backend.web.dto.PublicDemandListQueryTypeEnum;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author mtage
// * @since 2020/8/4 20:06
// */
//@SpringBootTest
//class DemandRepositoryTest extends AccountEnvBaseTest {
//    @Autowired
//    DemandRepository demandRepository;
//
//    @Test
//    void findAll() throws JsonProcessingException {
//        Specification<Demand> specification = new Specification<Demand>() {
//            @Override
//            public Predicate toPredicate(Root<Demand> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicateList = new ArrayList<>();
//                predicateList.add(criteriaBuilder.notEqual(root.get("status"), DemandStatusEnum.CLOSED));
//                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
//            }
//        };
//        Page<Demand> result = demandRepository.findAll(DemandStatusEnum.CLOSED.name(),0, 100,
//                BigDecimal.valueOf(39.53d), BigDecimal.valueOf(116.42d));
//        printResult(result);
//    }
//}