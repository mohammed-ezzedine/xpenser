package me.ezzedine.mohammed.xpenser.core.account.budget;

public class Currencies {

    public static Currency dollar() {
        return new Currency("USD", "$");
    }

    public static Currency euro() {
        return new Currency("EUR", "€");
    }

    public static Currency swissFranc() {
        return new Currency("CHF", "CHF");
    }

    public static Currency lebaneseLira() {
        return new Currency("LBP", "LBP");
    }
}
