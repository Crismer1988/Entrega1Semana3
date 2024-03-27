import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Read seller information
            Map<Long, String[]> salesmanInfo = readSalesmanInfo("SalesmanInfo.txt");

            // Read product information
            Map<String, Double> productInfo = readProductInfo("ProductInfo.txt");

            // Calculate revenue per seller
            Map<Long, Double> salesBySalesman = calculateSalesBySalesman("SalesmanInfo", productInfo);

            // Sort sellers by collection
            List<Map.Entry<Long, Double>> sortedSalesBySalesman = new ArrayList<>(salesBySalesman.entrySet());
            sortedSalesBySalesman.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Generate sales report by seller
            PrintWriter salesReportWriter = new PrintWriter("SalesReport.csv");
            for (Map.Entry<Long, Double> entry : sortedSalesBySalesman) {
                long id = entry.getKey();
                double totalSales = entry.getValue();
                String[] info = salesmanInfo.get(id);
                salesReportWriter.println(info[2] + " " + info[3] + ";" + totalSales);
            }
            salesReportWriter.close();

            // Generate report of products sold
            PrintWriter productsReportWriter = new PrintWriter("ProductsReport.csv");
            Map<String, Integer> totalSoldProducts = new HashMap<>();
            for (String productId : productInfo.keySet()) {
                totalSoldProducts.put(productId, 0);
            }
            File folder = new File("SalesmanInfo");
            File[] salesmanFiles = folder.listFiles();
            if (salesmanFiles != null) {
                for (File file : salesmanFiles) {
                    Map<String, Integer> soldProducts = readSoldProducts(file.getAbsolutePath());
                    for (Map.Entry<String, Integer> entry : soldProducts.entrySet()) {
                        String productId = entry.getKey();
                        int quantity = entry.getValue();
                        totalSoldProducts.put(productId, totalSoldProducts.get(productId) + quantity);
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : totalSoldProducts.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                double price = productInfo.get(productId);
                productsReportWriter.println(productId + ";" + quantity + ";" + price);
            }
            productsReportWriter.close();

            System.out.println("Report files generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating report files: " + e.getMessage());
        }
    }

    private static Map<Long, String[]> readSalesmanInfo(String filename) throws IOException {
        Map<Long, String[]> salesmanInfo = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                long id = Long.parseLong(parts[1]);
                String[] info = {parts[0], parts[2], parts[3]};
                salesmanInfo.put(id, info);
            }
        }
        return salesmanInfo;
    }

    private static Map<String, Double> readProductInfo(String filename) throws IOException {
        Map<String, Double> productInfo = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts[0];
                double price = Double.parseDouble(parts[2]);
                productInfo.put(id, price);
            }
        }
        return productInfo;
    }

    private static Map<Long, Double> calculateSalesBySalesman(String folderName, Map<String, Double> productInfo) {
        Map<Long, Double> salesBySalesman = new HashMap<>();
        File folder = new File(folderName);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    double totalSales = 0;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(";");
                        if (parts.length == 2) {
                            String productId = parts[0];
                            int quantity = Integer.parseInt(parts[1]);
                            double price = productInfo.get(productId);
                            totalSales += quantity * price;
                        }
                    }
                    long id = Long.parseLong(file.getName().replaceAll("[^\\d]", ""));
                    salesBySalesman.put(id, totalSales);
                } catch (IOException e) {
                    System.err.println("Error calculating seller sales: " + file.getName() + ". " + e.getMessage());
                }
            }
        }
        return salesBySalesman;
    }

    private static Map<String, Integer> readSoldProducts(String filename) throws IOException {
        Map<String, Integer> soldProducts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String productId = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    soldProducts.put(productId, quantity);
                }
            }
        }
        return soldProducts;
    }
}