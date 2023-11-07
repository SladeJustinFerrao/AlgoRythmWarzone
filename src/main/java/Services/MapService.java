package Services;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The MapService Class holds all operations for the map.
 */
public class MapService {

    /**
     * Method used to modify the Map
     *
     * @param p_gameState Current State of the game
     * @param p_editFile File name
     * @throws IOException
     */
    public void editMap(GameState p_gameState, String p_editFile) throws IOException {

        String l_filePath = getFilePath(p_editFile);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            Map l_map = new Map();
            l_map.setD_mapFile(p_editFile);
            p_gameState.setD_map(l_map);
            System.out.println("New Map File has been Created");
        } else {
            System.out.println("Map File is present");
            p_gameState.setD_map(new Map());
            this.loadMap(p_gameState, l_filePath);
            p_gameState.getD_map().setD_mapFile(p_editFile);
        }
    }

    /**
     * Method to Modify Continents
     *
     * @param p_gameState Current state of the game
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     * @throws IOException
     */
    public void editContinent(GameState p_gameState, String p_argument, String p_operation) throws IOException {
        String l_fileName = p_gameState.getD_map().getD_mapFile();
        Map l_map = p_gameState.getD_map();

        if(l_map!=null) {
            Map l_updatedMap = continentsToMap(l_map, p_argument, p_operation);
            p_gameState.setD_map(l_updatedMap);
        }
    }

    /**
     * Link Continents to Map
     *
     * @param p_map Map object
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     * @return Map Object
     */
    public Map continentsToMap(Map p_map, String p_argument, String p_operation) {
        String[] l_arguments = p_argument.split(" ");
        if (p_operation.equalsIgnoreCase("add") && l_arguments.length==2) {
            p_map.addContinent(l_arguments[0], Integer.parseInt(l_arguments[1]));
        } else if (p_operation.equalsIgnoreCase("remove") && l_arguments.length==1) {
            p_map.removeContinent(l_arguments[0]);
        } else {
            System.out.println("Error! Continents could not be modified, no changes made.");
        }

        return p_map;
    }

    /**
     * Method to Modify Countries
     *
     * @param p_gameState Current state of the game
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     */
    public void editCountry(GameState p_gameState, String p_argument, String p_operation) {
        String l_fileName= p_gameState.getD_map().getD_mapFile();
        Map l_map = p_gameState.getD_map();

        if(l_map!=null){
            Map l_updatedMap = countryToMap(l_map, p_argument, p_operation);
            p_gameState.setD_map(l_updatedMap);
        }
    }

    /**
     * Link Countries to Map
     *
     * @param p_map Map object
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     * @return Map Object
     */
    public Map countryToMap(Map p_map, String p_argument, String p_operation) {
        String[] l_arguments = p_argument.split(" ");
        if (p_operation.equalsIgnoreCase("add") && l_arguments.length==2){
            p_map.addCountry(l_arguments[0], l_arguments[1]);
        }else if(p_operation.equalsIgnoreCase("remove") && l_arguments.length==1){
            p_map.removeCountry(l_arguments[0]);
        }else{
            System.out.println("Error! Countries could not be modified, no changes made.");
        }
        return p_map;
    }

    /**
     * Method to Modify neighbouring countries
     *
     * @param p_gameState Current state of the game
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     */
    public void editNeighbour(GameState p_gameState, String p_argument, String p_operation) {
        String l_fileName= p_gameState.getD_map().getD_mapFile();
        Map l_map = p_gameState.getD_map();

        if(l_map!=null){
            Map l_updatedMap = neighbourToMap(l_map, p_argument, p_operation);
            p_gameState.setD_map(l_updatedMap);
        }
    }

    /**
     * Link Neighbours to Map
     *
     * @param p_map Map object
     * @param p_argument Arguments in the command
     * @param p_operation Operation of the command
     * @return Map Object
     */
    public Map neighbourToMap(Map p_map, String p_argument, String p_operation) {
        String[] l_arguments = p_argument.split(" ");
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
            p_map.addCountryNeighbour(l_arguments[0], l_arguments[1]);
        }else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
            p_map.removeCountryNeighbour(l_arguments[0], l_arguments[1]);
        }else{
            System.out.println("Error! Neighbours could not be modified, no changes made.");
        }

        return p_map;
    }

    /**
     * Link Country to Continents
     *
     * @param p_countries List of Countries
     * @param p_continents List of Continents
     * @return List of Continents
     */
    public List<Continent> countryToContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country l_country : p_countries) {
            for (Continent l_continent : p_continents) {
                if (l_continent.getD_continentID().equals(l_country.getD_continentId())) {
                    l_continent.addCountry(l_country);
                }
            }
        }
        return p_continents;
    }

    /**
     * Method to save the map
     * @param p_gameState Current Game State
     * @param p_fileName Name of the file
     * @return boolean true if map save was successful else false
     */
    public boolean saveMap(GameState p_gameState, String p_fileName){
        try {

            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setError("Filename to save does not match with the name provided for edit. Kindly provide the same name.");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();

                    System.out.println("Running Map Validation........");
                    boolean l_mapValidationStatus = l_currentMap.Validate();
                    if (l_mapValidationStatus) {
                        Files.deleteIfExists(Paths.get(getFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(getFilePath(p_fileName));

                        if (null != p_gameState.getD_map().getD_continents()
                                && !p_gameState.getD_map().getD_continents().isEmpty()) {
                            writeContinentdata(p_gameState, l_writer);
                        }
                        if (null != p_gameState.getD_map().getD_countries()
                                && !p_gameState.getD_map().getD_countries().isEmpty()) {
                            writeCountryAndNeighbourData(p_gameState, l_writer);
                        }
                        l_writer.close();
                    }
                } else {
                    p_gameState.setError("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException l_e) {
            l_e.printStackTrace();
            p_gameState.setError("Error in saving map file");
            return false;
        }
    }

    /**
     * Write Country and Neighbour into file
     *
     * @param p_gameState Current State of the Game
     * @param p_writer File Writer
     * @throws IOException
     */
    private void writeCountryAndNeighbourData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryData = new String();
        String l_bordersData = new String();
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + "[countries]" + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryData = new String();

            l_countryData = ((Integer) l_country.getD_countryId()).toString() + " " + (l_country.getD_countryName()) + " " + (((Integer) l_country.getD_continentId()).toString());
            l_countryData = ((Integer) l_country.getD_countryId()).toString() + " " + (l_country.getD_countryName()) + " " + (((Integer) l_country.getD_continentId()).toString());
            p_writer.write(l_countryData + System.lineSeparator());

            if (null != l_country.getD_neighbourCountryId() && !l_country.getD_neighbourCountryId().isEmpty()) {
                l_bordersData = new String();
                l_bordersData = ((Integer) l_country.getD_countryId()).toString();
                for (Integer l_adjCountry : l_country.getD_neighbourCountryId()) {
                    l_bordersData = l_bordersData + " " + (l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersData);
            }
        }

        if (!l_bordersList.isEmpty() && null != l_bordersList) {
            p_writer.write(System.lineSeparator() + "[borders]" + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    /**
     * Write Continents into file
     *
     * @param p_gameState Current State of the Game
     * @param p_writer File Writer
     * @throws IOException
     */
    private void writeContinentdata(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + "[continents]" + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_continent.getD_continentName() + " " + (l_continent.getD_continentValue().toString()) + System.lineSeparator());
        }
    }

    /**
     * Method to load Existing Map file
     *
     * @param p_gameState Current State of the Game
     * @param p_filePath Path of the filename provided
     * @return
     */
    public Map loadMap(GameState p_gameState, String p_filePath) {
        Map l_map = new Map();
        List<String> l_linesOfFile = loadFile(p_filePath);

        if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {

            List<String> l_continentData = getFileData(l_linesOfFile, "continent");
            List<Continent> l_continentObjects = parseContinentsData(l_continentData);
            List<String> l_countryData = getFileData(l_linesOfFile, "country");
            List<String> l_neighbourData = getFileData(l_linesOfFile, "neighbour");
            List<Country> l_countryObjects = parseCountriesData(l_countryData);

            l_countryObjects = parseNeighbourData(l_countryObjects, l_neighbourData);
            l_continentObjects = countryToContinents(l_countryObjects, l_continentObjects);
            l_map.setD_continents(l_continentObjects);
            l_map.setD_countries(l_countryObjects);
            p_gameState.setD_map(l_map);
        }

        return l_map;
    }

    /**
     * Method to extract contents of the file
     *
     * @param p_filePath Path of the filename provided
     * @return Lines of file
     */
    public List<String> loadFile(String p_filePath) {
        List<String> l_fileLines = new ArrayList<>();
        BufferedReader l_reader;
        try {
            l_reader = new BufferedReader(new FileReader(p_filePath));
            l_fileLines = l_reader.lines().collect(Collectors.toList());
            l_reader.close();
        } catch (IOException l_e1) {
            System.out.println("File not Found!");
        }
        return l_fileLines;
    }

    /**
     * Retrieve Contents of the file lines.
     *
     * @param p_linesOfFile Line of File
     * @param p_case Switch Case
     * @return List of String with particular lines
     */
    public List<String> getFileData(List<String> p_linesOfFile, String p_case) {
        switch (p_case) {
            case "continent":
                List<String> l_continentLines = p_linesOfFile.subList(
                        p_linesOfFile.indexOf("[continents]") + 1,
                        p_linesOfFile.indexOf("[countries]") - 1);
                return l_continentLines;
            case "country":
                List<String> l_countryLines = p_linesOfFile.subList(p_linesOfFile.indexOf("[countries]") + 1,
                        p_linesOfFile.indexOf("[borders]") - 1);
                return l_countryLines;
            case "neighbour":
                List<String> l_neightboursLines = p_linesOfFile.subList(p_linesOfFile.indexOf("[borders]") + 1,
                        p_linesOfFile.size());
                return l_neightboursLines;
            default:
                return null;
        }
    }

    /**
     * Retrieve formatted Continents data from Line of Files
     *
     * @param p_continentList Unformatted Continents data
     * @return Formatted Continents data
     */
    public List<Continent> parseContinentsData(List<String> p_continentList) {
        List<Continent> l_continents = new ArrayList<Continent>();
        int l_continentId = 1;

        for (String l_continent : p_continentList) {
            String[] l_data = l_continent.split(" ");
            l_continents.add(new Continent(l_continentId, l_data[0], Integer.parseInt(l_data[1])));
            l_continentId++;
        }
        return l_continents;
    }

    /**
     * Retrieve formatted Countries data from Line of Files
     *
     * @param p_countriesList Unformatted Countries data
     * @return Formatted Countries data
     */
    public List<Country> parseCountriesData(List<String> p_countriesList) {
        List<Country> l_countriesList = new ArrayList<Country>();
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String country : p_countriesList) {
            String[] l_data = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_data[0]), l_data[1],
                    Integer.parseInt(l_data[2])));
        }

        return l_countriesList;
    }

    /**
     * Retrieve formatted Neighbour data from Line of Files
     *
     * @param p_countriesList Countries data without neighbours
     * @param p_neighbourList Unformatted neighbours data
     * @return Formatted Countries data with Neighbour
     */
    public List<Country> parseNeighbourData(List<Country> p_countriesList, List<String> p_neighbourList) {

        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String l_neighbour : p_neighbourList) {
            if (null != l_neighbour && !l_neighbour.isEmpty()) {
                ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
                String[] l_splitString = l_neighbour.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++) {
                    l_neighbours.add(Integer.parseInt(l_splitString[i]));
                }
                l_countryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighbours);
            }
        }
        for (Country l_country : p_countriesList) {
            List<Integer> l_neighbourCountries = l_countryNeighbors.get(l_country.getD_countryId());
            l_country.setD_neighbourCountryId(l_neighbourCountries);
        }
        return p_countriesList;
    }

    /**
     * Fetch the file path.
     *
     * @param p_fileName Name of file.
     * @return Path of file
     */
    public String getFilePath(String p_fileName) {
        return new File("").getAbsolutePath() + File.separator + "src/main/resources" + File.separator + p_fileName;
    }

    /**
     * Method to reset the Current State
     * @param p_gameState Current Game state
     */
    public void resetState(GameState p_gameState) {
        System.out.println("Invalid Map! Map cannot be loaded. Kindly use a valid map");
        p_gameState.setD_map(new Models.Map());
    }

    public void editFunctions(GameState p_gameState, String p_argument, String p_operation, Integer p_switch) throws IOException{

        }

    public void resetMap(GameState p_gameState, String p_fileToLoad) {
    }
}
