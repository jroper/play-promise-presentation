package promise;

import model.*;
import play.libs.F.*;

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
     * Order some sliced bread from the baker
     *
     * @return The sliced bread
     */
    public Promise<SlicedBread> orderSlicedBread() {

        // Order flour
        return miller.orderFlour()
                // Create dough when the order is fulfilled
                .map(Dough::new)
                // Once the dough is made, place in the oven
                .flatMap(oven::bakeDough)
                // Slice it once it is baked
                .map(Bread::slice);
    }
}
