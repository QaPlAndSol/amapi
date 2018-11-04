package ru.pls.am.stepdefs.cms.acl;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.acl.DeviceTypeHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class DeviceTypeStepDefs extends AbstractStepDefs {

    private DeviceTypeHelper deviceTypeHelper = new DeviceTypeHelper();

    @Дано("^пользователь \"([^\"]*)\" создает тип устройства \"([^\"]*)\"(?: с id \"([^\"]*)\")*$")
    public void createDeviceType(String user, String name, String id) {
        int userId = dbHelper.getUserIdByName(user);
        deviceTypeHelper.createAclDeviceType(userId, name, id);
    }
}