package com.pw.facetnav.model.ad;

import com.pw.facetnav.model.attribute.AttributeFilterRecord;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class AdSpecifications {

    public static Specification<Ad> withAttributesAndCategory(String categoryName, Set<AttributeFilterRecord> attributeFilters) {
        Specification<Ad> categorySpec = hasCategory(categoryName);
        Specification<Ad> attributesSpec = hasAttributes(attributeFilters);

        return Specification.where(categorySpec).and(attributesSpec);
    }

    public static Specification<Ad> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("name"), categoryName);
    }

    public static Specification<Ad> hasAttributes(Set<AttributeFilterRecord> attributeFilters) {
        if (attributeFilters == null || attributeFilters.isEmpty()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = attributeFilters.stream()
                    .map(filter -> createAttributePredicate(root, criteriaBuilder, filter))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }

    private static Predicate createAttributePredicate(Root<Ad> root, CriteriaBuilder criteriaBuilder, AttributeFilterRecord filter) {
        String attributeName = filter.attributeName();
        Integer attributeValue = filter.attributeValue();
        String operator = filter.operator();

        Path<Integer> attributeValuePath = root.join("attributes").get("value");

        return switch (operator) {
            case "=" -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("attributes").get("name"), attributeName),
                    criteriaBuilder.equal(attributeValuePath, attributeValue)
            );
            case ">" -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("attributes").get("name"), attributeName),
                    criteriaBuilder.greaterThan(attributeValuePath, attributeValue)
            );
            case "<" -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("attributes").get("name"), attributeName),
                    criteriaBuilder.lessThan(attributeValuePath, attributeValue)
            );
            default -> throw new UnsupportedOperationException("Unsupported operator: " + operator);
        };
    }
}
