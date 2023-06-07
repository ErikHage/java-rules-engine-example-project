package com.tfr.order.model;

import java.util.ArrayList;
import java.util.List;

public class DiscountRuleResult {

    private final List<Discount> discounts;

    public DiscountRuleResult() {
        this.discounts = new ArrayList<>();
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
