package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the {@link PriceRepository} interface using SQL for data retrieval.
 * This class is marked as a Spring repository and interacts with the database through
 * the {@code JdbcClient}.
 *
 * The main purpose of this repository is to query price information based on the brand ID,
 * product ID, and application date.
 */
@Repository
public class PriceSqlRepository implements PriceRepository {
    private final JdbcClient jdbcClient;

    public PriceSqlRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * Retrieves the price information for a specific product and brand based on the provided application date.
     * The method queries the database to find the price with the highest priority that is valid for the specified time range.
     *
     * @param brandId         the ID of the brand to which the product belongs
     * @param productId       the ID of the product for which the price information is requested
     * @param applicationDate the date and time for which the price information needs to be determined
     * @return an {@code Optional} containing the {@code PriceInfo} if a matching record is found, or an empty {@code Optional} otherwise
     */
    @Override
    public Optional<PriceInfo> findPriceInfoByApplicationDate(int brandId, int productId, LocalDateTime applicationDate) {
        return jdbcClient.sql(
            """
            select brand_id, product_id, price_list, start_date, end_date, price,currency
            from prices
            where brand_id = :brandId and
                  product_id = :productId and
                  start_date <= :applicationDate and
                  end_date >= :applicationDate
            order by priority desc, last_update_by desc
            limit 1
            """
        ).param("brandId",brandId)
        .param("productId",productId)
        .param("applicationDate",applicationDate)
        .query((rs,_) -> new PriceInfo(
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
