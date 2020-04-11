package edu.duke651.wlt.client;

public class Printer {
    private PromptBase promptBase;
    public Printer(){
        promptBase = new PromptBase();
    }

    public void printInvalidAction(){
        System.out.println(promptBase.invalidAction_prompt);
    }
    public void printInvalidUnits(){
        System.out.println(promptBase.invalidUnits_prompt);
    }
    public void printLargerUnits(){
        System.out.println(promptBase.largerUnits_prompt);
    }
    public void printInvalidSourcePlace(){
        System.out.println(promptBase.invalidSourcePlace_prompt);
    }
    public void printInvalidEndPlace(){ System.out.println(promptBase.invalidEndPlace_prompt);}
    public void printInvalidAttackPlace(){ System.out.println(promptBase.invalidAttackPlace_prompt);}
    public void printNoMatchPlace(){
        System.out.println(promptBase.noMatchPlace_prompt);
    }
    public void printUnreachablePlace(){
        System.out.println(promptBase.unReachablePlace_prompt);
    }
    public void printCurrMap(){
        System.out.println(promptBase.currMap_Prompt());
    }
    public void printUnits_prompt(){System.out.println(promptBase.units_prompt);}
    public void printActionChoice(){
        System.out.println(promptBase.actionChoice_prompt);
    }
    public void printOriginalTerritoriesChoice() {
        System.out.println(promptBase.originalTerritoriesChoice_prompt);
    }
    public void printEndTerritoriesChoice(){
        System.out.println(promptBase.endTerritoriesChoice_prompt);
    }
    public void printEndRound_prompt(){
        System.out.println(promptBase.endRound_prompt);
    }
}
