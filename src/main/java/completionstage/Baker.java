package completionstage;

import model.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class Baker {
    private final Miller miller;
    private final Oven oven;
    private final YeastSupplier yeastSupplier;
    private Flour emergencyFlour;

    public Baker(Miller miller, Oven oven, YeastSupplier yeastSupplier) {
        this.miller = miller;
        this.oven = oven;
        this.yeastSupplier = yeastSupplier;
    }

    /**
     * Get some sliced bread from the baker and wait for it
     *
     * @return The sliced bread
     */
    public SlicedBread getSlicedBread() throws Exception {

        // Order flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Wait for order to be fulfilled
        Flour flour = flourOrder.toCompletableFuture().get();

        // Create dough
        Dough dough = new Dough(flour);

        // Put in oven
        CompletionStage<Bread> breadInOven = oven.bakeDough(dough);

        // Wait for it to finish backing
        Bread bread = breadInOven.toCompletableFuture().get();

        // Slice it
        SlicedBread slicedBread = bread.slice();

        // Give it to the customer
        return slicedBread;
    }

    /**
     * Order some sliced bread from the baker
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadComplex() {

        // Order flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Create dough when the order is fulfilled
        CompletionStage<Dough> dough = flourOrder.thenApply(new Function<Flour, Dough>() {
            public Dough apply(Flour flour) {
                return new Dough(flour);
            }
        });

        // Once the dough is made, place in the oven
        CompletionStage<Bread> bread = dough.thenCompose(new Function<Dough, CompletionStage<Bread>>() {
            @Override
            public CompletionStage<Bread> apply(Dough dough) {
                return oven.bakeDough(dough);
            }
        });

        // Slice it once it is baked
        CompletionStage<SlicedBread> slicedBreadOrder = bread.thenApply(new Function<Bread, SlicedBread>() {
            @Override
            public SlicedBread apply(Bread bread) {
                return bread.slice();
            }
        });

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadSimpler() {

        // Order flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Create dough when the order is fulfilled
        CompletionStage<Dough> dough = flourOrder.thenApply(flour -> new Dough(flour));

        // Once the dough is made, place in the oven
        CompletionStage<Bread> bread = dough.thenCompose(d -> oven.bakeDough(d));

        // Slice it once it is baked
        CompletionStage<SlicedBread> slicedBreadOrder = bread.thenApply(b -> b.slice());

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadSimplest() {

        // Order flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Create dough when the order is fulfilled
        CompletionStage<Dough> dough = flourOrder.thenApply(Dough::new);

        // Once the dough is made, place in the oven
        CompletionStage<Bread> bread = dough.thenCompose(oven::bakeDough);

        // Slice it once it is baked
        CompletionStage<SlicedBread> slicedBreadOrder = bread.thenApply(Bread::slice);

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadFluent() {

        // Order flour
        return miller.orderFlour()
                // Create dough when the order is fulfilled
                .thenApply(Dough::new)
                // Once the dough is made, place in the oven
                .thenCompose(oven::bakeDough)
                // Slice it once it is baked
                .thenApply(Bread::slice);
    }


    /**
     * Order some sliced bread from the baker, guaranteeing that it will be delivered, even when the Miller has no flour
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadGuaranteed() {

        // Order flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Handle the case where there's a problem in delivering the flour
        CompletionStage<Flour> guaranteedFlour =
                flourOrder.exceptionally(exception -> emergencyFlour);

        // Create dough when the order is fulfilled
        CompletionStage<Dough> dough = guaranteedFlour.thenApply(Dough::new);

        // Once the dough is made, place in the oven
        CompletionStage<Bread> bread = dough.thenCompose(oven::bakeDough);

        // Slice it once it is baked
        CompletionStage<SlicedBread> slicedBreadOrder = bread.thenApply(Bread::slice);

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker, guaranteeing that it will be delivered, with both fresh flour and fresh
     * yeast.
     *
     * @return The sliced bread
     */
    public CompletionStage<SlicedBread> orderSlicedBreadWithFreshYeast() {

        // Place order for flour
        CompletionStage<Flour> flourOrder = miller.orderFlour();

        // Place order for yeast
        CompletionStage<Yeast> yeastOrder = yeastSupplier.orderYeast();

        // Combine ingredients into single promise
        CompletionStage<List<Ingredient>> ingredientsOrder = flourOrder
                .thenCombine(yeastOrder, (flour, yeast) -> Arrays.asList(flour, yeast));

        // Create dough when the order is fulfilled
        CompletionStage<Dough> dough = ingredientsOrder.thenApply(Dough::new);

        // Once the dough is made, place in the oven
        CompletionStage<Bread> bread = dough.thenCompose(oven::bakeDough);

        // Slice it once it is baked
        CompletionStage<SlicedBread> slicedBreadOrder = bread.thenApply(Bread::slice);

        return slicedBreadOrder;
    }

}
