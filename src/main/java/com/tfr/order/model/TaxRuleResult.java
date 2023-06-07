package com.tfr.order.model;

public class TaxRuleResult {

    private boolean inScopeForTax;
    private boolean containsTaxableProducts;

    public TaxRuleResult() {
        this.inScopeForTax = false;
        this.containsTaxableProducts = false;
    }

    public boolean isInScopeForTax() {
        return inScopeForTax;
    }

    public void setInScopeForTax(boolean inScopeForTax) {
        this.inScopeForTax = inScopeForTax;
    }

    public boolean isContainsTaxableProducts() {
        return containsTaxableProducts;
    }

    public void setContainsTaxableProducts(boolean containsTaxableProducts) {
        this.containsTaxableProducts = containsTaxableProducts;
    }
}
