package simulation;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements Comparable<Product> {
    private String id;
    private String name;
    private Map<String, Integer> dimensions; // width, height, depth
    private LocalDate expiryDate;
    private int demand; // уровень востребованности

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("dimensions", dimensions);
        map.put("expiryDate", expiryDate != null ? expiryDate.toString() : null);
        map.put("demand", demand);
        return map;
    }

    public static Product fromMap(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        Map<String, Integer> dimensions = (Map<String, Integer>) map.get("dimensions");
        String expiryDateStr = (String) map.get("expiryDate");
        LocalDate expiryDate = expiryDateStr != null ? LocalDate.parse(expiryDateStr) : null;
        return new Product(
                (String) map.get("id"),
                (String) map.get("name"),
                dimensions,
                expiryDate,
                (int) map.get("demand")
        );
    }


    // Метод для генерации товаров
    public static List<Product> generateProducts(int count) {
        List<Product> products = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= count; i++) {
            products.add(new Product(
                    "P" + i,
                    "Product_" + i,
                    Map.of("width", 50, "height", 20, "depth", 30),
                    LocalDate.now().plusDays(random.nextInt(365) + 1),
                    random.nextInt(100) + 1
            ));
        }
        return products;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public int compareTo(Product o) {
        int id = Integer.parseInt(this.id.substring(1));
        int anotherId = Integer.parseInt(o.id.substring(1));
        return Integer.compare(id, anotherId);
    }
}
