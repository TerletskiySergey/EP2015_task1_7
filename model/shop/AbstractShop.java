package EPAM2015_task1_7.model.shop;

import EPAM2015_task1_7.model.unit.AbstractUnit;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractShop<T> implements Serializable {

    public static class ShopItemAttribute implements Serializable {
        private int price;
        private int quant;

        public ShopItemAttribute(int price, int quant) {
            inputFilter(price, quant);
        }

        private void inputFilter(int price, int quant) {
            this.price = price < 0 ? 0 : price;
            this.quant = quant < 1 ? 1 : quant;
        }

        public int getPrice() {
            return price;
        }

        public int getQuant() {
            return quant;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setQuant(int quant) {
            this.quant = quant;
        }
    }

    protected AbstractUnit visitor;
    protected Map<T, ShopItemAttribute> wares;

    protected AbstractShop() {
        this.wares = new HashMap<>();
    }

    public synchronized boolean admit(AbstractUnit visitor) {
        if (visitor == null) {
            throw new NullPointerException();
        }
        if (this.visitor != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        this.visitor = visitor;
        return true;
    }

    public boolean canAdmit() {
        return this.visitor == null;
    }

    public AbstractUnit getVisitor() {
        return this.visitor;
    }

    public void letGo() {
        if (this.visitor == null) {
            throw new IllegalStateException();
        }
        this.visitor = null;
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean sellItem(T item) {
        if (visitor == null) {
            throw new IllegalStateException();
        }
        ShopItemAttribute attr = this.wares.get(item);
        if (attr != null && visitor.canPay(attr.price)) {
            if (attr.quant > 1) {
                attr.quant--;
            } else {
                this.wares.remove(item);
            }
            visitor.pay(attr.price);
            return true;
        }
        return false;
    }

    public void addItem(T item, int price, int quant) {
        if (item == null || price < 0 || quant < 1) {
            throw new IllegalArgumentException();
        }
        this.wares.put(item, new ShopItemAttribute(price, quant));
    }

    public Set<T> wares() {
        return this.wares.keySet();
    }

    public ShopItemAttribute waresAttribute(T item) {
        return this.wares.get(item);
    }
}