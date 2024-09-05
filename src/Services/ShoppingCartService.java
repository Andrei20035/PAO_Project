package Services;

import Models.CartItem;
import Models.Product;
import Models.ShoppingCart;
import Repositories.ProductRepo;
import Repositories.ShoppingCartRepo;

import java.util.Scanner;

public class ShoppingCartService {

    public static void addProductToCart(Scanner scanner, ShoppingCartRepo shoppingCartRepository, ProductRepo productRepository) {
        System.out.print("ID Cos: ");
        int cartId = scanner.nextInt();
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartById(cartId);
        if (shoppingCart != null) {
            System.out.print("ID Produs: ");
            int productId = scanner.nextInt();
            Product product = productRepository.getProductById(productId);
            if (product != null) {
                System.out.print("Cantitate: ");
                int quantity = scanner.nextInt();
                CartItem cartItem = new CartItem(product, quantity);
                shoppingCart.addCartItem(cartItem);
                shoppingCartRepository.updateShoppingCart(shoppingCart);
                AuditService.logAction("Adauga Produs la Cos");
                System.out.println("Produsul a fost adaugat la cos.");
            } else {
                System.out.println("Produsul cu ID-ul " + productId + " nu exista.");
            }
        } else {
            System.out.println("Cosul cu ID-ul " + cartId + " nu exista.");
        }
    }
}
