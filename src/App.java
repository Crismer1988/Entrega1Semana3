import java.io.*;
import java.util.*;

class GenerateInfoFiles {
    public static void main(String[] args) {
        try {
            createSalesMenFile(10, "SalesmanInfo", 1001);
            createProductsFile(5);
            createSalesManInfoFile(10);
            System.out.println("Information files generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating information files: " + e.getMessage());
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        Random random = new Random();
        try (PrintWriter writer = new PrintWriter(name + ".txt")) {
            for (int i = 0; i < randomSalesCount; i++) {
                writer.println("SellerIDType" + ";" + id);
                for (int j = 1; j <= random.nextInt(5) + 1; j++) {
                    writer.println("ProductID" + j + ";" + (random.nextInt(10) + 1));
                }
            }
        }
    }

    public static void createProductsFile(int productsCount) throws IOException {
        try (PrintWriter writer = new PrintWriter("ProductInfo.txt")) {
            Random random = new Random();
            for (int i = 1; i <= productsCount; i++) {
                writer.println("ProductID" + i + ";Product" + i + ";" + (random.nextInt(100) + 1));
            }
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        Random random = new Random();
        String[] firstNames = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Alexander", "Mia"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Lee", "Wilson", "Taylor", "Clark", "Lewis", "Walker", "Moore"};

        try (PrintWriter writer = new PrintWriter("SalesmanInfo.txt")) {
            for (int i = 0; i < salesmanCount; i++) {
                String firstName = firstNames[random.nextInt(firstNames.length)];
                String lastName = lastNames[random.nextInt(lastNames.length)];
                long id = 1000 + i;
                writer.println("IDType;" + id + ";" + firstName + ";" + lastName);
            }
        }
    }
}