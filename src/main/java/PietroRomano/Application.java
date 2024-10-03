package PietroRomano;



import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class Application {

    public static void main(String[] args) {

        Faker faker = new Faker();
        System.out.println(faker.name().fullName());

        System.out.println("-------------------------------------------------------------------");
        System.out.println("Genero clienti casuali: ");
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customers.add(new Customer((long) i, faker.name().fullName(), faker.number().numberBetween(1, 2)));
            System.out.println(customers);
    }

        System.out.println("-------------------------------------------------------------------");
        System.out.println("Genero prodotti casuali: ");
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            products.add(new Product((long) i, faker.commerce().productName(), faker.commerce().department(), faker.number().randomDouble(2, 10, 1000)));
            System.out.println(products);
        }

        System.out.println("-------------------------------------------------------------------");
        System.out.println("Genero ordini casuali: ");
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int customerId = faker.number().numberBetween(0, customers.size());
            List<Product> orderProducts = new ArrayList<>();
            for (int j = 0; j < faker.number().numberBetween(1, 5); j++) {
                orderProducts.add(products.get(faker.number().numberBetween(0, products.size())));
            }
            orders.add(new Order((long) i, faker.lorem().word(),
                    LocalDate.now().minusDays(faker.number().numberBetween(1, 30)),
                    LocalDate.now().plusDays(faker.number().numberBetween(1, 30)),
                    orderProducts, customers.get(customerId)));
        }

        System.out.println("------------------------------ESERCIZIO N'1-------------------------------------");
        System.out.println(" ");
        System.out.println("Raggruppamento ordini per cliente: ");
        Map<Customer, List<Order>> ordiniPerCliente = orders.stream().collect(Collectors.groupingBy(order -> order.getCustomer()));
        ordiniPerCliente.forEach((customer, orderList) -> {

        //            System.out.println("CLiente" + customer.getName());
        //            orderList.forEach(order -> System.out.println(order));

            // ordine cliente piú dettagliato:
            System.out.println("CLiente" + customer.getName());
            orderList.forEach(order -> System.out.println(" ordine ID: " + order.getId() + " Data ordine: " + order.getOrderDate()));

        });


        System.out.println("------------------------------ESERCIZIO N'2-------------------------------------");
        System.out.println(" ");
        System.out.println("Calcolare il totale delle vendite per ogni cliente: ");
        Map<Customer, Double> totaleVenditePerCliente = orders.stream().collect(Collectors.groupingBy(Order::getCustomer,
                Collectors.summingDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum())));
                totaleVenditePerCliente.forEach((customer, total) -> {
            System.out.println("Cliente: " + customer.getName() + ", Totale Vendite: " + total);
        });


        System.out.println("------------------------------ESERCIZIO N'3-------------------------------------");
        System.out.println(" ");
        System.out.println("trova i prodotti piú costosi: ");
        OptionalDouble prodottiCostosi = products.stream().mapToDouble(Product::getPrice).max();
        if (prodottiCostosi.isPresent()) {
            double maxPrice = prodottiCostosi.getAsDouble();
            List<Product> prodottiPiuCostosi = products.stream().filter(product -> product.getPrice() == maxPrice).toList();
            prodottiPiuCostosi.forEach(System.out::println);} else {
            System.out.println("Nessun prodotto trovato");
            }


        System.out.println("------------------------------ESERCIZIO N'4-------------------------------------");
        System.out.println(" ");
        System.out.println("Calcola la media degli importi degli ordini: ");
        OptionalDouble mediaImportiOrdini = orders.stream().mapToDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum()).average();
        if (mediaImportiOrdini.isPresent()) System.out.println("Media degli importi degli ordini: " + mediaImportiOrdini.getAsDouble());
        else System.out.println("Nessun ordine trovato");


        System.out.println("------------------------------ESERCIZIO N'5-------------------------------------");
        System.out.println(" ");
        System.out.println("Raggruppa i prodotti per categoria e calcola la somma degli importi per ogni categoria: ");
        Map<String, Double> importiPerCategoria = products.stream().collect(Collectors.groupingBy(product -> product.category, Collectors.summingDouble(product -> product.price)));
        // System.out.println(importiPerCategoria);
        importiPerCategoria.forEach((categoria, somma) -> {
            System.out.println("Categoria: " + categoria + " | Somma degli importi: " + somma);
        });


    }
}
