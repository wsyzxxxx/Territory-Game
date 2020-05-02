package edu.duke651.wlt.server;

import edu.duke651.wlt.models.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

/**
 * @program: wlt-risc
 * @description: This is the game controller class which facilities the game in turn.
 * @author: Leo
 * @create: 2020-04-11 11:20
 **/
public class GameController {
    private HashMap<String, Player> players;
    private HashMap<Player, LinkInfo> playerLinkInfoHashMap;
    private HashMap<String, Territory> territoryMap;
    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;
    private ArrayList<ArrayList<Territory>> territoryGroups;

    /**
    * @Description: This function GameController is to initialize (thus build) a list of players based on client connections and their inputs, a list of connection info, a messageReceiver and a messageSender to communicate with all the clients. The first might throw IOException.
    * @Param: []
    * @return: 
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public GameController() throws IOException {
        this.players = new HashMap<>();
        this.playerLinkInfoHashMap = new HashMap<>();
        this.messageReceiver = new MessageReceiver();
        this.messageSender = new MessageSender();

        System.out.println("Init map");
        territoryMapInit();
        System.out.println("Create Link!");
        createLink();
        System.out.println("Assign map");
        assignTerritory();
    }

    /**
    * @Description: This function startGame is to start the game which turns into a loop, ending when there is only one player left is the winner.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public void startGame() throws IOException {
        while (!isGameOver()) {
            takeTurn();
            deletePlayers();
        }
        endGame();
    }

    /**
    * @Description: This function endGame is to send finish message to all the clients connected, indicating a closure of them.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void endGame() throws IOException {
        //send finish message
       for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
           messageSender.sendFinishMessage(linkInfo, players.values().iterator().next().getPlayerName());
           linkInfo.closeLink();
       }
    }

    /**
    * @Description: This function createLink is to create links between clients and server.
    * @Param: []
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    private void createLink() throws IOException {
        ServerSocket serverSocket = new ServerSocket(ServerSetting.PORT);
        while (playerLinkInfoHashMap.size() < ServerSetting.PLAYER_NUM) {
            LinkInfo linkInfo = new LinkInfo(serverSocket.accept());
            linkInfo.setPlayerName(linkInfo.readMessage());
            Player player = new Player(linkInfo.getPlayerName());
            playerLinkInfoHashMap.put(player, linkInfo);
            players.put(player.getPlayerName(), player);
            System.out.println("link: " + playerLinkInfoHashMap.size());
        }
        serverSocket.close();
    }

    /**
    * @Description: This function takeTurn is the turns in the game loop where players receive game info and then make move and attack decisions and send to server. Server is responsible for sending game info to every player at the start of every turn, and then receive orders, and then execute them. At the end of each turn, there should be a check whether one or more players lose the game and thus should be ignored of their incoming turns.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void takeTurn() throws IOException {
        //broadcast the map and player setting
        messageSender.sendResults(playerLinkInfoHashMap, territoryMap);

        //receive new orders
        Set<Player> illegalPlayerSet = new HashSet<>();
        ArrayList<ActionOrder> moveActionOrders = new ArrayList<>();
        ArrayList<ActionOrder> attackActionOrders = new ArrayList<>();
        ArrayList<UpgradeUnitOrder> upgradeUnitsOrders = new ArrayList<>();
        ArrayList<UpgradeTechOrder> upgradeTechOrders = new ArrayList<>();
        ArrayList<ActionOrder> allActionOrders = new ArrayList<>();
        for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
            if (players.containsKey(linkInfo.getPlayerName())) {
                try {
                    messageReceiver.receiveNewTurn(linkInfo, players, territoryMap, attackActionOrders, moveActionOrders, upgradeUnitsOrders, upgradeTechOrders);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error order with player " + linkInfo.getPlayerName() + e.getMessage());
                    illegalPlayerSet.add(players.get(linkInfo.getPlayerName()));
                } catch (Exception e) {
                    System.out.println("Network error with player " + linkInfo.getPlayerName());
                }
            }
        }
        allActionOrders.addAll(moveActionOrders);
        allActionOrders.addAll(attackActionOrders);

        System.out.println("check upgrade orders...");
        //check upgrade
        for (Player player : players.values()) {
            if (illegalPlayerSet.contains(player)) continue;
            int needTech = 0;
            for (UpgradeTechOrder upgradeTechOrder : upgradeTechOrders) {
                if (upgradeTechOrder.getPlayer() == player) {
                    if (upgradeTechOrder.checkLegal()) {
                        needTech += upgradeTechOrder.getTechCost();
                    } else {
                        System.out.println("Illegal tech order " + upgradeTechOrder.serialize().toString());
                        illegalPlayerSet.add(player);
                    }
                }
            }
            for (UpgradeUnitOrder upgradeUnitOrder : upgradeUnitsOrders) {
                if (upgradeUnitOrder.getPlayer() == player) {
                    if (upgradeUnitOrder.checkLegal()) {
                        needTech += upgradeUnitOrder.getTechCost();
                    } else {
                        System.out.println("Illegal upgrade order " + upgradeUnitOrder.serialize().toString());
                        illegalPlayerSet.add(player);
                    }
                }
            }
            if (needTech > player.getTechResources()) {
                System.out.println("No enough tech " + player.getPlayerName());
                illegalPlayerSet.add(player);
            }
        }

        System.out.println("do upgrading...");
        //execute upgrade units orders
        upgradeUnitsOrders.forEach(UpgradeUnitOrder::execute);

        System.out.println("check action orders...");
        //check if the orders are legal
        for (Territory territory : territoryMap.values()) {
            if (illegalPlayerSet.contains(territory.getTerritoryOwner())) continue;
            ArrayList<Integer> needUnits = new ArrayList<>(Collections.nCopies(7, 0));
            for (ActionOrder actionOrder : allActionOrders) {
                if (territory == actionOrder.getSource()) {
                    if (!actionOrder.checkLegal()) {
                        System.out.println("Illegal action order " + actionOrder.serialize().toString());
                        illegalPlayerSet.add(territory.getTerritoryOwner());
                        break;
                    }

                    ArrayList<Integer> units = actionOrder.getUnits();
                    for (int i = 0; i < needUnits.size(); i++) {
                        needUnits.set(i, needUnits.get(i) + units.get(i));
                    }
                }
            }
            if (!territory.hasEnoughUnits(needUnits)) {
                System.out.println("Illegal sum");
                illegalPlayerSet.add(territory.getTerritoryOwner());
            }
        }

        System.out.println("move out units...");
        //execute new orders
        //moveOut phase
        for (ActionOrder actionOrder : allActionOrders) {
            if (!illegalPlayerSet.contains(actionOrder.getPlayer())) {
                actionOrder.moveOut();
            }
        }

        System.out.println("execute orders...");
        //moveIn and attack phase
        //move orders first
        for (ActionOrder actionOrder : moveActionOrders) {
            if (!illegalPlayerSet.contains(actionOrder.getPlayer())) {
                actionOrder.execute();
            }
        }
        //attack orders then
        for (ActionOrder actionOrder : attackActionOrders) {
            if (!illegalPlayerSet.contains(actionOrder.getPlayer())) {
                actionOrder.execute();
            }
        }

        System.out.println("upgrade tech...");
        //carry out upgrade tech last
        upgradeTechOrders.forEach(UpgradeTechOrder::execute);

        System.out.println("refresh resources...");
        //refresh resources
        incrementTerritoryUnit();
        //This is for EVO2
        incrementResource();

        System.out.println("Illegal players: ");
        illegalPlayerSet.forEach(e -> System.out.println(e.getPlayerName()));
        System.out.println("Finish this round!");
    }

    /**
    * @Description: This function incrementTerritoryUnit is to increment territory unit by 1.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/20
    */
    private void incrementTerritoryUnit() {
        for (Territory territory : territoryMap.values()) {
            territory.incrementUnits();
        }
    }

    /**
    * @Description: This function incrementResource is to increment players' tech and food resource.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/20
    */
    private void incrementResource() {
        for (Player player : players.values()) {
            player.collectTechResource();
            player.collectFoodResource();
        }
    }

    /**
    * @Description: This function deletePlayers is to delete players from the player map who have lost all the territories.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void deletePlayers() {
        ArrayList<String> removeList = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.checkLose()) removeList.add(player.getPlayerName());
        }

        for (String playerName : removeList) {
            players.remove(playerName);
        }
    }

    /**
    * @Description: This function isGameOver is to check whether the game is over by checking the size of player map.
    * @Param: []
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private boolean isGameOver() {
        return players.size() == 1 || playerLinkInfoHashMap.isEmpty();
    }

    /**
    * @Description: This function assignTerritory is to assign territory groups randomly to all the players connected.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void assignTerritory() {
        //randomly assign territories to players
        Random random = new Random();
        ArrayList<String> colorArray = new ArrayList<>(Arrays.asList(ServerSetting.COLOR_SET));
        for (Player player: players.values()) {
            player.setFoodResources(ServerSetting.INIT_FOOD_RESOURCE_HOLD);
            //assign color
            player.setColor(colorArray.get(random.nextInt(colorArray.size())));
            colorArray.remove(player.getColor());

            ArrayList<Territory> territoryArrayList = this.territoryGroups.get(random.nextInt(territoryGroups.size()));
            for (Territory territory: territoryArrayList) {
                territory.setTerritoryOwner(player);
                player.addTerritory(territory);
                territoryMap.put(territory.getTerritoryName(), territory);
            }
            //messageSender.sendTerritoryList(playerLinkInfoHashMap.get(player), territoryArrayList);
            territoryGroups.remove(territoryArrayList);

            //assign units
            for (int i = 0; i < ServerSetting.INIT_UNITS; i++) {
                territoryArrayList.get(random.nextInt(territoryArrayList.size())).incrementUnits();
            }

            //assign size
            for (int i = 0; i < ServerSetting.INIT_SIZE - ServerSetting.INIT_SIZE_BASE * territoryArrayList.size(); i++) {
                territoryArrayList.get(random.nextInt(territoryArrayList.size())).increaseSize(1);
            }

            //assign techResourceGenerate
            for (int i = 0; i < ServerSetting.INIT_TECH_RESOURCE_GENERATE_LEVEL - ServerSetting.INIT_TECH_RESOURCE_GENERATE_LEVEL_BASE * territoryArrayList.size(); i++) {
                territoryArrayList.get(random.nextInt(territoryArrayList.size())).increaseTechResourceGenerateLevel(1);
            }

            //assign foodResourceGenerate
            for (int i = 0; i < ServerSetting.INIT_FOOD_RESOURCE_GENERATE_LEVEL - ServerSetting.INIT_FOOD_RESOURCE_GENERATE_LEVEL_BASE * territoryArrayList.size(); i++) {
                territoryArrayList.get(random.nextInt(territoryArrayList.size())).increaseFoodResourceGenerateLevel(1);
            }
        }
    }

    /**
    * @Description: This function territoryMapInit is to get a new map layout from the class TerritoryMapInit. This class can be inherited for more different maps. 
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void territoryMapInit() {
        TerritoryMapInit map = new TerritoryMapInit();
        this.territoryMap = map.getMap();
        this.territoryGroups = map.getGroups();
    }

}
