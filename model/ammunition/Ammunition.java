package EPAM2015_task1_7.model.ammunition;

import java.io.Serializable;
import java.util.Objects;

public abstract class Ammunition implements Serializable {


    protected String name;
    protected int weight;

    protected Ammunition(String name, int weight) {
        inputFilter(name, weight);
    }

    private void inputFilter(String name, int weight) {
        this.name = name == null ? "" : name;
        this.weight = weight < 0 ? 0 : weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object other) {
        return other != null &&
                this.getClass() == other.getClass() &&
                this.name.equals(((Ammunition) other).name) &&
                this.weight == ((Ammunition) other).weight;
    }

    @Override
    public int hashCode() {
        int toReturn = 1;
        toReturn = toReturn * 31 + Objects.hashCode(name);
        toReturn = toReturn * 31 + Objects.hashCode(weight);
        return toReturn;
    }


}