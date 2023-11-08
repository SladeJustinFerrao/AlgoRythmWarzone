package Models;

import Constants.GameConstants;
import Controller.GameEngine;
import Utils.Command;
import Utils.UncaughtExceptionHandler;
import Views.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class StartUpPhase extends Phase {

    /**
     * Constructor for initializing the current game engine and game state.
     *
     * @param p_gameEngine Instance of the game engine to update the state
     * @param p_gameState  Instance of the game state
     */
    public StartUpPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    /**
     * {@inheritDoc}
     */
    public void performValidateMap(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to validate, Please `loadmap` & `editmap` first", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new Exception(GameConstants.INVALIDCOMMAND);
            } else {
                if (l_currentMap.Validate()) {
                    d_gameEngine.setD_gameEngineLog("The loaded map is valid!", GameConstants.OUTCOME);
                } else {
                    System.out.println("Failed to Validate map!");
                }
            }
        } else {
            throw new Exception(GameConstants.INVALIDCOMMAND);

        }
    }

    /**
     * {@inheritDoc}
     */
    public void performEditNeighbour(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Neighbors, please perform `editmap` first", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(GameConstants.OPERATIONS, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(GameConstants.OPERATIONS),
                            l_map.get(GameConstants.ARGUMENTS), 3);
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void performEditCountry(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Country, please perform `editmap` first", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(GameConstants.OPERATIONS, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(GameConstants.OPERATIONS),
                            l_map.get(GameConstants.ARGUMENTS), 2);
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void performLoadMap(Command p_command, Player p_player) throws Exception {
        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();
        boolean l_flagValidate = false;

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)) {
                    // Loads the map if it is valid or resets the game state
                    Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
                            l_map.get(GameConstants.ARGUMENTS));
                    if (l_mapToLoad.Validate()) {
                        l_flagValidate = true;
                        d_gameState.setD_loadCommand();
                        d_gameEngine.setD_gameEngineLog(l_map.get(GameConstants.ARGUMENTS) + " has been loaded to start the game", GameConstants.OUTCOME);
                    } else {
                        d_mapService.resetMap(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                    }
                    if (!l_flagValidate) {
                        d_mapService.resetMap(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                    }
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void performEditContinent(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Continent, please perform `editmap` first", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(GameConstants.OPERATIONS, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(GameConstants.ARGUMENTS),
                            l_map.get(GameConstants.OPERATIONS), 1);
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createPlayers(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found, Please `loadmap` before adding game players", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            if (d_gameState.getD_loadCommand()) {
                for (Map<String, String> l_map : l_operations_list) {
                    if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)
                            && p_command.checkRequiredKeysPresent(GameConstants.OPERATIONS, l_map)) {
                        d_playerService.updatePlayers(d_gameState, l_map.get(GameConstants.OPERATIONS),
                                l_map.get(GameConstants.ARGUMENTS));
                    } else {
                        throw new Exception(GameConstants.INVALIDCOMMAND);
                    }
                }
            } else {
                d_gameEngine.setD_gameEngineLog("Please load a valid map first via loadmap command!", GameConstants.OUTCOME);
            }
        }
    }

    @Override
    protected void performCreateDeploy(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    @Override
    protected void performAdvance(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    @Override
    protected void performCardHandle(String p_enteredCommand, Player p_player) {
        printInvalidCommandInState();
    }

    /**
     * {@inheritDoc}
     */
    public void performAssignCountries(Command p_command, Player p_player) throws Exception {
        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            d_playerService.assignCountries(d_gameState);
            d_playerService.assignArmies(d_gameState);
            d_gameEngine.setIssueOrderPhase();
        } else {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        }
    }

    @Override
    protected void performShowMap(Command p_command, Player p_player) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    /**
     * {@inheritDoc}
     */
    public void initPhase() {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

        while (d_gameEngine.getD_CurrentPhase() instanceof StartUpPhase) {
            try {
                System.out.println("Enter Game Commands or type 'exit' for quitting");
                String l_commandEntered = l_reader.readLine();

                handleCommand(l_commandEntered);
            } catch (Exception l_exception) {
                d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), GameConstants.OUTCOME);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void performMapEdit(Command p_command, Player p_player) throws Exception {
        List<java.util.Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (java.util.Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)) {
                    d_mapService.editMap(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void performSaveMap(Command p_command, Player p_player) throws Exception {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to save, Please `editmap` first", GameConstants.OUTCOME);
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getTaskandArguments();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new Exception(GameConstants.INVALIDCOMMAND);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(GameConstants.ARGUMENTS, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.saveMap(d_gameState,
                            l_map.get(GameConstants.ARGUMENTS));
                    if (l_fileUpdateStatus) {
                        d_gameEngine.setD_gameEngineLog("Required changes have been made in map file", GameConstants.OUTCOME);
                    } else
                        System.out.println(d_gameState.getError());
                } else {
                    throw new Exception(GameConstants.INVALIDCOMMAND);
                }
            }
        }
    }
}
