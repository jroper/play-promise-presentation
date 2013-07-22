package completionstage;

import model.Bread;
import model.Dough;

import java.util.concurrent.CompletionStage;

public interface Oven {
    /**
     * Put some dough in the oven
     *
     * @param dough The dough to bake
     * @return A promise to produce bread
     */
    CompletionStage<Bread> bakeDough(Dough dough);
}
