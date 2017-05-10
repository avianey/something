package fr.avianey.lunatech.entity.wrapper;

public class IdentWrapper {

    private final String ident;
    private final Integer count;

    public IdentWrapper(String ident, Number count) {
        this.count = count.intValue();
        this.ident = ident;
    }
}
