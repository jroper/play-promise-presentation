package async;

import model.*;
import play.libs.F.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public Baker() {
        this(null, null, null);
    }

    /**
     * Get some sliced bread from the baker and wait for it
     *
     * @return The sliced bread
     */
    public SlicedBread getSlicedBread() {

        // Place order
        Promise<Flour> flourOrder = miller.orderFlour();

        // Wait for order to be fulfilled
        Flour flour = flourOrder.get();

        // Create dough
        Dough dough = new Dough(flour);

        // Put in oven
        Promise<Bread> breadInOven = oven.bakeDough(dough);

        // Wait for it to finish backing
        Bread bread = breadInOven.get();

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
    public Promise<SlicedBread> orderSlicedBread() {

        // Place order
        Promise<Flour> flourOrder = miller.orderFlour();

        // Create dough when the order is fulfilled
        Promise<Dough> dough = flourOrder.map(new Function<Flour, Dough>() {
            public Dough apply(Flour flour) {
                return new Dough(flour);
            }
        });

        // Once the dough is made, place in the oven
        Promise<Bread> bread = dough.flatMap(new Function<Dough, Promise<Bread>>() {
            public Promise<Bread> apply(Dough dough) {
                return oven.bakeDough(dough);
            }
        });

        // Slice it once it is baked
        Promise<SlicedBread> slicedBreadOrder = bread.map(new Function<Bread, SlicedBread>() {
            @Override
            public SlicedBread apply(Bread bread) throws Throwable {
                return bread.slice();
            }
        });

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker, guaranteeing that it will be delivered, even when the Miller has no flour
     *
     * @return The sliced bread
     */
    public Promise<SlicedBread> orderSlicedBreadGuaranteed() {

        // Place order
        Promise<Flour> flourOrder = miller.orderFlour();

        // Handle the case where there's a problem in delivering the flour
        flourOrder.recover(new Function<Throwable, Flour>() {
            public Flour apply(Throwable throwable) throws Throwable {
                return emergencyFlour;
            }
        });

        // Create dough when the order is fulfilled
        Promise<Dough> dough = flourOrder.map(new Function<Flour, Dough>() {
            public Dough apply(Flour flour) {
                return new Dough(flour);
            }
        });

        // Once the dough is made, place in the oven
        Promise<Bread> bread = dough.flatMap(new Function<Dough, Promise<Bread>>() {
            public Promise<Bread> apply(Dough dough) {
                return oven.bakeDough(dough);
            }
        });

        // Slice it once it is baked
        Promise<SlicedBread> slicedBreadOrder = bread.map(new Function<Bread, SlicedBread>() {
            @Override
            public SlicedBread apply(Bread bread) throws Throwable {
                return bread.slice();
            }
        });

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker, guaranteeing that it will be delivered, even when the Miller has no flour
     *
     * @return The sliced bread
     */
    public Promise<SlicedBread> orderSlicedBreadWithFreshYeast() {

        // Place order for flour
        Promise<Flour> flourOrder = miller.orderFlour();

        // Order yeast once flour comes
        Promise<Collection<Ingredient>> ingredientsOrder = flourOrder.flatMap(new Function<Flour, Promise<Collection<Ingredient>>>() {
            public Promise<Collection<Ingredient>> apply(final Flour flour) throws Throwable {
                return yeastSupplier.orderYeast().map(new Function<Yeast, Collection<Ingredient>>() {
                    public Collection<Ingredient> apply(Yeast yeast) throws Throwable {
                        return Arrays.asList(flour, yeast);
                    }
                });
            }
        });

        // Create dough when the order is fulfilled
        Promise<Dough> dough = flourOrder.map(new Function<Flour, Dough>() {
            public Dough apply(Flour flour) {
                return new Dough(flour);
            }
        });

        // Once the dough is made, place in the oven
        Promise<Bread> bread = dough.flatMap(new Function<Dough, Promise<Bread>>() {
            public Promise<Bread> apply(Dough dough) {
                return oven.bakeDough(dough);
            }
        });

        // Slice it once it is baked
        Promise<SlicedBread> slicedBreadOrder = bread.map(new Function<Bread, SlicedBread>() {
            @Override
            public SlicedBread apply(Bread bread) throws Throwable {
                return bread.slice();
            }
        });

        return slicedBreadOrder;
    }

    /**
     * Order some sliced bread from the baker, guaranteeing that it will be delivered, even when the Miller has no flour
     *
     * @return The sliced bread
     */
    public Promise<SlicedBread> orderSlicedBreadWithFreshYeastInParallel() {

        // Place order for flour
        Promise<Flour> flourOrder = miller.orderFlour();

        // Place order for yeast
        Promise<Yeast> yeastOrder = yeastSupplier.orderYeast();

        // Combine them into one promise
        Promise<List<Ingredient>> ingredientsOrder = Promise.waitAll(flourOrder, yeastOrder);

        // Create dough when the order is fulfilled
        Promise<Dough> dough = ingredientsOrder.map(new Function<List<Ingredient>, Dough>() {
            public Dough apply(List<Ingredient> ingredients) {
                return new Dough(ingredients);
            }
        });

        // Once the dough is made, place in the oven
        Promise<Bread> bread = dough.flatMap(new Function<Dough, Promise<Bread>>() {
            public Promise<Bread> apply(Dough dough) {
                return oven.bakeDough(dough);
            }
        });

        // Slice it once it is baked
        Promise<SlicedBread> slicedBreadOrder = bread.map(new Function<Bread, SlicedBread>() {
            @Override
            public SlicedBread apply(Bread bread) throws Throwable {
                return bread.slice();
            }
        });

        return slicedBreadOrder;
    }

}
