package edu.duke651.wlt.server;

/**
 * @program: wlt-risc
 * @description:
 * @author: Leo
 * @create: 2020-04-09 11:58
 **/
public abstract class Order {
    //fields:
    Player player;
    Territory source;
    Territory aim;
    int numUnits;

    //methods:
    abstract void execute();
    abstract boolean checkLegal();

}
