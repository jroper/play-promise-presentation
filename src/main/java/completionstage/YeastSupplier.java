package completionstage;

import model.Yeast;

import java.util.concurrent.CompletionStage;

public interface YeastSupplier {
    /**
     * Order some yeast.
     *
     * @return A promise to deliver some yeast.
     */
    CompletionStage<Yeast> orderYeast();
}
