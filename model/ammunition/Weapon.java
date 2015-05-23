package EPAM2015_task1_7.model.ammunition;

import java.util.Objects;

public class Weapon extends Ammunition {

    private int damage;
    private int range;

    public Weapon(String name, int weight, int damage, int range) {
        super(name, weight);
        inputFilter(damage, range);
    }

    private void inputFilter(int damage, int range) {
        this.damage = damage < 0 ? 0 : damage;
        this.range = range < 0 ? 0 : range;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) &&
                this.damage == ((Weapon) other).damage &&
                this.range == ((Weapon) other).range;
    }

    @Override
    public int hashCode() {
        int toReturn = super.hashCode();
        toReturn = toReturn * 31 + Objects.hashCode(damage);
        toReturn = toReturn * 31 + Objects.hashCode(range);
        return toReturn;
    }
}