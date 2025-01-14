package simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarehouseApp {
    public static Warehouse loadWarehouseFromFile(String filePath) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        return Warehouse.fromJson(jsonBuilder.toString());
    }

    public static void main(String[] args) {
        Map<String, Integer> assemblyPoint = Map.of("x", 0, "y", 0, "z", 0);
        Map<String, Integer> cellSize = Map.of("width", 100, "height", 25, "depth", 100);

        Warehouse warehouse = new Warehouse("WH001", assemblyPoint, cellSize, 6);

        List<Product> products = Product.generateProducts(600);
        List<Cell> cells = Cell.generateCells(633, warehouse.getLevels());

        warehouse.addCells(cells);

        System.out.printf("ЦФ, рандом: %,.2f %n", Distribution.distributeProductsRandomly(Warehouse.copy(warehouse), new ArrayList<Product>(products)));
        System.out.printf("ЦФ, TOPSIS: %,.2f %n", Distribution.distributeWithTOPSIS(Warehouse.copy(warehouse), new ArrayList<Product>(products)));
        System.out.printf("ЦФ, ELECTRE_TRI: %,.2f %n", Distribution.distributeWithELECTRE_TRI(Warehouse.copy(warehouse), new ArrayList<Product>(products)));
        System.out.printf("ЦФ, ELECTRE_TRI_and_TOPSIS: %,.2f %n", Distribution.distributeWithELECTRE_TRI_and_TOPSIS(Warehouse.copy(warehouse), new ArrayList<Product>(products)));


//        System.out.println("Склад и товары в JSON формате:");
//        System.out.println(warehouse.toJson());
//        try {
//            Warehouse warehouse = loadWarehouseFromFile("warehouse.json");
//
//            System.out.println("Склад из JSON:");
//            System.out.println(warehouse.toJson());
//
//        } catch (IOException e) {
//            System.err.println("Ошибка чтения JSON: " + e.getMessage());
//        }
    }
}
