package edu.duke651.wlt.client;

public class Printer {
    private static PromptBase promptBase;
    private static Printer instance = new Printer();
    private Printer(){}
    public static Printer getInstance(){
        promptBase = new PromptBase();
        return instance;
    }
    void printInvalidAction(){
        System.out.println(promptBase.invalidAction_prompt);
    }
    void printInvalidUnits(){
        System.out.println(promptBase.invalidUnits_prompt);
    }
    void printLargerUnits(){
        System.out.println(promptBase.largerUnits_prompt);
    }
    void printInvalidPlace(){
        System.out.println(promptBase.invalidPlace_prompt);
    }
    void printNoMatchPlace(){
        System.out.println(promptBase.noMatchPlace_prompt);
    }
    void printUnreachablePlace(){
        System.out.println(promptBase.unReachablePlace_prompt);
    }
    void printCurrMap(TerritoryRelation territoryRelation){
        System.out.println(promptBase.currMap_Prompt(territoryRelation));
    }
    void printActionChoice(){
        System.out.println(promptBase.actionChoice_prompt);
    }
    void printOriginalTerritoriesChoice() {
        System.out.println(promptBase.originalTerritoriesChoice_prompt);
    }
    void printEndTerritoriesChoice(){
        System.out.println(promptBase.endTerritoriesChoice_prompt);
    }

}
