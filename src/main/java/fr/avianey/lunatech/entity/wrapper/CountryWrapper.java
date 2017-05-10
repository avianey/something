package fr.avianey.lunatech.entity.wrapper;

import fr.avianey.lunatech.entity.Country;

public class CountryWrapper {

    private final Integer count;
    private final String code;
    private final String name;

    public CountryWrapper(Country country, Number count) {
        this.count = count.intValue();
        this.code = country.getCode();
        this.name = country.getName();
    }

    public Integer getCount() {
        return count;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
