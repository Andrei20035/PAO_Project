package Services;

import Models.*;
import Repositories.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static Services.ShoppingCartService.addProductToCart;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao", "Andrei", "Andrei2003.");
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("===== Meniu Principal =====");
                System.out.println("1. Gestionare Utilizatori");
                System.out.println("2. Gestionare Produse");
                System.out.println("3. Gestionare Cosuri de Cumparaturi");
                System.out.println("4. Gestionare Comenzi");
                System.out.println("5. Gestionare Recenzii");
                System.out.println("6. Iesire");
                System.out.print("Alege o optiune: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        manageUsers(scanner);
                        break;
                    case 2:
                        manageProducts(scanner);
                        break;
                    case 3:
                        manageShoppingCarts(scanner);
                        break;
                    case 4:
                        manageOrders(scanner);
                        break;
                    case 5:
                        manageReviews(scanner);
                        break;
                    case 6:
                        System.out.println("Iesire...");
                        break;
                    default:
                        System.out.println("Optiune invalida. Te rog sa incerci din nou.");
                }
            } while (choice != 6);

            scanner.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Driverul MySQL nu a fost gasit.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void manageUsers(Scanner scanner) {
        UserRepo userRepository = new UserRepo(connection);
        int choice;

        do {
            System.out.println("===== Gestionare Utilizatori =====");
            System.out.println("1. Adauga Utilizator");
            System.out.println("2. Afiseaza Utilizatori");
            System.out.println("3. Actualizeaza Utilizator");
            System.out.println("4. Sterge Utilizator");
            System.out.println("5. Inapoi la Meniul Principal");
            System.out.print("Alege o optiune: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("1. Utilizator Premium");
                    System.out.println("2. Utilizator Normal");
                    System.out.print("Alege tipul de utilizator: ");
                    int userType = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nume: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Parolă: ");
                    String password = scanner.nextLine();
                    System.out.print("Adresă: ");
                    String address = scanner.nextLine();

                    if (userType == 1) {
                        double discountRate;
                        while (true) {
                            System.out.print("Rata de discount (intre 1% si 99%): ");
                            discountRate = scanner.nextDouble();
                            if (discountRate >= 1 && discountRate <= 99) {
                                break;
                            } else {
                                System.out.println("Rata de discount trebuie să fie între 1% si 99%. Te rog sa incerci din nou.");
                            }
                        }
                        PremiumUser premiumUser = new PremiumUser(name, email, password, address, discountRate);
                        userRepository.createPremiumUser(premiumUser);
                        AuditService.logAction("Adauga Utilizator Premium");
                    } else if (userType == 2) {
                        User user = new User(name, email, password, address);
                        userRepository.createUser(user);
                        AuditService.logAction("Adauga Utilizator Normal");
                    } else {
                        System.out.println("Tip de utilizator invalid. Te rog să incerci din nou.");
                    }
                    break;
                case 2:
                    for (User u : userRepository.getAllUsers()) {
                        System.out.println(u);
                    }
                    AuditService.logAction("Afiseaza Utilizatori");
                    break;
                case 3:
                    System.out.print("ID Utilizator: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nume: ");
                    name = scanner.nextLine();
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    System.out.print("Parola: ");
                    password = scanner.nextLine();
                    System.out.print("Adresa: ");
                    address = scanner.nextLine();
                    System.out.print("Rata de discount (0 daca nu este premium): ");
                    double discountRate = scanner.nextDouble();
                    if (discountRate > 0) {
                        PremiumUser premiumUser = new PremiumUser(userId, name, email, password, address, discountRate);
                        userRepository.updatePremiumUser(premiumUser);
                    } else {
                        User user = new User(userId, name, email, password, address);
                        userRepository.updateUser(user);
                    }
                    AuditService.logAction("Actualizeaza Utilizator");
                    break;
                case 4:
                    System.out.print("ID Utilizator: ");
                    userId = scanner.nextInt();
                    userRepository.deleteUser(userId);
                    AuditService.logAction("Sterge Utilizator");
                    break;
                case 5:
                    System.out.println("Inapoi la Meniul Principal...");
                    break;
                default:
                    System.out.println("Optiune invalida. Te rog sa incerci din nou.");
            }
        } while (choice != 5);
    }




    private static void manageProducts(Scanner scanner) {
        ProductRepo productRepository = new ProductRepo(connection);
        int choice;

        do {
            System.out.println("===== Gestionare Produse =====");
            System.out.println("1. Adauga Produs");
            System.out.println("2. Afișeaza Produse");
            System.out.println("3. Actualizeaza Produs");
            System.out.println("4. Sterge Produs");
            System.out.println("5. Inapoi la Meniul Principal");
            System.out.print("Alege o optiune: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Nume: ");
                    String name = scanner.nextLine();
                    System.out.print("Descriere: ");
                    String description = scanner.nextLine();
                    double price = 0;
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.print("Pret: ");
                        if (scanner.hasNextDouble()) {
                            price = scanner.nextDouble();
                            validInput = true;
                        } else {
                            System.out.println("Te rog sa introduci un numar valid pentru pret.");
                            scanner.next();
                        }
                    }
                    System.out.print("Cantitate in stoc: ");
                    int stockQuantity = scanner.nextInt();
                    Product product = new Product(name, description, price, stockQuantity);
                    productRepository.createProduct(product);
                    AuditService.logAction("Adauga Produs");
                    break;
                case 2:
                    for (Product p : productRepository.getAllProducts()) {
                        System.out.println(p);
                    }
                    AuditService.logAction("Afiseaza Produse");
                    break;
                case 3:
                    System.out.print("ID Produs: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nume: ");
                    name = scanner.nextLine();
                    System.out.print("Descriere: ");
                    description = scanner.nextLine();
                    price = 0;
                    validInput = false;
                    while (!validInput) {
                        System.out.print("Pret: ");
                        if (scanner.hasNextDouble()) {
                            price = scanner.nextDouble();
                            validInput = true;
                        } else {
                            System.out.println("Te rog sa introduci un numar valid pentru pret.");
                            scanner.next();
                        }
                    }
                    System.out.print("Cantitate in stoc: ");
                    stockQuantity = scanner.nextInt();
                    product = new Product(productId, name, description, price, stockQuantity);
                    productRepository.updateProduct(product);
                    AuditService.logAction("Actualizeaza Produs");
                    break;
                case 4:
                    System.out.print("ID Produs: ");
                    productId = scanner.nextInt();
                    productRepository.deleteProduct(productId);
                    AuditService.logAction("Sterge Produs");
                    break;
                case 5:
                    System.out.println("Inapoi la Meniul Principal...");
                    break;
                default:
                    System.out.println("Optiune invalida. Te rog sa incerci din nou.");
            }
        } while (choice != 5);
    }



    private static void manageShoppingCarts(Scanner scanner) {
        ShoppingCartRepo shoppingCartRepository = new ShoppingCartRepo(connection);
        ProductRepo productRepository = new ProductRepo(connection);
        int choice;

        do {
            System.out.println("===== Gestionare Cosuri de Cumparaturi =====");
            System.out.println("1. Adauga Cos de Cumparaturi");
            System.out.println("2. Afiseaza Cosuri de Cumparaturi");
            System.out.println("3. Actualizeaza Cos de Cumparaturi");
            System.out.println("4. Sterge Cos de Cumparaturi");
            System.out.println("5. Adauga Produs la Cos");
            System.out.println("6. Inapoi la Meniul Principal");
            System.out.print("Alege o optiune: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("ID Utilizator: ");
                    int userId = scanner.nextInt();
                    System.out.print("Suma Totala: ");
                    double totalAmount = scanner.nextDouble();
                    User user = new UserRepo(connection).getUserById(userId);
                    if (user != null) {
                        ShoppingCart shoppingCart = new ShoppingCart(user, totalAmount);
                        shoppingCartRepository.createShoppingCart(shoppingCart);
                        AuditService.logAction("Adauga Cos de Cumparaturi");
                    } else {
                        System.out.println("Utilizatorul cu ID-ul " + userId + " nu exista.");
                    }
                    break;
                case 2:
                    for (ShoppingCart sc : shoppingCartRepository.getAllShoppingCarts()) {
                        System.out.println(sc);
                    }
                    AuditService.logAction("Afiseaza Cosuri de Cumparaturi");
                    break;
                case 3:
                    System.out.print("ID Cos: ");
                    int cartId = scanner.nextInt();
                    System.out.print("ID Utilizator: ");
                    userId = scanner.nextInt();
                    System.out.print("Suma Totala: ");
                    totalAmount = scanner.nextDouble();
                    user = new UserRepo(connection).getUserById(userId);
                    if (user != null) {
                        ShoppingCart shoppingCart = new ShoppingCart(cartId, user, totalAmount);
                        shoppingCartRepository.updateShoppingCart(shoppingCart);
                        AuditService.logAction("Actualizeaza Cos de Cumparaturi");
                    } else {
                        System.out.println("Utilizatorul cu ID-ul " + userId + " nu exista.");
                    }
                    break;
                case 4:
                    System.out.print("ID Cos: ");
                    cartId = scanner.nextInt();
                    shoppingCartRepository.deleteShoppingCart(cartId);
                    AuditService.logAction("Sterge Cos de Cumparaturi");
                    break;
                case 5:
                    addProductToCart(scanner, shoppingCartRepository, productRepository);
                    break;
                case 6:
                    System.out.println("Inapoi la Meniul Principal...");
                    break;
                default:
                    System.out.println("Optiune invalida. Te rog sa incerci din nou.");
            }
        } while (choice != 6);
    }


    private static void manageOrders(Scanner scanner) {
        OrderRepo orderRepository = new OrderRepo(connection);
        int choice;

        do {
            System.out.println("===== Gestionare Comenzi =====");
            System.out.println("1. Adauga Comanda");
            System.out.println("2. Afiseaza Comenzi");
            System.out.println("3. Actualizeaza Comanda");
            System.out.println("4. Sterge Comanda");
            System.out.println("5. Inapoi la Meniul Principal");
            System.out.print("Alege o optiune: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("ID Utilizator: ");
                    int userId = scanner.nextInt();
                    System.out.print("Suma Totala: ");
                    double totalAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Status: ");
                    String status = scanner.nextLine();
                    User user = new UserRepo(connection).getUserById(userId);
                    if (user != null) {
                        Order order = new Order(user, totalAmount, status);
                        orderRepository.createOrder(order);
                        AuditService.logAction("Adauga Comanda");
                    } else {
                        System.out.println("Utilizatorul cu ID-ul " + userId + " nu exista.");
                    }
                    break;
                case 2:
                    for (Order o : orderRepository.getAllOrders()) {
                        System.out.println(o);
                    }
                    AuditService.logAction("Afiseaza Comenzi");
                    break;
                case 3:
                    System.out.print("ID Comanda: ");
                    int orderId = scanner.nextInt();
                    System.out.print("ID Utilizator: ");
                    userId = scanner.nextInt();
                    System.out.print("Suma Totala: ");
                    totalAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Status: ");
                    status = scanner.nextLine();
                    user = new UserRepo(connection).getUserById(userId);
                    if (user != null) {
                        Order order = new Order(orderId, user, totalAmount, status);
                        orderRepository.updateOrder(order);
                        AuditService.logAction("Actualizeaza Comanda");
                    } else {
                        System.out.println("Utilizatorul cu ID-ul " + userId + " nu exista.");
                    }
                    break;
                case 4:
                    System.out.print("ID Comanda: ");
                    orderId = scanner.nextInt();
                    orderRepository.deleteOrder(orderId);
                    AuditService.logAction("Sterge Comanda");
                    break;
                case 5:
                    System.out.println("Inapoi la Meniul Principal...");
                    break;
                default:
                    System.out.println("Optiune invalida. Te rog sa incerci din nou.");
            }
        } while (choice != 5);
    }


    private static void manageReviews(Scanner scanner) {
        ReviewRepo reviewRepository = new ReviewRepo(connection);
        int choice = 0;

        do {
            System.out.println("===== Gestionare Recenzii =====");
            System.out.println("1. Adauga Recenzie");
            System.out.println("2. Afiseaza Recenzii");
            System.out.println("3. Actualizeaza Recenzie");
            System.out.println("4. Sterge Recenzie");
            System.out.println("5. Inapoi la Meniul Principal");
            System.out.print("Alege o optiune: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("ID Utilizator: ");
                    int userId = scanner.nextInt();
                    System.out.print("ID Produs: ");
                    int productId = scanner.nextInt();

                    int rating;
                    while (true) {
                        System.out.print("Rating (intre 1 si 5): ");
                        rating = scanner.nextInt();
                        if (rating >= 1 && rating <= 5) {
                            break;
                        } else {
                            System.out.println("Ratingul trebuie sa fie intre 1 si 5. Te rog sa incerci din nou.");
                        }
                    }

                    scanner.nextLine();
                    System.out.print("Comentariu: ");
                    String comment = scanner.nextLine();
                    User user = new UserRepo(connection).getUserById(userId);
                    Product product = new ProductRepo(connection).getProductById(productId);
                    if (user != null && product != null) {
                        Review review = new Review(user, product, rating, comment);
                        reviewRepository.createReview(review);
                        AuditService.logAction("Adaugă Recenzie");
                    } else {
                        System.out.println("Utilizatorul sau produsul nu exista.");
                    }
                    break;

                case 2:
                    for (Review r : reviewRepository.getAllReviews()) {
                        System.out.println(r);
                    }
                    AuditService.logAction("Afiseaza Recenzii");
                    break;
                case 3:
                    System.out.print("ID Recenzie: ");
                    int reviewId = scanner.nextInt();
                    System.out.print("ID Utilizator: ");
                    userId = scanner.nextInt();
                    System.out.print("ID Produs: ");
                    productId = scanner.nextInt();
                    System.out.print("Rating: ");
                    rating = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Comentariu: ");
                    comment = scanner.nextLine();
                    user = new UserRepo(connection).getUserById(userId);
                    product = new ProductRepo(connection).getProductById(productId);
                    if (user != null && product != null) {
                        Review review = new Review(reviewId, user, product, rating, comment);
                        reviewRepository.updateReview(review);
                        AuditService.logAction("Actualizeaza Recenzie");
                    } else {
                        System.out.println("Utilizatorul sau produsul nu exista.");
                    }
                    break;
                case 4:
                    System.out.print("ID Recenzie: ");
                    reviewId = scanner.nextInt();
                    reviewRepository.deleteReview(reviewId);
                    AuditService.logAction("Sterge Recenzie");
                    break;
                case 5:
                    System.out.println("Inapoi la Meniul Principal...");
                    break;
                default:
                    System.out.println("Optiune invalida. Te rog sa incerci din nou.");
            }
        } while (choice != 5);
    }
}
