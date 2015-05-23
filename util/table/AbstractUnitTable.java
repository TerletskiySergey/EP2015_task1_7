package EPAM2015_task1_7.util.table;

import EPAM2015_task1_7.model.ammunition.Ammunition;
import EPAM2015_task1_7.model.ammunition.Armor;
import EPAM2015_task1_7.model.ammunition.Weapon;
import EPAM2015_task1_7.model.unit.AbstractUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AbstractUnitTable extends Table {

    private AbstractUnit unit;
    private int horShift;
    private int verShift;
    private Comparator<Ammunition> comparator;

    public AbstractUnitTable(AbstractUnit unit, Comparator<Ammunition> comparator) {
        super(null, null, null);
        this.unit = unit;
        this.comparator = comparator;
    }

    @Override
    public void setHorShift(int horShift) {
        this.horShift = horShift;
    }

    @Override
    public void setVerShift(int verShift) {
        this.verShift = verShift;
    }

    @Override
    public String toString() {
        Table left = new Table(widthsLeftPart(), headerLeftPart(), dataLeftPart());
        left.tag = "UNIT INFO:";
        Table right = new Table(widthsRightPart(), headerRightPart(), dataRightPart());
        right.tag = "UNIT'S KITBAG CONTENTS:";
        left.fitWidthsToContent(new int[]{0, 1, 2, 3});
        left.setHorShift(this.horShift);
        left.setVerShift(this.verShift);
        right.setVerShift(this.verShift);
        right.fitWidthsToContent(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        return Table.concat(left, right);
    }

    private List<List<String>> dataLeftPart() {
        List<List<String>> data = new ArrayList<>();
        if (unit == null) {
            return data;
        }
        List<String> row = new ArrayList<>();
        row.add(String.valueOf(unit.getClass().getSimpleName()));
        row.add(unit.getName());
        row.add(String.valueOf(unit.getCarryForce()));
        row.add(String.valueOf(unit.getCash()));
        data.add(row);
        return data;
    }

    private List<String> headerLeftPart() {
        List<String> header = new ArrayList<>();
        header.add("TYPE");
        header.add("NAME");
        header.add("CARRYING FORCE");
        header.add("CASH");
        return header;
    }

    private List<Integer> widthsLeftPart() {
        List<Integer> widths = new ArrayList<>();
        widths.add(4);
        widths.add(4);
        widths.add(14);
        widths.add(4);
        return widths;
    }

    private List<List<String>> dataRightPart() {
        List<List<String>> data = new ArrayList<>();
        if (unit == null || unit.ammoSet().isEmpty()) {
            return data;
        }
        List<String> row;
        int counter = 1;
        List<Ammunition> ammoList = new ArrayList<>(unit.ammoSet());
        ammoList.sort(comparator);
        for (Ammunition amm : ammoList) {
            row = new ArrayList<>();
            row.add(String.valueOf(counter));
            row.add(amm.getName());
            String type = amm.getClass().getSimpleName();
            row.add(type);
            row.add(String.valueOf(amm.getWeight()));
            if (type.equals("Armor")) {
                row.add(String.valueOf(((Armor) amm).getStrength()));
            } else {
                row.add("--");
            }
            if (type.equals("Weapon")) {
                row.add(String.valueOf(((Weapon) amm).getDamage()));
                row.add(String.valueOf(((Weapon) amm).getRange()));
            } else {
                row.add("--");
                row.add("--");
            }
            row.add(String.valueOf(unit.ammoQuant(amm)));
            data.add(row);
            counter++;
        }
        return data;
    }

    private List<String> headerRightPart() {
        List<String> header = new ArrayList<>();
        header.add("ITEM");
        header.add("DESCRIPTION");
        header.add("TYPE");
        header.add("WEIGHT");
        header.add("STRENGTH");
        header.add("DAMAGE");
        header.add("RANGE");
        header.add("Q-TY");
        return header;
    }

    private List<Integer> widthsRightPart() {
        List<Integer> widths = new ArrayList<>();
        widths.add(4);
        widths.add(11);
        widths.add(4);
        widths.add(6);
        widths.add(8);
        widths.add(6);
        widths.add(5);
        widths.add(4);
        return widths;
    }
}