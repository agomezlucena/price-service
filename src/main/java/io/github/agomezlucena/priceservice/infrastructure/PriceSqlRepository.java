package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.criteria.InvalidPriceCriteriaException;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryTranslator;
import io.github.agomezlucena.priceservice.infrastructure.criteria.PriceInfoCriteriaSqlQuery;
import io.github.agomezlucena.priceservice.infrastructure.criteria.PriceInfoCriteriaSqlTranslator;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the {@link PriceRepository} interface using SQL for data retrieval.
 * This class is marked as a Spring repository and interacts with the database through
 * the {@code JdbcClient}.
 * <p>
 * The main purpose of this repository is to query price information based on the brand ID,
 * product ID, and application date or using criteria.
 */
@Repository
public class PriceSqlRepository implements PriceRepository {
    private final JdbcClient jdbcClient;
    private final PriceInfoQueryTranslator<PriceInfoCriteriaSqlQuery> sqlTranslator;

    @Autowired
    public PriceSqlRepository(JdbcClient jdbcClient, PriceInfoQueryTranslator<PriceInfoCriteriaSqlQuery> sqlTranslator) {
        this.jdbcClient = jdbcClient;
        this.sqlTranslator = sqlTranslator;
    }

    /**
     * Finds price information based on the given criteria, ensuring that only a single result is returned.
     * If the criteria do not specify exactly one result, an {@code InvalidPriceCriteriaException} is thrown.
     * The method performs a database query using the provided criteria and translates the result set
     * into an {@code Optional<PriceInfo>} instance.
     *
     * @param criteria the {@code PriceInfoQuery} instance that defines the filtering,
     *                 sorting, and limiting conditions for the query results. Must not be null
     *                 and must specify exactly one result via {@code isForSingleResult()}.
     * @return an {@code Optional<PriceInfo>} containing the retrieved price information if found,
     *         or an empty {@code Optional} if no result satisfies the criteria.
     * @throws InvalidPriceCriteriaException if the provided criteria do not enforce a single result.
     */
    @Override
    @Timed("price.database.query.timespent")
    public Optional<PriceInfo> findPriceInfoByCriteria(PriceInfoQuery criteria) {
        if (!criteria.isForSingleResult()) {
            throw new InvalidPriceCriteriaException("this method allow to extract only one result");
        }

        var query = sqlTranslator.translate(criteria);

        return jdbcClient.sql(query.sql())
                .params(query.parameters())
                .withMaxRows(criteria.limit())
                .query((rs, _) -> new PriceInfo(
                        rs.getInt("brand_id"),
                        rs.getInt("product_id"),
                        rs.getInt("price_list"),
                        rs.getObject("start_date", LocalDateTime.class),
                        rs.getObject("end_date", LocalDateTime.class),
                        rs.getBigDecimal("price"),
                        rs.getString("currency")
                )).optional();
    }
}
