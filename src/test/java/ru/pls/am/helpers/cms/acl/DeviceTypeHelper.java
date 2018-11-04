package ru.pls.am.helpers.cms.acl;

import ru.pls.am.helpers.AbstractHelper;

import java.util.LinkedList;
import java.util.List;

public class DeviceTypeHelper extends AbstractHelper {

    public void createAclDeviceType(int userId, String name, String id) {
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(name);                                                                                               // "name"
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.acl_devicetype(" +
                (id != null ? "id, " : "") +
                "created, modified, \"name\", created_by_id, modified_by_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.acl_devicetype", "\"name\"", name);
        System.out.println("Создали тип устройства " + name);
    }
}