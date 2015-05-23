package EPAM2015_task1_7.model.shop;

import EPAM2015_task1_7.model.ammunition.Ammunition;

public class Armoury extends AbstractShop<Ammunition> {

    @Override
    public boolean sellItem(Ammunition item) {
        if (visitor.canOutfit(item) && super.sellItem(item)) {
            visitor.outfit(item);
            return true;
        }
        return false;
    }
}