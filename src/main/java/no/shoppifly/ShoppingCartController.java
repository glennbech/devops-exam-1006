package no.shoppifly;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
public class ShoppingCartController implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private final CartService cartService;

    private Map<String, Cart> theCart = new HashMap();
    private Map<String, Cart> theCartForUpdate = new HashMap();
    private MeterRegistry meterRegistry;


    @Autowired
    public ShoppingCartController(CartService cartService, MeterRegistry meterRegistry) {
        this.cartService = cartService;
        this.meterRegistry = meterRegistry;
    }


    @GetMapping(path = "/cart/{id}")
    public Cart getCart(@PathVariable String id) {
        meterRegistry.counter("carts").increment();
        return cartService.getCart(id);
    }

    /**
     * Checks out a shopping cart. Removes the cart, and returns an order ID
     *
     * @return an order ID
     */
    @Timed("checkout_latency")
    @PostMapping(path = "/cart/checkout")
    public String checkout(@RequestBody Cart cart) {
        meterRegistry.counter("remove_cart").increment();
        String temp = cartService.checkout(cart);
        theCart.put(cart.getId(), cart);
        theCartForUpdate.remove(cart.getId());
        return temp;
    }

    /**
     * Updates a shopping cart, replacing it's contents if it already exists. If no cart exists (id is null)
     * a new cart is created.
     *
     * @return the updated cart
     */

    @PostMapping(path = "/cart")
    public Cart updateCart(@RequestBody Cart cart) {
        meterRegistry.counter("update_cart").increment();
        theCartForUpdate.put(cart.getId(), cart);
        return cartService.update(cart);
    }

    /**
     * return all cart IDs
     *
     * @return
     */
    @GetMapping(path = "/carts")
    public List<String> getAllCarts() {
        return cartService.getAllsCarts();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        // Denne meter-typen "Gauge" rapporterer hvor mye penger som totalt finnes i banken
        Gauge.builder("checkouts" , theCart,
                b -> b.values().size()).register(meterRegistry);

        Gauge.builder("carts" , theCartForUpdate,
                        b -> b.values().size()).register(meterRegistry);

        Gauge.builder("cartsvalue" , cartService,
                b -> b.total()).register(meterRegistry);


    }




}