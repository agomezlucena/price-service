package io.github.agomezlucena.priceservice.infrastructure.criteria;

import io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator;
import io.github.agomezlucena.priceservice.domain.criteria.InvalidPriceCriteriaException;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoCriterion;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortCriterion;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Translates a {@link PriceInfoQuery} into a safe parameterized SQL query and its parameters.
 * Validates order criteria fields against allowed columns to prevent SQL injection.
 */
@Component
public class PriceInfoCriteriaSqlTranslator {

    private record FieldMapping(String column, String paramName) {
    }

    private static final Map<PriceInfoQueryField, FieldMapping> FIELD_MAPPINGS = Map.of(
            PriceInfoQueryField.PRIORITY, new FieldMapping("priority", "priority"),
            PriceInfoQueryField.LAST_UPDATE_BY, new FieldMapping("last_update_by", "lastUpdateBy"),
            PriceInfoQueryField.LAST_UPDATE, new FieldMapping("last_update", "lastUpdate"),
            PriceInfoQueryField.START_DATE, new FieldMapping("start_date", "startDate"),
            PriceInfoQueryField.END_DATE, new FieldMapping("end_date", "endDate"),
            PriceInfoQueryField.PRICE, new FieldMapping("price", "price"),
            PriceInfoQueryField.PRICE_LIST, new FieldMapping("price_list", "priceList"),
            PriceInfoQueryField.BRAND_ID, new FieldMapping("brand_id", "brandId"),
            PriceInfoQueryField.PRODUCT_ID, new FieldMapping("product_id", "productId")
    );

    private static final Map<CriterionComparator, String> COMPARATORS_MAPPINGS = Map.of(
            CriterionComparator.EQUALS, "=",
            CriterionComparator.GREATER_THAN, ">",
            CriterionComparator.LESS_THAN, "<",
            CriterionComparator.GREATER_THAN_OR_EQUAL, ">=",
            CriterionComparator.LESS_THAN_OR_EQUAL, "<="
    );

    private static final String BASE_SQL = """
            select brand_id, product_id, price_list, start_date, end_date, price, currency
            from prices""";

    /**
     * Translates the given {@link PriceInfoQuery} into a parameterized SQL query and parameters.
     *
     * @param criteria the domain criteria to translate
     * @return the generated {@link PriceInfoCriteriaSqlQuery} containing the SQL and parameter map
     * @throws InvalidPriceCriteriaException if an invalid order field is supplied or criteria is null
     */
    public PriceInfoCriteriaSqlQuery translate(PriceInfoQuery criteria) {
        if (criteria == null) {
            throw new InvalidPriceCriteriaException("PriceInfoCriteria cannot be null");
        }

        var parameters = new HashMap<String, Object>();
        var whereClauses = buildWhereClauses(criteria.criteria(), parameters);
        var orderClauses = buildOrderClauses(criteria.priceInfoOrderCriteria());

        var sql = new StringBuilder(BASE_SQL);
        appendWhereClause(sql, whereClauses);
        appendOrderByClause(sql, orderClauses);

        return new PriceInfoCriteriaSqlQuery(sql.toString().trim(), parameters);
    }

    private List<String> buildWhereClauses(List<PriceInfoCriterion> criteria, Map<String, Object> parameters) {
        if (criteria == null || criteria.isEmpty()) {
            return List.of();
        }

        var whereClauses = new ArrayList<String>();
        for (var criterion : criteria) {
            if (criterion == null || criterion.field() == null) {
                continue;
            }
            var mapping = FIELD_MAPPINGS.get(criterion.field());
            if (mapping == null) {
                throw new InvalidPriceCriteriaException("Unsupported criteria field: " + criterion.field());
            }
            var operator = getSqlOperator(criterion.comparator());
            whereClauses.add(mapping.column() + " " + operator + " :" + mapping.paramName());
            parameters.put(mapping.paramName(), criterion.value());
        }
        return whereClauses;
    }

    private List<String> buildOrderClauses(List<PriceInfoSortCriterion> orderCriteria) {
        if (orderCriteria == null || orderCriteria.isEmpty()) {
            return List.of();
        }

        var orderClauses = new ArrayList<String>();
        for (var order : orderCriteria) {
            if (order == null || order.field() == null) {
                throw new InvalidPriceCriteriaException("Order criteria or field cannot be null");
            }

            var mapping = FIELD_MAPPINGS.get(order.field());

            if (mapping == null) {
                throw new InvalidPriceCriteriaException("Unsupported order field: " + order.field());
            }

            var direction = Optional.ofNullable(order.direction())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .orElse("asc");
            orderClauses.add(mapping.column() + " " + direction);
        }
        return orderClauses;
    }

    private String getSqlOperator(CriterionComparator comparator) {
        return Optional.ofNullable(comparator)
                .map(COMPARATORS_MAPPINGS::get)
                .orElse("=");
    }

    private void appendWhereClause(StringBuilder sql, List<String> whereClauses) {
        if (whereClauses.isEmpty()) {
            return;
        }
        sql.append("\nwhere ").append(String.join(" and\n      ", whereClauses));
    }

    private void appendOrderByClause(StringBuilder sql, List<String> orderClauses) {
        if (orderClauses.isEmpty()) {
            return;
        }
        sql.append("\norder by ").append(String.join(", ", orderClauses));
    }
}
