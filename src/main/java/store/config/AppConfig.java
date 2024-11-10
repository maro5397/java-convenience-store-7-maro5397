package store.config;

import store.controller.PurchaseController;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.PurchaseService;
import store.service.impl.PurchaseServiceImpl;
import store.view.InputView;
import store.view.OutputView;
import store.view.impl.ConsoleInputViewImpl;
import store.view.impl.ConsoleOutputViewImpl;

public class AppConfig {
    private static AppConfig INSTANCE;

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final InputView inputView;
    private final OutputView outputView;
    private final PurchaseService purchaseService;
    private final PurchaseController purchaseController;

    public AppConfig() {
        this.productRepository = new ProductRepository("src/main/resources/products.md");
        this.promotionRepository = new PromotionRepository("src/main/resources/promotions.md");
        this.inputView = new ConsoleInputViewImpl();
        this.outputView = new ConsoleOutputViewImpl();
        this.purchaseService = new PurchaseServiceImpl(productRepository, promotionRepository);
        this.purchaseController = new PurchaseController(this.purchaseService, this.inputView, this.outputView);
    }

    public PurchaseController getPurchaseController() {
        return purchaseController;
    }
}
