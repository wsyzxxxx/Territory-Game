package edu.duke651.wlt.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is ServerSetting class which contains the network information for one client's connection.
 * @author: Will
 * @create: 2020-04-09 11:55
 **/
public class ServerSetting {
    public static final String HOST = "willxxx.xin";
    //public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int PLAYER_NUM = 3;
    public static final int INIT_UNITS = 200;
    public static final Map<Integer, String> PLAYER_TERRITORY_GROUP_MAP = new HashMap<Integer, String>() {{
       put(2, "[[\"Narnia\", \"Midkemia\", \"Oz\", \"Gondor\", \"Mordor\"], [\"Elantris\", \"Scadrial\", \"Roshar\", \"Hogwarts\"]]");
       put(3, "[[\"Narnia\", \"Midkemia\", \"Oz\"], [\"Gondor\", \"Mordor\", \"Hogwarts\"], [\"Elantris\", \"Scadrial\", \"Roshar\"]]");
       put(4, "[[\"Narnia\", \"Elantris\"], [\"Midkemia\", \"Oz\", \"Scadrial\"], [\"Gondor\", \"Mordor\"], [\"Hogwarts\", \"Roshar\"]]");
       put(5, "[[\"Narnia\", \"Midkemia\"], [\"Oz\", \"Gondor\"], [\"Mordor\", \"Hogwarts\"], [\"Elantris\"], [\"Scadrial\", \"Roshar\"]]");
    }};
}
