package async;

import model.Flour;
import play.libs.F.*;

public class LocalMiller implements Miller {

    public Promise<Flour> orderFlour() {
        Promise<Flour> flourOrder;

        // Check if flour is in stock
        if (hasFlourInStock()) {

            // Create a pure (immediate) promise for stocked flour
            flourOrder = Promise.pure(getStockedFlour());

        } else {

            // Order the flour to be milled
            flourOrder = orderFlourToBeMilled();
        }

        // Return the promise
        return flourOrder;
    }



































    private Flour getStockedFlour() {
        return null;
    }

    private boolean hasFlourInStock() {
        return true;
    }

    private Promise<Flour> orderFlourToBeMilled() {
        return null;
    }
}
