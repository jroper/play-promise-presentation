package completionstage;

import model.Flour;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LocalMiller implements Miller {

    public CompletionStage<Flour> orderFlour() {
        CompletionStage<Flour> flourOrder;

        // Check if flour is in stock
        if (hasFlourInStock()) {

            // Create a pure (immediate) promise for stocked flour
            flourOrder = CompletableFuture.completedFuture(getStockedFlour());

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

    private CompletionStage<Flour> orderFlourToBeMilled() {
        return null;
    }
}
