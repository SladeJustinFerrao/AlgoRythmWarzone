package Models;

import Controller.GameEngine;
import Utils.Command;

import java.io.IOException;

public abstract class Phase {

    /**
     * Stores the information about the current GamePlay.
     */
    GameState d_gameState;

    /**
     * Stores the information about the current GamePlay.
     */
    GameEngine d_gameEngine;

    /**
     * Flag to determine if the map is loaded.
     */
    boolean l_isMapLoaded;

    /**
     * Redirect to specific phase implementations the command which were entered  by user.
     *
     * @param p_enteredCommand Command entered by the user in the Command Line Interface (CLI)
     * @param p_player         Player instance
     * @throws IOException Indicates a failure in I/O operation
     */
    private void commandHandler(String p_enteredCommand, Player p_player) throws IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_rootCommand = l_command.getMainCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");
        switch (l_rootCommand) {
            case "editmap": {
                performMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performEditContinent(l_command, p_player);
                break;
            }
            case "deploy": {
                performCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "gameplayer": {
                createPlayers(l_command, p_player);
                break;
            }
            case "savemap": {
                performSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performValidateMap(l_command, p_player);
                break;
            }
            case "advance": {
                performAdvance(p_enteredCommand, p_player);
                break;
            }
            case "editcountry": {
                performEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performEditNeighbour(l_command, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
            {
                performCardHandle(p_enteredCommand, p_player);
                break;
            }
            case "assigncountries": {
                performAssignCountries(l_command, p_player);
                break;
            }
            case "showmap": {
                performShowMap(l_command, p_player);
                break;
            }
            case "exit": {
                d_gameEngine.setD_gameEngineLog("Exit Command Entered, Game Ends!", "effect");
                System.exit(0);
                break;
            }
            default: {
                d_gameEngine.setD_gameEngineLog("Invalid Command", "effect");
                break;
            }
        }
    }

    /**
     * Basic validation of <strong>validatemap</strong> command for checking
     * required arguments and redirecting control to the model for actual processing.
     *
     * @param p_command Command entered by the user on CLI
     * @param p_player  Instance of Player Object
     * @throws IOException    Indicates a failure in I/O operation
     */
    protected abstract void performValidateMap(Command p_command, Player p_player) throws IOException;

    /**
     * Basic validation of <strong>editneighbor</strong> command for checking
     * required arguments and redirecting control to the model for actual processing.
     *
     * @param p_command Command entered by the user in the CLI
     * @param p_player  Instance of Player Object
     * @throws IOException    Handles File I/O Exception
     */
    protected abstract void performEditNeighbour(Command p_command, Player p_player) throws IOException;
    /**
     * Basic validation of <strong>editcountry</strong> command for checking
     * required arguments and redirecting control to the model for actual processing.
     *
     * @param p_command Command entered by the user on CLI
     * @param p_player  Instance of Player Object
     * @throws IOException    Handles File I/O Exception
     */
    protected abstract void performEditCountry(Command p_command, Player p_player) throws IOException;

    /**
     * Basic validation of <strong>loadmap</strong> command for checking required
     * arguments and redirecting control to the model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player instance of Player Object
     * @throws IOException  indicates failure in I/O operation
     */
    protected abstract void performLoadMap(Command p_command, Player p_player) throws IOException;

    /**
     * Basic validation of <strong>savemap</strong> command for checking required
     * arguments and redirecting control to the model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player instance of Player Object
     * @throws IOException  indicates failure in I/O operation
     */
    protected abstract void performSaveMap(Command p_command, Player p_player) throws IOException;


    /**
     * Basic validation of <strong>editmap</strong> command for checking required
     * arguments and redirecting control to the model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player instance of Player Object
     * @throws IOException indicates when failure in I/O operation
     */
    protected abstract void performMapEdit(Command p_command, Player p_player) throws IOException;

    /**
     * Basic validation of <strong>editcontinent</strong> command for checking
     * required arguments and redirecting control to the model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player instance of Player Object
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void performEditContinent(Command p_command, Player p_player) throws IOException;

    /**
     * Basic validation of gameplayer command for checking required arguments and redirecting control to the model for adding or removing players.
     *
     * @param p_command Command entered by the user in the CLI
     * @param p_player  Instance of Player Object
     * @throws IOException    Indicates a failure in I/O operation
     */
    protected abstract void createPlayers(Command p_command, Player p_player) throws IOException;

    /**
     * Handles the deployment order in the gameplay.
     *
     * @param p_command Command entered by the user
     * @param p_player  Instance of the player object
     * @throws IOException Indicates failure in I/O operation
     */
    protected abstract void performCreateDeploy(String p_command, Player p_player) throws IOException;


    /**
     * Handles the advance order in the gameplay.
     *
     * @param p_command Command entered by the user
     * @param p_player  Instance of the player object
     * @throws IOException Indicates failure in I/O operation
     */
    protected abstract void performAdvance(String p_command, Player p_player) throws IOException;

    /**
     * Handles the card commands.
     *
     * @param p_enteredCommand String representing the entered command
     * @param p_player         Player instance
     * @throws IOException Signals an I/O exception
     */
    protected abstract void performCardHandle(String p_enteredCommand, Player p_player) throws IOException;

    /**
     * Basic validation of <strong>assigncountries</strong> to check for required
     * arguments and redirect control to the model for assigning countries to players.
     *
     * @param p_command Command entered by the user in the CLI
     * @param p_player  Instance of the Player Object
     * @throws IOException    Indicates failure in I/O operation
     */
    protected abstract void performAssignCountries(Command p_command, Player p_player) throws IOException;

    /**
     * Handles the 'show map' command.
     *
     * @param p_command Command entered by the user
     * @param p_player  Player object instance
     * @throws IOException    Indicates a failure in I/O operation
     */
    protected abstract void performShowMap(Command p_command, Player p_player) throws IOException;

}
