package com.tfr.order.rule;

import com.tfr.order.model.DiscountRuleResult;
import com.tfr.order.model.Order;
import com.tfr.order.model.TaxRuleResult;
import com.tfr.rulesEngine.evaluate.EvaluatorBuilder;
import com.tfr.rulesEngine.evaluate.MatchingStrategy;
import com.tfr.rulesEngine.evaluate._Evaluator;
import com.tfr.rulesEngine.rule.RuleSet;
import com.tfr.rulesEngine.rule._RuleSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.tfr.order.rule.DiscountRules.*;

@Configuration
@ComponentScan(basePackages = {"com.tfr.order"})
public class SpringRuleConfig {

    @Bean("TaxRuleEvaluator")
    public _Evaluator<Order, TaxRuleResult> taxRuleEvaluator() {
        return new EvaluatorBuilder<Order, TaxRuleResult>()
                .withRules(taxRuleSet())
                .withMatchingStrategy(MatchingStrategy.MATCH_FIRST)
                .build();
    }

    @Bean("TaxRuleSet")
    public _RuleSet<Order, TaxRuleResult> taxRuleSet() {
        return new RuleSet.RuleSetBuilder<Order, TaxRuleResult>()
                .addRule(TaxRules.IS_TAXABLE)
                .addRule(TaxRules.NOT_TAXABLE)
                .addRule(TaxRules.ORDER_CONTAINS_TAXABLE_PRODUCT)
                .addRule(TaxRules.ORDER_DOES_NOT_CONTAIN_TAXABLE_PRODUCT)
                .build();
    }

    @Bean("DiscountRuleEvaluator")
    public _Evaluator<Order,DiscountRuleResult> discountRuleEvaluator() {
        return new EvaluatorBuilder<Order, DiscountRuleResult>()
                .withRules(discountRuleSets())
                .withMatchingStrategy(MatchingStrategy.MATCH_FIRST)
                .build();
    }

    @Bean("DiscountRuleSets")
    public _RuleSet<Order, DiscountRuleResult> discountRuleSets() {
        return new RuleSet.RuleSetBuilder<Order, DiscountRuleResult>()
                .addRule(LABOR_DAY_SALE)
                .addRule(NO_SINGLE_DAY_SALE)
                .addRule(SUMMER_SALE)
                .build();
    }
}
