package vip.efactory.ejpa.base.service;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vip.efactory.ejpa.base.entity.BaseEntity;
import vip.efactory.ejpa.datafilter.DataFilter;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Description:服务层接口的父接口，继承此接口默认下面的这些方法要实现的，采用泛型的写法
 *
 * @author dbdu
 */
public interface IBaseService<T extends BaseEntity, ID> {

    // 查询的方法
    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting the entity with the given id or {@link Mono#empty()} if none found.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<T> findById(ID id);

    /**
     * Retrieves an entity by its id supplied by a {@link Publisher}.
     *
     * @param id must not be {@literal null}. Uses the first emitted element to perform the find-query.
     * @return {@link Mono} emitting the entity with the given id or {@link Mono#empty()} if none found.
     * @throws IllegalArgumentException in case the given {@link Publisher id} is {@literal null}.
     */
    Mono<T> findById(Publisher<ID> id);
    /**
     * Returns a {@link Mono} emitting the entity matching the given {@link Predicate} or {@link Mono#empty()} if none was
     * found.
     *
     * @param predicate must not be {@literal null}.
     * @return a {@link Mono} emitting a single entity matching the given {@link Predicate} or {@link Mono#empty()} if
     *         none was found.
     * @throws IllegalArgumentException if the required parameter is {@literal null}.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the predicate yields more than one
     *           result.
     */
    Mono<T> findOne(Predicate predicate);

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return {@link Flux} emitting the found entities. The size can be equal or less than the number of given
     *         {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
     */
    Flux<T> findAllById(Iterable<ID> ids);

    /**
     * Returns all instances of the type {@code T} with the given IDs supplied by a {@link Publisher}.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param idStream must not be {@literal null}.
     * @return {@link Flux} emitting the found entities.
     * @throws IllegalArgumentException in case the given {@link Publisher idStream} is {@literal null}.
     */
    Flux<T> findAllById(Publisher<ID> idStream);

    /**
     * Returns all instances of the type.
     *
     * @return {@link Flux} emitting all entities.
     */
    Flux<T> findAll();

    /**
     * Returns a {@link Flux} emitting all entities matching the given {@link Predicate}. In case no match could be found,
     * {@link Flux} emits no items.
     *
     * @param predicate must not be {@literal null}.
     * @return a {@link Flux} emitting all entities matching the given {@link Predicate} one by one.
     * @throws IllegalArgumentException if the required parameter is {@literal null}.
     */
    Flux<T> findAll(Predicate predicate);

    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort must not be {@literal null}.
     * @return all entities sorted by the given options.
     * @throws IllegalArgumentException in case the given {@link Sort} is {@literal null}.
     */
    Flux<T> findAll(Sort sort);

    /**
     * Returns a {@link Flux} emitting all entities matching the given {@link Predicate} applying the given {@link Sort}.
     * In case no match could be found, {@link Flux} emits no items.
     *
     * @param predicate must not be {@literal null}.
     * @param sort the {@link Sort} specification to sort the results by, may be {@link Sort#unsorted()}, must not be
     *          {@literal null}.
     * @return a {@link Flux} emitting all entities matching the given {@link Predicate} one by one.
     * @throws IllegalArgumentException if one of the required parameters is {@literal null}.
     */
    Flux<T> findAll(Predicate predicate, Sort sort);

    /**
     * Returns a {@link Flux} emitting all entities matching the given {@link Predicate} applying the given
     * {@link OrderSpecifier}s. In case no match could be found, {@link Flux} emits no items.
     *
     * @param predicate must not be {@literal null}.
     * @param orders the {@link OrderSpecifier}s to sort the results by.
     * @return a {@link Flux} emitting all entities matching the given {@link Predicate} applying the given
     *         {@link OrderSpecifier}s.
     * @throws IllegalArgumentException if one of the required parameter is {@literal null}, or contains a {@literal null}
     *           value.
     */
    Flux<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    /**
     * Returns a {@link Flux} emitting all entities ordered by the given {@link OrderSpecifier}s.
     *
     * @param orders the {@link OrderSpecifier}s to sort the results by.
     * @return a {@link Flux} emitting all entities ordered by the given {@link OrderSpecifier}s.
     * @throws IllegalArgumentException one of the {@link OrderSpecifier OrderSpecifiers} is {@literal null}.
     */
    Flux<T> findAll(OrderSpecifier<?>... orders);
    /**
     * Returns a single entity matching the given {@link Example} or {@link Mono#empty()} if none was found.
     *
     * @param example must not be {@literal null}.
     * @return a single entity matching the given {@link Example} or {@link Mono#empty()} if none was found.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException via {@link Mono#error(Throwable)} if the
     *           example yields more than one result.
     */
    <S extends T> Mono<S> findOne(Example<S> example);

    /**
     * Returns all entities matching the given {@link Example}. In case no match could be found {@link Flux#empty()} is
     * returned.
     *
     * @param example must not be {@literal null}.
     * @return all entities matching the given {@link Example}.
     */
    <S extends T> Flux<S> findAll(Example<S> example);

    /**
     * Returns all entities matching the given {@link Example} applying the given {@link Sort}. In case no match could be
     * found {@link Flux#empty()} is returned.
     *
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
     * @return all entities matching the given {@link Example}.
     */
    <S extends T> Flux<S> findAll(Example<S> example, Sort sort);


    // 保存的方法
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return {@link Mono} emitting the saved entity.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    <S extends T> Mono<S> save(S entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return {@link Flux} emitting the saved entities.
     * @throws IllegalArgumentException in case the given {@link Iterable entities} or one of its entities is
     *           {@literal null}.
     */
    <S extends T> Flux<S> saveAll(Iterable<S> entities);

    /**
     * Saves all given entities.
     *
     * @param entityStream must not be {@literal null}.
     * @return {@link Flux} emitting the saved entities.
     * @throws IllegalArgumentException in case the given {@link Publisher entityStream} is {@literal null}.
     */
    <S extends T> Flux<S> saveAll(Publisher<S> entityStream);

    // 存在判断与计数方法

    /**
     * Returns whether an entity with the given {@literal id} exists.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<Boolean> existsById(ID id);

    /**
     * Returns whether an entity with the given id, supplied by a {@link Publisher}, exists. Uses the first emitted
     * element to perform the exists-query.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} emitting {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@link Publisher id} is {@literal null}.
     */
    Mono<Boolean> existsById(Publisher<ID> id);
    /**
     * 检查数据存储是否包含与给定Predicate匹配的元素。
     *
     * 参数：
     * predicate – 用于存在检查的Predicate ，不能为空。
     * 返回：
     * 如果数据存储包含与给定Predicate匹配的元素，则Mono发出 true ，否则发出 false 。
     * 抛出：IllegalArgumentException – 如果所需参数为空。
     *
     */
    Mono<Boolean> exists(Predicate predicate);

    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @return {@literal true} if the data store contains elements that match the given {@link Example}.
     */
    <S extends T> Mono<Boolean> exists(Example<S> example);
    /**
     * Returns the number of entities available.
     *
     * @return {@link Mono} emitting the number of entities.
     */
    Mono<Long> count();

    /**
     * Returns a {@link Mono} emitting the number of instances matching the given {@link Predicate}.
     *
     * @param predicate the {@link Predicate} to count instances for, must not be {@literal null}.
     * @return a {@link Mono} emitting the number of instances matching the {@link Predicate} or {@code 0} if none found.
     * @throws IllegalArgumentException if the required parameter is {@literal null}.
     */
    Mono<Long> count(Predicate predicate);
    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return the number of instances matching the {@link Example}.
     */
    <S extends T> Mono<Long> count(Example<S> example);


    // 删除的方法
    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<Void> deleteById(ID id);

    /**
     * Deletes the entity with the given id supplied by a {@link Publisher}.
     *
     * @param id must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Publisher id} is {@literal null}.
     */
    Mono<Void> deleteById(Publisher<ID> id);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Mono<Void> delete(T entity);

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Iterable entities} or one of its entities is
     *           {@literal null}.
     */
    Mono<Void> deleteAll(Iterable<? extends T> entities);

    /**
     * Deletes the given entities supplied by a {@link Publisher}.
     *
     * @param entityStream must not be {@literal null}.
     * @return {@link Mono} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@link Publisher entityStream} is {@literal null}.
     */
    Mono<Void> deleteAll(Publisher<? extends T> entityStream);

    /**
     * Deletes all entities managed by the repository.
     *
     * @return {@link Mono} signaling when operation has completed.
     */
    Mono<Void> deleteAll();

    // 上面的方法是框架自带的，下面的是用户自定义的方法


    /**
     * Description: 根据实体的属性名称判断，实体是否存在,
     * 注意：使用此方法，要自己保证属性名称的正确性，否则抛异常！
     * @param propertyName 实体的属性名，暂时支持字符串类型
     * @param propertyValue 实体的属性名对应的值,仅支持简单的基本类型的值为字符串的，不支持其他的自定义类的类型
     * @return boolean true实体存在；false 不存在。
     */
    Mono<Boolean> existsByEntityProperty(String propertyName,String propertyValue) throws NoSuchFieldException;

    /**
     * Description:使用主键批量删除
     *
     * @param var1 可迭代的id集合
     * @return int
     * @author dbdu
     */
    Mono<Integer> deleteAllById(Iterable<ID> var1);

    /**
     * Description:更新实体的方法，很多时候保存和更新的处理逻辑是不一样的，权限也是不一样的，所以单独分开
     *
     * @param var1 要更新的实体
     * @return S
     * @author dbdu
     */
    <S extends T> Mono<S> update(S var1);

//    /**
//     * Description:根据实体的编号，判断数据库中是否存在实体
//     *
//     * @param entityNum 实体编码
//     * @return java.lang.Boolean
//     * @author dbdu
//     */
//    Boolean existsByEntityNum(String entityNum);

    /**
     * Description: 高级模糊查询,查询条件在实体中
     *
     * @param entity 包含高级查询条件的实体
     * @return java.util.List&lt;T&gt;
     * @author dbdu
     */
    Flux<T> advancedQuery(T entity);

    /**
     * Description: 高级模糊查询及分页
     *
     * @param entity   包含高级查询条件的实体
     * @param pageable 分页参数对象
     * @return org.springframework.data.domain.Page&lt;T&gt;
     * @author dbdu
     */
    Flux<T> advancedQuery(T entity, Pageable pageable);


    Flux<T> advancedQuery(T entity, DataFilter dataFilter);

    /**
     * 查询某个属性集合,不包含重复数据
     *
     * @param property 驼峰式的属性
     * @param value    模糊查询的value值
     * @return Set 集合
     */
    Set advanceSearchProperty(String property, String value);

    /**
     * 注册观察者,即哪些组件观察自己，让子类调用此方法实现观察者注册
     */
    @Async
    void registObservers(Observer... observers);

    /**
     * 自己的状态改变了，通知所有依赖自己的组件进行缓存清除，
     * 通常的增删改的方法都需要调用这个方法，来维持缓存一致性
     * @param arg 通知观察者时可以传递礼物arg，即数据，如果不需要数据就传递null;
     */
    @Async
    void notifyOthers(Object arg);

    /**
     * 这是观察别人，别人更新了之后来更新自己的
     * 其实此处不需要被观察者的任何数据，只是为了知道被观察者状态变了，自己的相关缓存也就需要清除了，否则不一致
     * 例如：观察Ａ对象，但是Ａ对象被删除了，那个自己这边关联查询与Ａ有关的缓存都应该清除
     * 子类重写此方法在方法前面加上清除缓存的注解，或者在方法体内具体执行一些清除缓存的代码。
     *
     * @param o   被观察的对象
     * @param arg 传递的数据
     */
    @Async
    void update(Observable o, Object arg);

    /***************************************以下是数据范围相关的查询方法***************************************************/


    /**
     * Description: 高级模糊查询及分页
     *
     * @param entity   包含高级查询条件的实体
     * @param pageable 分页参数对象
     * @param filter   数据过滤对象
     * @return org.springframework.data.domain.Page&lt;T&gt;
     * @author dbdu
     */
    Flux<T> advancedQuery(T entity, Pageable pageable, DataFilter filter);

    /**
     * Description: 带数据过滤的分页对象
     *
     * @param var1   分页对象
     * @param filter 分页对象
     * @return Page
     */
    Flux<T> findAll(Pageable var1, DataFilter filter);

    Flux<T> findAll(DataFilter filter);

    /**
     * 根据查询条件及过滤条件查询列表数据
     *
     * @param spec   查询条件
     * @param filter 数据过滤条件
     * @return List<T>
     */
    Flux<T> getListByFilter(Specification<T> spec, DataFilter filter);

    /**
     * 根据查询条件及过滤条件查询分页数据
     *
     * @param pageable 分页参数对象
     * @param filter   数据过滤条件
     * @return Page<T>
     */
    Flux<T> getPageByFilter(Pageable pageable, DataFilter filter);

    /**
     * 根据查询条件及过滤条件查询分页数据
     *
     * @param pageable 分页参数对象
     * @param spec     查询条件
     * @param filter   数据过滤条件
     * @return Page<T>
     */
    Flux<T> getPageByFilter(Pageable pageable, Specification<T> spec, DataFilter filter);

    Flux<T> getByFilter(DataFilter filter);

    Flux<T> getByFilter(Specification<T> spec, DataFilter filter);

    /**
     * 根据查询条件及过滤条件查询总共有的记录数量
     *
     * @param spec   查询条件
     * @param filter 数据过滤条件
     * @return long 数值
     */
//    <S extends T> Mono<Long> getCountByFilter(Specification<S> spec, DataFilter filter);


    /**
     * 使用基于example的查询条件及过滤条件查询所有的数据
     *
     * @param example 查询条件
     * @param filter  数据过滤条件
     * @param <S>     实体或者实体的子类
     * @return 集合
     */
    //<S extends T> Flux<S> findAllByFilter(Example<S> example, DataFilter filter);

    /**
     * 使用基于example的查询条件及过滤条件查询分页数据
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @param filter   数据过滤条件
     * @param <S>      实体或者实体的子类
     * @return 分页数据
     */
    //<S extends T> Flux<S> findPageByFilter(Example<S> example, Pageable pageable, DataFilter filter);

    /**
     * 使用基于example的查询条件及过滤条件查询匹配的记录数量
     *
     * @param example 查询条件
     * @param filter  数据过滤条件
     * @param <S>     实体或者实体的子类
     * @return 匹配的数量
     */
    //<S extends T> Mono<Long> findCountByFilter(Example<S> example, DataFilter filter);

}
