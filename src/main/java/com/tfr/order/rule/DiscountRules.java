package com.tfr.order.rule;

import com.tfr.order.model.DiscountRuleResult;
import com.tfr.order.model.Order;
import com.tfr.rulesEngine.rule.Rule;
import com.tfr.rulesEngine.rule._Rule;

import static com.tfr.order.config.Constants.*;


/**
 *
 * Created by Erik on 7/4/2017.
 */
public interface DiscountRules {

    _Rule<Order, DiscountRuleResult> LABOR_DAY_SALE = new Rule.RuleBuilder<Order, DiscountRuleResult>(
            "laborDaySale",
            i -> LABOR_DAY.equals(i.getOrderDate()))
            .onMatchHandler((i,o) -> o.addDiscount(LABOR_DAY_DEAL))
            .nextGroupName("Summer")
            .build();

    _Rule<Order, DiscountRuleResult> NO_SINGLE_DAY_SALE = new Rule.RuleBuilder<Order, DiscountRuleResult>(
            "noSingleDaySale",
            i -> true)
            .onMatchHandler((i,o) -> {})
            .nextGroupName("Summer")
            .build();


    _Rule<Order, DiscountRuleResult> SUMMER_SALE = new Rule.RuleBuilder<Order, DiscountRuleResult>(
            "summerSale",
            i -> SUMMER_SALE_START.isBefore(i.getOrderDate()) && SUMMER_SALE_END.isAfter(i.getOrderDate()))
            .onMatchHandler((i,o) -> o.addDiscount(SUMMER_SALE_DEAL))
            .groupName("Summer")
            .build();

}
