package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
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
    private final PriceInfoCriteriaSqlTranslator sqlTranslator;

    @Autowired
    public PriceSqlRepository(JdbcClient jdbcClient, PriceInfoCriteriaSqlTranslator sqlTranslator) {
        this.jdbcClient = jdbcClient;
        this.sqlTranslator = sqlTranslator;
    }

    /**
     * Retrieves price information based on the provided {@link PriceInfoQuery}.
     *
     * @param criteria the criteria describing the filter and ordering conditions
     * @return an {@code Optional} containing the {@code PriceInfo} if a matching record is found, or an empty {@code Optional} otherwise
     */
    @Override
    @Timed("price.database.query.timespent")
    public Optional<PriceInfo> findPriceInfoByCriteria(PriceInfoQuery criteria) {
        var query = sqlTranslator.translate(criteria);

        var spec = jdbcClient.sql(query.sql())
                .params(query.parameters());

        if (criteria.limit() != null && criteria.limit() > 0) {
            spec = spec.withMaxRows(criteria.limit());
        }

        return spec.query((rs, _) -> new PriceInfo(
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
