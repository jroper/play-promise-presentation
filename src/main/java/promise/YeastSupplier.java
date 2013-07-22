package promise;

import model.Yeast;
import play.libs.F.*;

public interface YeastSupplier {
    Promise<Yeast> orderYeast();
}
