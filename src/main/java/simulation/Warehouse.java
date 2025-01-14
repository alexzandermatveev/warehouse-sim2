package simulation;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Warehouse {

    private String id;
    private Map<String, Integer> assemblyPoint;
    private Map<String, Integer> cellSize;
    private int levels;
    private Shelving shelving;

    @Getter
    private List<Cell> cells;

    public Warehouse(String id, Map<String, Integer> assemblyPoint, Map<String, Integer> cellSize, int levels) {
        this.id = id;
        this.assemblyPoint = assemblyPoint;
        this.cells = new ArrayList<>();
        this.cellSize = cellSize;
        this.levels = levels;
        shelving = new Shelving(cellSize.get("height"), levels);

    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public void addCells(List<Cell> cells) {
        this.cells.addAll(cells);
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("assemblyPoint", new JSONObject(assemblyPoint));
        json.put("cellSize", new JSONObject(cellSize));
        JSONArray cellArray = new JSONArray();
        for (Cell cell : cells) {
            cellArray.put(new JSONObject(cell.toMap()));
        }
        json.put("cells", cellArray);
        return json.toString(4);
    }

    public static Warehouse fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String id = json.getString("id");
        int levels = json.getInt("levels");
        Map<String, Integer> assemblyPoint = Map.of(
                "x", json.getJSONObject("assemblyPoint").getInt("x"),
                "y", json.getJSONObject("assemblyPoint").getInt("y"),
                "z", json.getJSONObject("assemblyPoint").getInt("z")
        );
        Map<String, Integer> cellSize = Map.of(
                "width", json.getJSONObject("cellSize").getInt("width"),
                "height", json.getJSONObject("cellSize").getInt("height"),
                "depth", json.getJSONObject("cellSize").getInt("depth"));
        Warehouse warehouse = new Warehouse(id, assemblyPoint, cellSize, levels);
        JSONArray cells = json.getJSONArray("cells");
        for (int i = 0; i < cells.length(); i++) {
            JSONObject cellJson = cells.getJSONObject(i);
            warehouse.addCell(Cell.fromMap(cellJson.toMap()));
        }
        return warehouse;
    }

    public static Warehouse copy(Warehouse warehouse) {
        Warehouse copy = new Warehouse(
                warehouse.getId(),
                new HashMap<String, Integer>(warehouse.assemblyPoint),
                new HashMap<String, Integer>(warehouse.cellSize),
                warehouse.levels
        );
        copy.cells = warehouse.getCells().stream()
                .map(Cell::clone)
                .collect(Collectors.toCollection(ArrayList::new));
        return copy;
    }

}
