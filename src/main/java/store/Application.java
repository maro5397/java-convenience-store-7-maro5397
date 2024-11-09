package store;

import store.controller.PurchaseController;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.PurchaseService;
import store.service.impl.PurchaseServiceImpl;
import store.view.InputView;
import store.view.OutputView;
import store.view.impl.ConsoleInputViewImpl;
import store.view.impl.ConsoleOutputViewImpl;

public class Application {
    public static void main(String[] args) {
        PromotionRepository promotionRepository = new PromotionRepository("src/main/resources/promotions.md");
        ProductRepository productRepository = new ProductRepository("src/main/resources/products.md");
        PurchaseService purchaseService = new PurchaseServiceImpl(productRepository, promotionRepository);
        InputView inputView = new ConsoleInputViewImpl();
        OutputView outputView = new ConsoleOutputViewImpl();
        PurchaseController purchaseController = new PurchaseController(purchaseService, inputView, outputView);
        purchaseController.start();
    }
}
