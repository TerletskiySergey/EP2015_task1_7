package EPAM2015_task1_7.model.unit;

import EPAM2015_task1_7.model.ammunition.Ammunition;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractUnit implements Serializable {

    protected String name;
    protected int carryForce;
    protected int cash;
    protected Map<Ammunition, Integer> kitbag;

    protected AbstractUnit(String name, int carryForce, int cash) {
        inputFilter(name, carryForce, cash);
        this.kitbag = new HashMap<>();
    }

    private void inputFilter(String name, int carryForce, int cash) {
        this.name = name == null ? "" : name;
        this.carryForce = carryForce < 0 ? 0 : carryForce;
        this.cash = cash < 0 ? 0 : cash;
    }

    public String getName() {
        return name;
    }

    public int getCarryForce() {
        return carryForce;
    }

    public int getCash() {
        return cash;
    }

    public Set<Ammunition> ammoSet() {
        return this.kitbag == null ? new HashSet<>() : this.kitbag.keySet();
    }

    public Integer ammoQuant(Ammunition item) {
        Integer toReturn = this.kitbag == null ? null : this.kitbag.get(item);
        return toReturn == null ? 0 : toReturn;
    }

    public boolean canOutfit(Ammunition item) {
        return item != null && item.getWeight() <= this.carryForce;
    }

    public void outfit(Ammunition item) {
        if (canOutfit(item)) {
            Integer quant = kitbag.get(item);
            if (quant != null) {
                kitbag.replace(item, quant + 1);
            } else {
                this.kitbag.put(item, 1);
            }
            this.carryForce -= item.getWeight();
        }
    }

    public boolean canPay(int val) {
        if (val < 0) {
            throw new IllegalArgumentException();
        }
        return this.cash >= val;
    }

    public void pay(int val) {
        if (canPay(val)) {
            this.cash -= val;
        }
    }

    public void putoff(Ammunition item) {
        Integer quant = kitbag.get(item);
        if (quant != null) {
            this.carryForce += item.getWeight();
            if (quant == 1) {
                kitbag.remove(item);
            } else {
                kitbag.replace(item, quant - 1);
            }
        }
    }
}