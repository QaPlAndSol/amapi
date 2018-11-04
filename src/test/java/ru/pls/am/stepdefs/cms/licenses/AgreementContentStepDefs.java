package ru.pls.am.stepdefs.cms.licenses;

import cucumber.api.java.ru.Дано;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static ru.pls.am.helpers.ConvertHelper.DD_MM_YYYY_HH_MM_SS;

public class AgreementContentStepDefs extends AbstractStepDefs {

    @Дано("^пользователь \"([^\"]*)\" добавляет (фильм|многосерийный фильм|сериал|сезон|эпизод) \"([^\"]*)\"" +
            " к договору \"([^\"]*)\"" +
            " с лицензиаром \"([^\"]*)\"" +
            "(?: фиксированной суммой \"([^\"]*)\" в валюте \"([^\"]*)\")*" +
            "(?: минимальной гарантией \"([^\"]*)\" в валюте \"([^\"]*)\" процентом от выручки \"([^\"]*)\")*" +
            "(?: началом действия \"([^\"]*)\")*" +
            "(?: концом действия \"([^\"]*)\")*" +
            "(?: (использованием DRM))*$")
    public void createAgreementContent(String userName, String contentType, String content, String agreement,
                                       String licensor, String fixPrice, String fixPriceCurrency, String minGuarantee,
                                       String minGuaranteeCurrency, String revenuePercent, String startTime,
                                       String endTime, String drm)
            throws ParseException {
        int userId = dbHelper.getUserIdByName(userName);
        int contentId = dbHelper.getContentIdByTechnicalName(content);
        int agreementId = dbHelper.getAgreementIdByNumberAndLicensor(agreement, licensor);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(fixPrice != null ? Float.valueOf(fixPrice) : null);                                                  // fix_price
        values.add(minGuarantee != null ? Float.valueOf(minGuarantee) : null);                                          // min_guarantee
        values.add(revenuePercent != null ? Float.valueOf(revenuePercent) : null);                                      // revenue_percent
        values.add(drm != null);                                                                                        // drm
        values.add(startTime != null ? convertHelper.convertStringToTimestamp(startTime, DD_MM_YYYY_HH_MM_SS) :
                convertHelper.getActualTimestamp());                                                                    // start_time
        values.add(endTime != null ? convertHelper.convertStringToTimestamp(endTime, DD_MM_YYYY_HH_MM_SS) : null);      // end_time
        values.add(agreementId);                                                                                        // agreement_id
        values.add(contentId);                                                                                          // content_id
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(fixPriceCurrency != null ? fixPriceCurrency : "RUB");                                                // fix_price_currency
        values.add(minGuaranteeCurrency != null ? minGuaranteeCurrency : "RUB");                                        // min_guarantee_currency
        values.add(false);                                                                                              // is_locked
        String sql = "INSERT INTO public.licenses_agreementcontent" +
                "(created, modified, fix_price, min_guarantee, revenue_percent, drm, start_time, end_time, " +
                "agreement_id, content_id, created_by_id, modified_by_id, fix_price_currency, min_guarantee_currency)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Привязали " + contentType + " " + content + " к договор № " + agreement);
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("agreement_id", agreementId);
        pairs.put("content_id", contentId);
        dbCLeaner.addEntity("CMS", "public.licenses_agreementcontent", pairs);
    }
}
