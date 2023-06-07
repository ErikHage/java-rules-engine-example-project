package com.tfr.order.rule;

import com.tfr.order.model.Order;
import com.tfr.order.model.TaxRuleResult;
import com.tfr.rulesEngine.rule.Rule;
import com.tfr.rulesEngine.rule._Rule;

import static com.tfr.order.config.Constants.*;

public interface TaxRules {

    _Rule<Order, TaxRuleResult> IS_TAXABLE = new Rule.RuleBuilder<Order,TaxRuleResult>(
            "taxableRule",
            (i) -> STATES_IN_TAX_SCOPE.contains(i.getProperty("STATE")))
            .onMatchHandler((i,o) -> o.setInScopeForTax(true))
            .priority(100)
            .nextGroupName("Product Group")
            .build();

    _Rule<Order,TaxRuleResult> NOT_TAXABLE = new Rule.RuleBuilder<Order,TaxRuleResult>(
            "notTaxableRule",
            (i) -> true)
            .onMatchHandler((i,o) -> o.setInScopeForTax(false))
            .priority(50)
            .build();



    _Rule<Order,TaxRuleResult> ORDER_CONTAINS_TAXABLE_PRODUCT = new Rule.RuleBuilder<Order,TaxRuleResult>(
            "taxableProductRule",
            (o) -> o.getItems()
                    .keySet()
                    .stream()
                    .anyMatch(i -> TAXABLE_PRODUCT_TYPES.contains(i.getType())))
            .onMatchHandler((i,o) -> o.setContainsTaxableProducts(true))
            .groupName("Product Group")
            .priority(100)
            .build();

    _Rule<Order,TaxRuleResult> ORDER_DOES_NOT_CONTAIN_TAXABLE_PRODUCT = new Rule.RuleBuilder<Order,TaxRuleResult>(
            "noTaxableProductRule",
            (o) -> true)
            .onMatchHandler((i,o) -> o.setContainsTaxableProducts(false))
            .groupName("Product Group")
            .priority(50)
            .build();

}
