package xmlcontext;

import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.strategy.StrategyWrapper;

import java.io.Serializable;
import java.util.UUID;

public class TestStrategy implements IDStrategy {
    private String pm;

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
        System.out.println(pm);
    }

    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
