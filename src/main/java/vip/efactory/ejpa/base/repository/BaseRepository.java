package vip.efactory.ejpa.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import vip.efactory.ejpa.base.entity.BaseEntity;

import java.util.List;

/**
 * Description:项目自定义的一些常用的扩展
 *
 * @author dbdu
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends ReactiveSortingRepository<T, ID>, ReactiveQuerydslPredicateExecutor<T>, ReactiveQueryByExampleExecutor<T> {

    /**
     * Description:使用条件查询，不分页
     *
     * @param spec 高级条件
     * @return 列表集合
     * @author dbdu
     */
    Flux<T> findAll(Specification<T> spec);

    /**
     * Description:获取前25条数据
     *
     * @return 列表集合
     * @author dbdu
     */
    Flux<T> findTop25ByOrderByIdDesc();
}
