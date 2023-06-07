package com.tfr.order.processor;

import com.tfr.order.RulesEngineExampleApplication;
import com.tfr.order.model.Item;
import com.tfr.order.model.Order;
import com.tfr.order.rule.SpringRuleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static com.tfr.order.config.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RulesEngineExampleApplication.class)
@Import(SpringRuleConfig.class)
public class TestOrderRuleProcessor {

    @Autowired
    private OrderRuleProcessor processor;

    @Test
    public void testProcess_GivenJuly4thNYOneClothingItem_ExpectDiscountAndTaxable() {
        Order order = new Order(LocalDate.of(2017, 7, 4));
        order.addItem(new Item("Shirt", 20.0, Item.Type.CLOTHING, "Men's Shirt"));
        order.setProperty("STATE", "NY");

        runTest(order, 1, "Y", "Y");
        assertEquals(SUMMER_SALE_DEAL, order.getDiscounts().get(0));
    }

    @Test
    public void testProcess_GivenJuly4thNYTwoFoodItems_ExpectDiscountAndNotTaxable() {
        Order order = new Order(LocalDate.of(2017, 7, 4));
        order.addItem(new Item("Apples", 3.0, Item.Type.FOOD, "Gala"));
        order.addItem(new Item("Apples", 5.0, Item.Type.FOOD, "Honeycrisp"));
        order.setProperty("STATE", "NY");

        runTest(order, 1, "Y", "N");
        assertEquals(SUMMER_SALE_DEAL, order.getDiscounts().get(0));
    }

    @Test
    public void testProcess_GivenJune10thNYTwoFoodItems_ExpectNoDiscountAndNotTaxable() {
        Order order = new Order(LocalDate.of(2017, 6, 10));
        order.addItem(new Item("Apples", 3.0, Item.Type.FOOD, "Gala"));
        order.addItem(new Item("Apples", 5.0, Item.Type.FOOD, "Honeycrisp"));
        order.setProperty("STATE", "NY");

        runTest(order, 0, "Y", "N");
    }

    @Test
    public void testProcess_GivenJune10thOKTwoFoodItems_ExpectNoDiscountAndNotInScopeForTax() {
        Order order = new Order(LocalDate.of(2017, 6, 10));
        order.addItem(new Item("Apples", 3.0, Item.Type.FOOD, "Gala"));
        order.addItem(new Item("Apples", 5.0, Item.Type.FOOD, "Honeycrisp"));
        order.setProperty("STATE", "OK");

        runTest(order, 0, "N", null);
    }


    private void runTest(Order order, int expectedDiscountNumber, String expectedInScopeForTax,
                          String expectedContainsTaxableProducts) {
        order = processor.process(order);

        System.out.println(order);

        assertEquals(expectedDiscountNumber, order.getDiscounts().size());
        assertEquals(expectedInScopeForTax, order.getProperty("IN_SCOPE_FOR_TAX"));
        assertEquals(expectedContainsTaxableProducts, order.getProperty("CONTAINS_TAXABLE_PRODUCTS"));
    }

}
