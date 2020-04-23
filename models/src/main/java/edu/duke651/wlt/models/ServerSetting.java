package edu.duke651.wlt.models;

import org.checkerframework.checker.units.qual.UnitsBottom;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is ServerSetting class which contains the network information for one client's connection.
 * @author: Will
 * @create: 2020-04-09 11:55
 **/
public class ServerSetting {
    //public static final String HOST = "willxxx.xin";
    public static final String HOST = "localhost";
    public static final int PORT = 8888;
    public static final int PLAYER_NUM = 2;
    public static final int INIT_UNITS = 15;
    public static final int INIT_SIZE_BASE = 5;
    public static final int INIT_SIZE = 50;
    public static final int INIT_RESOURCE_GENERATE_LEVEL_BASE = 5;
    public static final int INIT_RESOURCE_GENERATE_LEVEL = 60;
    public static final Map<Integer, String> PLAYER_TERRITORY_GROUP_MAP = new HashMap<>() {{
        put(2, "[[\"Narnia\", \"Midkemia\", \"Oz\", \"Gondor\", \"Mordor\"], [\"Elantris\", \"Scadrial\", \"Roshar\", \"Hogwarts\"]]");
        put(3, "[[\"Narnia\", \"Midkemia\", \"Oz\"], [\"Gondor\", \"Mordor\", \"Hogwarts\"], [\"Elantris\", \"Scadrial\", \"Roshar\"]]");
        put(4, "[[\"Narnia\", \"Elantris\"], [\"Midkemia\", \"Oz\", \"Scadrial\"], [\"Gondor\", \"Mordor\"], [\"Hogwarts\", \"Roshar\"]]");
        put(5, "[[\"Narnia\", \"Midkemia\"], [\"Oz\", \"Gondor\"], [\"Mordor\", \"Hogwarts\"], [\"Elantris\"], [\"Scadrial\", \"Roshar\"]]");
    }};

    //Below is added for EVO2
    /**
    * @Description: This field is to keep techLevelCost in a map.
    * @Param:
    * @return:
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public static final Map<Integer, Integer> TECH_LEVEL_COST_MAP = new HashMap<>() {{
        put(2, 50);
        put(3, 75);
        put(4, 125);
        put(5, 200);
        put(6, 300);
    }};

    /**
    * @Description: This field is to keep unit information in a hash map.
    * @Param:
    * @return:
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public static final Map<Integer, UnitInfo> UNIT_LEVEL_COST_MAP = new HashMap<>() {{
        put(0, new UnitInfo(0, "Peasant", 0, 0));
        put(1, new UnitInfo(1, "Archer", 1, 3));
        put(2, new UnitInfo(2, "Swordsman", 3, 11));
        put(3, new UnitInfo(3, "Crusader", 5, 30));
        put(4, new UnitInfo(4, "Warrior", 8, 55));
        put(5, new UnitInfo(5, "Cavalier", 11, 90));
        put(6, new UnitInfo(6, "Paladin", 15, 140));
    }};
}
