package com.tfr.order.processor;

import com.tfr.order.model.DiscountRuleResult;
import com.tfr.order.model.Order;
import com.tfr.order.model.TaxRuleResult;
import com.tfr.rulesEngine.data.EvaluationResult;
import com.tfr.rulesEngine.evaluate._Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("OrderRuleProcessor")
public class OrderRuleProcessor {

    private final _Evaluator<Order, TaxRuleResult> taxEvaluator;
    private final _Evaluator<Order, DiscountRuleResult> discountEvaluator;

    @Autowired
    public OrderRuleProcessor(@Qualifier("TaxRuleEvaluator") _Evaluator<Order, TaxRuleResult> taxEvaluator,
                              @Qualifier("DiscountRuleEvaluator") _Evaluator<Order, DiscountRuleResult> discountEvaluator) {
        this.taxEvaluator = taxEvaluator;
        this.discountEvaluator = discountEvaluator;
    }

    public Order process(Order order) {
        TaxRuleResult taxRuleResult = new TaxRuleResult();
        DiscountRuleResult discountRuleResult = new DiscountRuleResult();

        EvaluationResult<Order, TaxRuleResult> taxResult = taxEvaluator.evaluate(order, taxRuleResult);
        System.out.println(taxResult);
        EvaluationResult<Order, DiscountRuleResult> discountResult = discountEvaluator.evaluate(order, discountRuleResult);
        System.out.println(discountResult);

        return updateOrder(order, taxResult, discountResult);
    }

    private Order updateOrder(Order order, EvaluationResult<Order, TaxRuleResult> taxResult, EvaluationResult<Order, DiscountRuleResult> discountResult) {
        TaxRuleResult taxRuleResult = taxResult.getKnowledge().value();
        DiscountRuleResult discountRuleResult = discountResult.getKnowledge().value();

        if (taxRuleResult.isInScopeForTax()) {
            order.setProperty("IN_SCOPE_FOR_TAX", "Y");
            order.setProperty("CONTAINS_TAXABLE_PRODUCTS", taxRuleResult.isContainsTaxableProducts() ? "Y" : "N");
        } else {
            order.setProperty("IN_SCOPE_FOR_TAX", "N");
        }

        order.getDiscounts().addAll(discountRuleResult.getDiscounts());

        return order;
    }


}

