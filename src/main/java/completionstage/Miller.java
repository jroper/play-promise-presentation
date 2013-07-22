package completionstage;

import model.Flour;
import java.util.concurrent.CompletionStage;

public interface Miller {
    /**
     * Order some flour
     *
     * @return A promise to deliver some flour
     */
    public CompletionStage<Flour> orderFlour();
}
