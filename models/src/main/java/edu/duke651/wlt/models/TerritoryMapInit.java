package edu.duke651.wlt.models;

import javafx.geometry.HorizontalDirection;

import java.lang.module.ModuleDescriptor;
import java.util.*;

/**
 * @program: wlt-risc
 * @description: This is the class used to initialize map, in case of possible more maps later.
 * @author: Leo
 * @create: 2020-04-11 11:28
 **/
public class TerritoryMapInit {

    private HashMap<String, Territory> territoryMapInit = new HashMap<>();

    /**
    * @Description: This function TerritoryMapInit is to generate the specific map of this game.
    * @Param: []
    * @return: 
    * @Author: Leo
    * @Date: 2020/4/11
    */
    public TerritoryMapInit() {
        Territory Narnia = new Territory("Narnia");
        Territory Midkemia = new Territory("Midkemia");
        Territory Oz = new Territory("Oz");
        Territory Gondor = new Territory("Gondor");
        Territory Elantris = new Territory("Elantris");
        Territory Scadrial = new Territory("Scadrial");
        Territory Roshar = new Territory("Roshar");
        Territory Mordor = new Territory("Mordor");
        Territory Hogwarts = new Territory("Hogwarts");
        //NarniaInit
        Narnia.addTerritoryNeighbors(Elantris);
        Narnia.addTerritoryNeighbors(Midkemia);
        //MidkemiaInit
        Midkemia.addTerritoryNeighbors(Narnia);
        Midkemia.addTerritoryNeighbors(Elantris);
        Midkemia.addTerritoryNeighbors(Scadrial);
        Midkemia.addTerritoryNeighbors(Oz);
        //Oz
        Oz.addTerritoryNeighbors(Midkemia);
        Oz.addTerritoryNeighbors(Scadrial);
        Oz.addTerritoryNeighbors(Gondor);
        Oz.addTerritoryNeighbors(Mordor);
        //Gondor
        Gondor.addTerritoryNeighbors(Oz);
        Gondor.addTerritoryNeighbors(Mordor);
        //Elantris
        Elantris.addTerritoryNeighbors(Narnia);
        Elantris.addTerritoryNeighbors(Midkemia);
        Elantris.addTerritoryNeighbors(Scadrial);
        Elantris.addTerritoryNeighbors(Roshar);
        //Scandrial
        Scadrial.addTerritoryNeighbors(Elantris);
        Scadrial.addTerritoryNeighbors(Midkemia);
        Scadrial.addTerritoryNeighbors(Oz);
        Scadrial.addTerritoryNeighbors(Mordor);
        Scadrial.addTerritoryNeighbors(Hogwarts);
        Scadrial.addTerritoryNeighbors(Roshar);
        //Mordor
        Mordor.addTerritoryNeighbors(Oz);
        Mordor.addTerritoryNeighbors(Scadrial);
        Mordor.addTerritoryNeighbors(Gondor);
        Mordor.addTerritoryNeighbors(Hogwarts);
        //Hogwarts
        Hogwarts.addTerritoryNeighbors(Scadrial);
        Hogwarts.addTerritoryNeighbors(Mordor);
        Hogwarts.addTerritoryNeighbors(Roshar);
        //Roshar
        Roshar.addTerritoryNeighbors(Elantris);
        Roshar.addTerritoryNeighbors(Scadrial);
        Roshar.addTerritoryNeighbors(Hogwarts);
        //put in
        territoryMapInit.put(Narnia.getTerritoryName(), Narnia);
        territoryMapInit.put(Elantris.getTerritoryName(), Elantris);
        territoryMapInit.put(Oz.getTerritoryName(), Oz);
        territoryMapInit.put(Gondor.getTerritoryName(), Gondor);
        territoryMapInit.put(Scadrial.getTerritoryName(), Scadrial);
        territoryMapInit.put(Midkemia.getTerritoryName(), Midkemia);
        territoryMapInit.put(Mordor.getTerritoryName(), Mordor);
        territoryMapInit.put(Hogwarts.getTerritoryName(), Hogwarts);
        territoryMapInit.put(Roshar.getTerritoryName(), Roshar);

    }

    /**
    * @Description: This function getMap is to return the specific map.
    * @Param: []
    * @return: java.util.HashMap<java.lang.String,edu.duke651.wlt.models.Territory>
    * @Author: Leo
    * @Date: 2020/4/11
    */
    public HashMap<String, Territory> getMap() {
        return territoryMapInit;
    }
}
