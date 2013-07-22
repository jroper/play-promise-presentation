package promise;

import model.Bread;
import model.Dough;
import play.libs.F.*;

public interface Oven {
    /**
     * Put some dough in the oven
     *
     * @param dough The dough to bake
     * @return A promise to produce bread
     */
    Promise<Bread> bakeDough(Dough dough);
}
