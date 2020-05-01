package edu.duke651.wlt.models;

import javafx.scene.shape.Polyline;
import javafx.util.Pair;

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
    public static final int INIT_TECH_RESOURCE_GENERATE_LEVEL_BASE = 5;
    public static final int INIT_TECH_RESOURCE_GENERATE_LEVEL = 60;
    public static final int INIT_FOOD_RESOURCE_GENERATE_LEVEL_BASE = 5;
    public static final int INIT_FOOD_RESOURCE_GENERATE_LEVEL = 60;
    public static final Map<Integer, String> PLAYER_TERRITORY_GROUP_MAP = new HashMap<Integer, String>() {{
        put(2, "[[\"Narnia\", \"Midkemia\", \"Oz\", \"Gondor\", \"Mordor\"], [\"Elantris\", \"Scadrial\", \"Roshar\", \"Hogwarts\"]]");
        put(3, "[[\"Narnia\", \"Midkemia\", \"Oz\"], [\"Gondor\", \"Mordor\", \"Hogwarts\"], [\"Elantris\", \"Scadrial\", \"Roshar\"]]");
        put(4, "[[\"Narnia\", \"Elantris\"], [\"Midkemia\", \"Oz\", \"Scadrial\"], [\"Gondor\", \"Mordor\"], [\"Hogwarts\", \"Roshar\"]]");
        put(5, "[[\"Narnia\", \"Midkemia\"], [\"Oz\", \"Gondor\"], [\"Mordor\", \"Hogwarts\"], [\"Elantris\"], [\"Scadrial\", \"Roshar\"]]");
    }};
    public static final String[] COLOR_SET = {"#00a6ac", "#e0861a", "#7fb80e", "#aa2116", "#694d9f"};
    public static final Map<String, Polyline> TERRITORY_POLY_MAP = new HashMap<String, Polyline>() {{
        put("Elantris", new Polyline(  300,200,  400,190, 400,270, 370,350, 320,350, 260,250, 300,200));
        put("Roshar", new Polyline(  400,270,  370,350, 570,350, 580,290, 480,290, 400,270));
        put("Scadrial", new Polyline(  400,270,  400,230, 500,210, 540,250, 590,250, 580,290, 480,290, 400,270));
        put("Hogwarts", new Polyline(  570,350,  580,290, 590,250, 660,240, 700,300, 650,370, 570,350));
        put("Mordor", new Polyline(  500,210,  540,250, 590,250, 660,240, 670,170, 500,210));
        put("Gondor", new Polyline(  585,190,  670,170, 680,175, 690,90, 590,100, 585,190));
        put("Narnia", new Polyline(  320,198,  400,190, 430,130, 380,100, 310,130, 320,198));
        put("Midkemia", new Polyline(  400,190,  400,230, 490,212, 590,100, 460,70, 400,190));
        put("Oz", new Polyline(  490,212,  500,210, 585,190, 590,100, 490,212));
    }};
    public static final Map<String, Pair<Integer, Integer>> NAME_ON_MAP = new HashMap<String, Pair<Integer, Integer>>() {{
        put("Elantris", new Pair<>(320, 250));
        put("Roshar", new Pair<>(430, 320));
        put("Scadrial", new Pair<>(450, 260));
        put("Hogwarts", new Pair<>(600, 300));
        put("Mordor", new Pair<>(570, 230));
        put("Gondor", new Pair<>(600, 150));
        put("Narnia", new Pair<>(330, 160));
        put("Midkemia", new Pair<>(460, 140));
        put("Oz", new Pair<>(540, 180));
    }};

    //Below is added for EVO2
    /**
    * @Description: This field is to keep techLevelCost in a map.
    * @Param:
    * @return:
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public static final Map<Integer, Integer> TECH_LEVEL_COST_MAP = new HashMap<Integer, Integer>() {{
        put(1, 0);
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
    public static final Map<Integer, UnitInfo> UNIT_LEVEL_COST_MAP = new HashMap<Integer, UnitInfo>() {{
        put(0, new UnitInfo(0, "Peasant", 0, 0));
        put(1, new UnitInfo(1, "Archer", 1, 3));
        put(2, new UnitInfo(2, "Swordsman", 3, 11));
        put(3, new UnitInfo(3, "Crusader", 5, 30));
        put(4, new UnitInfo(4, "Warrior", 8, 55));
        put(5, new UnitInfo(5, "Cavalier", 11, 90));
        put(6, new UnitInfo(6, "Paladin", 15, 140));
    }};
}
