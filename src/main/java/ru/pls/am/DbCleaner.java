package ru.pls.am;

import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DbCleaner {

    private static HashMap<String, List<DbEntity>> servicesAndEntities = new HashMap<>();

    public void cleanDb() {
        for (String service : servicesAndEntities.keySet()) {
            List<DbEntity> dbEntities = servicesAndEntities.get(service);
            try {
                Jdbi jdbi = new DbConnect(service).getJdbi();
                Collections.reverse(dbEntities);
                StringBuilder allSql = new StringBuilder();
                List<Object> allValues = new LinkedList<>();
                for (DbEntity dbEntity : dbEntities) {
                    StringBuilder sql = new StringBuilder("DELETE FROM " + dbEntity.table + " WHERE ");
                    List<Object> values = new LinkedList<>();
                    for (String column : dbEntity.pairs.keySet()) {
                        sql.append(column).append(dbEntity.like ? " like ? AND " : "=? AND ");
                        values.add(dbEntity.pairs.get(column));
                    }
                    sql.reverse().replace(0, 5, "").reverse().append(";");
                    allSql.append(sql);
                    allValues.addAll(values);
                }
                jdbi.useHandle(handle -> handle.execute(allSql.toString(), allValues.toArray()));
                servicesAndEntities.remove(service);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addEntity(String service, String table, HashMap<String, Object> pairs) {
        addEntity(service, table, pairs, false);
    }

    public void addEntity(String service, String table, String column, Object value) {
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put(column, value);
        addEntity(service, table, pairs, false);
    }

    public void addLikeEntity(String service, String table, HashMap<String, Object> pairs) {
        addEntity(service, table, pairs, true);
    }

    public void addLikeEntity(String service, String table, String column, Object value) {
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put(column, value);
        addEntity(service, table, pairs, true);
    }

    private void addEntity(String service, String table, HashMap<String, Object> pairs, boolean like) {
        servicesAndEntities.computeIfAbsent(service, k -> new LinkedList<>());
        servicesAndEntities.get(service).add(new DbEntity(table, pairs, like));
    }

    private class DbEntity {

        private String table;
        private HashMap<String, Object> pairs;
        private boolean like;

        DbEntity(String table, HashMap<String, Object> pairs, boolean like) {
            this.table = table;
            this.pairs = pairs;
            this.like = like;
        }
    }
}
