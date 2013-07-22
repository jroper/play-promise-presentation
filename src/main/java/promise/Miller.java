package promise;

import model.Flour;
import play.libs.F.*;

public interface Miller {
    /**
     * Order some flour
     *
     * @return A promise to deliver some flour
     */
    public Promise<Flour> orderFlour();
}
