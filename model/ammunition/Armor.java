package EPAM2015_task1_7.model.ammunition;

import java.util.Objects;

public class Armor extends Ammunition {

    private int strength;

    public Armor(String name, int weight, int strength) {
        super(name, weight);
        inputFilter(strength);
    }

    private void inputFilter(int strength) {
        this.strength = strength < 0 ? 0 : strength;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) &&
                this.strength == ((Armor) other).strength;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + Objects.hashCode(strength);
    }
}
