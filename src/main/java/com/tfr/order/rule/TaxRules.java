package com.tfr.order.rule;

import com.tfr.order.model.Order;
import com.tfr.rulesEngine.rule.Rule;
import com.tfr.rulesEngine.rule._Rule;

import static com.tfr.order.config.Constants.*;

/**
 *
 * Created by Erik on 7/4/2017.
 */
public interface TaxRules {

    _Rule<Order,Order> IS_TAXABLE = new Rule.RuleBuilder<Order,Order>(
            "taxableRule",
            (o) -> STATES_IN_TAX_SCOPE.contains(o.getProperty("STATE")))
            .consumer(o -> o.setProperty("IN_SCOPE_FOR_TAX", "Y"))
            .priority(100)
            .nextGroup("Product Group")
            .build();

    _Rule<Order,Order> NOT_TAXABLE = new Rule.RuleBuilder<Order,Order>(
            "notTaxableRule",
            (o) -> true)
            .consumer(o -> o.setProperty("IN_SCOPE_FOR_TAX", "N"))
            .priority(50)
            .build();



    _Rule<Order,Order> ORDER_CONTAINS_TAXABLE_PRODUCT = new Rule.RuleBuilder<Order,Order>(
            "taxableProductRule",
            (o) -> "Y".equals(o.getProperty("IN_SCOPE_FOR_TAX"))
                    && o.getItems()
                    .keySet()
                    .stream()
                    .filter(i -> TAXABLE_PRODUCT_TYPES.contains(i.getType()))
                    .count() > 0)
            .consumer(o -> o.setProperty("CONTAINS_TAXABLE_PRODUCTS", "Y"))
            .group("Product Group")
            .priority(100)
            .build();

    _Rule<Order,Order> ORDER_DOES_NOT_CONTAIN_TAXABLE_PRODUCT = new Rule.RuleBuilder<Order,Order>(
            "noTaxableProductRule",
            (o) -> true)
            .consumer(o -> o.setProperty("CONTAINS_TAXABLE_PRODUCTS", "N"))
            .group("Product Group")
            .priority(50)
            .build();

}
