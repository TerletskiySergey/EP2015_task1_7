package EPAM2015_task1_7.view;

import EPAM2015_task1_7.model.ammunition.Ammunition;
import EPAM2015_task1_7.model.ammunition.Armor;
import EPAM2015_task1_7.model.ammunition.Weapon;
import EPAM2015_task1_7.model.shop.AbstractShop;
import EPAM2015_task1_7.model.shop.Armoury;
import EPAM2015_task1_7.model.unit.AbstractUnit;
import EPAM2015_task1_7.model.unit.Knight;
import EPAM2015_task1_7.util.Utils;
import EPAM2015_task1_7.util.table.AbstractUnitTable;
import EPAM2015_task1_7.util.table.ArmouryTable;
import EPAM2015_task1_7.util.table.Table;

import java.io.*;
import java.util.*;

public class ArmouryApp {

    public static final Comparator<Ammunition> BY_TYPE_NAME_COMPARATOR = ArmouryApp::compareByTypeName;
    public static final Comparator<Ammunition> BY_WEIGHT_COMPARATOR = ArmouryApp::compareByWeight;

    private static AbstractUnit unit;
    private static Armoury shop;
    private static Armoury filter;
    private static Comparator<Ammunition> curComparator = BY_TYPE_NAME_COMPARATOR;

    private static class MainMenu {
        private static void start() throws IOException, ClassNotFoundException {
            initialization();
            int userChoice = -1;
            Utils.clearConsole();
            while (userChoice != 0) {
                show();
                userChoice = Utils.getIntFromConsole(0, 2);
                switch (userChoice) {
                    case 1:
                        ArmouryMenu.start();
                        break;
                    case 2:
                        UnitProfileMenu.start();
                }
                Utils.clearConsole();
            }
        }

        private static void show() {
            System.out.println("         MENU");
            System.out.println("----------------------");
            System.out.println("1. Enter armoury.");
            System.out.println("2. Enter unit profile.");
            System.out.println("----------------------");
            System.out.println("0. Exit.");
        }
    }

    private static class UnitProfileMenu {
        private static void start() {
            int userChoice = -1;
            Utils.clearConsole();
            while (userChoice != 0) {
                showUnit();
                show();
                userChoice = unit == null || unit.ammoSet().isEmpty()
                        ? Utils.getIntFromConsole(0, 0)
                        : Utils.getIntFromConsole(0, 2);
                switch (userChoice) {
                    case 1:
                        Utils.clearConsole();
                        showUnit();
                        putOffItem();
                        break;
                    case 2:
                        if (curComparator.equals(BY_TYPE_NAME_COMPARATOR)) {
                            sortContentByWeight();
                        } else {
                            sortContentByTypeName();
                        }
                        break;
                    case 0:
                        sortContentByTypeName();
                }
                Utils.clearConsole();
            }
        }

        private static void putOffItem() {
            System.out.print("Enter item number: ");
            Set<Ammunition> ammoSet = unit.ammoSet();
            int index = Utils.getIntFromConsole(1, ammoSet.size());
            List<Ammunition> ammoList = new ArrayList<>(ammoSet);
            ammoList.sort(curComparator);
            unit.putoff(ammoList.get(index - 1));
        }

        private static void show() {
            System.out.println("\n   UNIT PROFILE MENU.");
            System.out.println("------------------------");
            if (unit != null && !unit.ammoSet().isEmpty()) {
                System.out.println("1. Put off item.");
                if (curComparator.equals(BY_TYPE_NAME_COMPARATOR)) {
                    System.out.println("2. Sort items by weight.");
                } else {
                    System.out.println("2. Sort items by type and name.");
                }
            }
            System.out.println("------------------------");
            System.out.println("0. Exit unit profile.");
        }
    }

    private static class ArmouryMenu {

        private static void buyItem() {
            if (noItemsAvailable()) {
                return;
            }
            System.out.print("Enter item number: ");
            int index = Utils.getIntFromConsole(1, filter != null ? filter.wares().size() : shop.wares().size());
            Ammunition item;
            List<Ammunition> ammoList;
            if (filter != null) {
                ammoList = new ArrayList<>(filter.wares());
            } else {
                ammoList = new ArrayList<>(shop.wares());
            }
            ammoList.sort(curComparator);
            item = ammoList.get(index - 1);
            if (!shop.sellItem(item)) {
                if (!unit.canPay(shop.waresAttribute(item).getPrice())) {
                    System.out.print("Not enough money to buy item.");
                } else {
                    System.out.print("Not enough force to carry item.");
                }
                new Scanner(System.in).nextLine();
            }
        }

        private static void filterPrice() {
            if (noItemsAvailable()) {
                return;
            }
            System.out.print("Enter min filter value: ");
            int minVal = Utils.getIntFromConsole(0);
            System.out.print("Enter max filter value: ");
            int maxVal = Utils.getIntFromConsole(minVal);
            filter = new Armoury();
            filter.admit(unit);
            for (Ammunition amm : shop.wares()) {
                AbstractShop.ShopItemAttribute attr = shop.waresAttribute(amm);
                if (attr.getPrice() <= maxVal && attr.getPrice() >= minVal) {
                    filter.addItem(amm, attr.getPrice(), attr.getQuant());
                }
            }
        }

        private static boolean noItemsAvailable() {
            if (filter != null && filter.wares().isEmpty() ||
                    shop.wares().isEmpty()) {
                System.out.println("No item is available.");
                new Scanner(System.in).nextLine();
                return true;
            }
            return false;
        }

        private static void resetFilter() {
            filter = null;
        }

        private static void start() {
            int userChoice = -1;
            shop.admit(unit);
            Utils.clearConsole();
            while (userChoice != 0) {
                showShop();
                showUnit();
                show();
                userChoice = Utils.getIntFromConsole(0, 3);
                switch (userChoice) {
                    case 1:
                        Utils.clearConsole();
                        showShop();
                        showUnit();
                        buyItem();
                        break;
                    case 2:
                        if (filter == null) {
                            Utils.clearConsole();
                            showShop();
                            showUnit();
                            filterPrice();
                        } else {
                            resetFilter();
                        }
                        break;
                    case 3:
                        if (curComparator.equals(BY_TYPE_NAME_COMPARATOR)) {
                            sortContentByWeight();
                        } else {
                            sortContentByTypeName();
                        }
                        break;
                    case 0:
                        shop.letGo();
                        resetFilter();
                        sortContentByTypeName();
                }
                Utils.clearConsole();
            }
        }

        private static void show() {
            System.out.println("\n       ARMOURY MENU:");
            System.out.println("---------------------------");
            System.out.println("1. Buy item.");
            if (filter == null) {
                System.out.println("2. Filter price.");
            } else {
                System.out.println("2. Reset filter.");
            }
            if (curComparator.equals(BY_TYPE_NAME_COMPARATOR)) {
                System.out.println("3. Sort items by weight.");
            } else {
                System.out.println("3. Sort items by type and name.");
            }
            System.out.println("---------------------------");
            System.out.println("0. Exit armoury.");
        }
    }

    @SuppressWarnings("unchecked")
    private static void initialization() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/EPAM2015_task1_7/resources/armouryAppInit"))) {
            unit = (AbstractUnit) in.readObject();
            shop = (Armoury) in.readObject();
        }
    }

    private static void serialization() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/EPAM2015_task1_7/resources/armouryAppInit"))) {
            AbstractUnit unit = new Knight("Arthur", 500, 1000);
            Armoury shop = new Armoury();
            shop.addItem(new Armor("Cuirass", 100, 500), 400, 5);
            shop.addItem(new Armor("Hauberk", 50, 150), 100, 5);
            shop.addItem(new Armor("Helmet", 40, 150), 150, 5);
            shop.addItem(new Armor("Buckler", 40, 250), 250, 5);
            shop.addItem(new Armor("Shield", 70, 400), 250, 5);
            shop.addItem(new Weapon("Arbalest", 20, 40, 20), 150, 5);
            shop.addItem(new Weapon("Bow", 20, 50, 15), 200, 5);
            shop.addItem(new Weapon("Greatsword", 60, 150, 4), 200, 5);
            shop.addItem(new Weapon("Mace", 20, 100, 1), 75, 5);
            shop.addItem(new Weapon("Sword", 40, 120, 2), 200, 5);
            out.writeObject(unit);
            out.writeObject(shop);
        }
    }

    private static int compareByTypeName(Ammunition am1, Ammunition am2) {
        return am1.getClass() == am2.getClass()
                ? am1.getName().compareTo(am2.getName())
                : am1.getClass().getSimpleName().compareTo(am2.getClass().getSimpleName());
    }

    private static int compareByWeight(Ammunition am1, Ammunition am2) {
        return am1.getWeight() - am2.getWeight();
    }

    private static void sortContentByTypeName() {
        curComparator = BY_TYPE_NAME_COMPARATOR;
    }

    private static void sortContentByWeight() {
        curComparator = BY_WEIGHT_COMPARATOR;
    }

    private static void showShop() {
        Table shopTable;
        if (filter != null) {
            shopTable = new ArmouryTable(filter, curComparator);
        } else {
            shopTable = new ArmouryTable(shop, curComparator);
        }
        System.out.println(shopTable);
    }

    private static void showUnit() {
        Table unitTable = new AbstractUnitTable(unit, curComparator);
        System.out.println(unitTable);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        serialization();
        initialization();
        MainMenu.start();
    }
}