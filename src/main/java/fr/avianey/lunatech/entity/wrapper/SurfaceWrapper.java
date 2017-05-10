package fr.avianey.lunatech.entity.wrapper;

import fr.avianey.lunatech.entity.Country;

public class SurfaceWrapper {

    private final String surface;
    private final String code;
    private final String name;

    public SurfaceWrapper(Country country, String surface) {
        this.surface = surface;
        this.code = country.getCode();
        this.name = country.getName();
    }
}
