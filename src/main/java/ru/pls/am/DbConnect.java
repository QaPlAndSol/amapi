package ru.pls.am;

import org.jdbi.v3.core.Jdbi;
import ru.pls.am.data.service.DbInfo;

import java.io.IOException;

public class DbConnect {

    private Jdbi jdbi;

    public DbConnect(String serviceName) throws IOException {
        DbInfo db = new Util().loadServicesInfo().getService(serviceName).getDb();
        String url = "jdbc:" + db.getDriver() + "://" + db.getUrl() + ":" + db.getPort() + "/" + db.getDbName();
        jdbi = Jdbi.create(url, db.getDbUser(), db.getDbPassword());
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}
