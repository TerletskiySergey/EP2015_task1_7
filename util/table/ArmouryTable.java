package EPAM2015_task1_7.util.table;

import EPAM2015_task1_7.model.ammunition.Ammunition;
import EPAM2015_task1_7.model.ammunition.Armor;
import EPAM2015_task1_7.model.ammunition.Weapon;
import EPAM2015_task1_7.model.shop.Armoury;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArmouryTable extends Table {

    private Armoury shop;
    private Comparator<Ammunition> comparator;

    public ArmouryTable(Armoury shop, Comparator<Ammunition> comparator) {
        super(null, null, null);
        this.shop = shop;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        tag = "CURRENTLY AVAILABLE IN STOCK:";
        widths();
        header();
        data();
        fitWidthsToContent(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        return super.toString();
    }

    private boolean canBeBought(Ammunition item) {
        return !shop.canAdmit() &&
                shop.waresAttribute(item).getPrice() <= shop.getVisitor().getCash() &&
                item.getWeight() <= shop.getVisitor().getCarryForce();
    }

    private void data() {
        data = new ArrayList<>();
        if (shop == null || shop.wares().isEmpty()) return;
        ArrayList<String> row;
        int counter = 1;
        List<Ammunition> ammoList = new ArrayList<>(shop.wares());
        ammoList.sort(comparator);
        for (Ammunition item : ammoList) {
            row = new ArrayList<>();
            char sign = canBeBought(item) ? '+' : '-';
            row.add(String.valueOf(counter) + sign);
            row.add(item.getName());
            String type = item.getClass().getSimpleName();
            row.add(type);
            row.add(String.valueOf(item.getWeight()));
            if (type.equals("Armor")) {
                row.add(String.valueOf(((Armor) item).getStrength()));
            } else {
                row.add("--");
            }
            if (type.equals("Weapon")) {
                row.add(String.valueOf(((Weapon) item).getDamage()));
                row.add(String.valueOf(((Weapon) item).getRange()));
            } else {
                row.add("--");
                row.add("--");
            }
            row.add(String.valueOf(shop.waresAttribute(item).getPrice()));
            row.add(String.valueOf(shop.waresAttribute(item).getQuant()));
            data.add(row);
            counter++;
        }
    }

    private void header() {
        header = new ArrayList<>();
        header.add("ITEM");
        header.add("DESCRIPTION");
        header.add("TYPE");
        header.add("WEIGHT");
        header.add("STRENGTH");
        header.add("DAMAGE");
        header.add("RANGE");
        header.add("PRICE");
        header.add("Q-TY");
    }

    private void widths() {
        widths = new ArrayList<>();
        widths.add(4);
        widths.add(11);
        widths.add(4);
        widths.add(6);
        widths.add(8);
        widths.add(6);
        widths.add(5);
        widths.add(5);
        widths.add(4);
    }
}
