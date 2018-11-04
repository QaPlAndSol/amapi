package ru.pls.am.stepdefs.cms.licenses;

import cucumber.api.java.ru.Дано;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static ru.pls.am.helpers.ConvertHelper.DD_MM_YYYY_HH_MM_SS;

public class AgreementStepDefs extends AbstractStepDefs {

    @Дано("^пользователь \"([^\"]*)\" создает договор № \"([^\"]*)\" с" +
            " лицензиаром \"([^\"]*)\"" +
            "(?: фиксированной суммой \"([^\"]*)\" в валюте \"([^\"]*)\")*" +
            "(?: минимальной гарантией \"([^\"]*)\" в валюте \"([^\"]*)\" процентом от выручки \"([^\"]*)\")*" +
            " началом действия \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\"" +
            "(?: концом действия \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\")*" +
            "( (использованием DRM))*$")
    public void createAgreement(String userName, String number, String licensor, String fixPrice,
                                String fixPriceCurrency, String minGuarantee, String minGuaranteeCurrency,
                                String revenuePercent, String startTime, String endTime, String drm)
            throws ParseException {
        int userId = dbHelper.getUserIdByName(userName);
        int licensorId = dbHelper.getLicensorIdByName(licensor);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(fixPrice != null ? Float.valueOf(fixPrice) : null);                                                  // fix_price
        values.add(minGuarantee != null ? Float.valueOf(minGuarantee) : null);                                          // min_guarantee
        values.add(revenuePercent != null ? Float.valueOf(revenuePercent) : null);                                      // revenue_percent
        values.add(drm != null);                                                                                        // drm
        values.add(convertHelper.convertStringToTimestamp(startTime, DD_MM_YYYY_HH_MM_SS));                             // start_time
        values.add(endTime != null ? convertHelper.convertStringToTimestamp(endTime, DD_MM_YYYY_HH_MM_SS) : null);      // end_time
        values.add(number);                                                                                             // "number"
        values.add(userId);                                                                                             // created_by_id
        values.add(licensorId);                                                                                         // licensor_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(fixPriceCurrency != null ? fixPriceCurrency : "RUB");                                                // fix_price_currency
        values.add(minGuaranteeCurrency != null ? minGuaranteeCurrency : "RUB");                                        // min_guarantee_currency
        values.add(false);                                                                                              // is_locked
        String sql = "INSERT INTO public.licenses_agreement" +
                "(created, modified, fix_price, min_guarantee, revenue_percent, drm, start_time, end_time, " +
                "\"number\", created_by_id, licensor_id, modified_by_id, fix_price_currency, min_guarantee_currency, " +
                "is_locked)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Создали договор № " + number);
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("\"number\"", number);
        pairs.put("licensor_id", licensorId);
        dbCLeaner.addEntity("CMS", "public.licenses_agreement", pairs);
    }
}
