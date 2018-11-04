package ru.pls.am.helpers;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ru.pls.am.DbConnect;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DbHelper {

    private static Jdbi dbCms;

    /**
     * Инициализация хелпера для работы с БД
     *
     * @throws FileNotFoundException не найден файл с параметрами подключения к бд
     */
    public DbHelper() throws IOException {
        dbCms = new DbConnect("CMS").getJdbi();
    }

    /**
     * Возвращает подключение к БД CMS
     *
     * @return подключение к БД CMS
     */
    public Jdbi getDbCms() {
        return dbCms;
    }

    /**
     * Выполняет SQL скрипт с подстановкой параметров
     *
     * @param sql  скрипт
     * @param args параметры
     */
    public void executeSqlScript(String sql, Object... args) {
        dbCms.useHandle(handle -> handle.execute(sql, args));
    }

    /**
     * Возвращает id пользователя по его имени
     *
     * @param userName имя пользователя
     * @return id пользователя
     */
    public int getUserIdByName(String userName) {
        int userId;
        String sql = "SELECT id FROM public.accounts_user WHERE username=?;";
        try (Handle handle = dbCms.open()) {
            userId = Integer.valueOf(handle.createQuery(sql).bind(0, userName)
                    .mapTo(String.class).findOnly());
        }
        return userId;
    }

    /**
     * Возвращает id жанра по его названию
     *
     * @param genreName название жанра
     * @return id жанра
     */
    public int getGenreIdByName(String genreName) {
        int genreId;
        String sql = "SELECT id FROM public.metadata_genre WHERE \"name\"=?;";
        try (Handle handle = dbCms.open()) {
            genreId = Integer.valueOf(handle.createQuery(sql).bind(0, genreName)
                    .mapTo(String.class).findOnly());
        }
        return genreId;
    }

    /**
     * Возвращает id контента по его техническому названию
     *
     * @param contentTechnicalName техническое название контента
     * @return id контента
     */
    public int getContentIdByTechnicalName(String contentTechnicalName) {
        int contentId;
        String sql = "SELECT id FROM public.content_content WHERE autocomplete_title=?;";
        try (Handle handle = dbCms.open()) {
            contentId = Integer.valueOf(handle.createQuery(sql).bind(0, contentTechnicalName)
                    .mapTo(String.class).findOnly());
        }
        return contentId;
    }

    /**
     * Возвращает id коллекции по ее названию
     *
     * @param collectionName название коллекции
     * @return id коллекции
     */
    public int getCollectionIdByName(String collectionName) {
        int collectionId;
        String sql = "SELECT id FROM public.showcases_collection WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            collectionId = Integer.valueOf(handle.createQuery(sql).bind(0, collectionName)
                    .mapTo(String.class).findOnly());
        }
        return collectionId;
    }

    /**
     * Возвращает id правообладателя по его названию
     *
     * @param rightsHolderName название правообладателя
     * @return id правообладателя
     */
    public int getRightsHolderIdByName(String rightsHolderName) {
        int rightsHolderId;
        String sql = "SELECT id FROM public.licenses_rightsholder WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            rightsHolderId = Integer.valueOf(handle.createQuery(sql).bind(0, rightsHolderName)
                    .mapTo(String.class).findOnly());
        }
        return rightsHolderId;
    }

    /**
     * Возвращает id лицензиара по его названию
     *
     * @param licensor название лицензиара
     * @return id лицензиара
     */
    public int getLicensorIdByName(String licensor) {
        int licensorId;
        String sql = "SELECT id FROM public.licenses_licensor WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            licensorId = Integer.valueOf(handle.createQuery(sql).bind(0, licensor)
                    .mapTo(String.class).findOnly());
        }
        return licensorId;
    }

    /**
     * Возвращает id договора по его номеру и лицензиару
     *
     * @param agreementNumber номер договора
     * @param licensor        лицензиар
     * @return id договора
     */
    public int getAgreementIdByNumberAndLicensor(String agreementNumber, String licensor) {
        int agreementId;
        String sql = "SELECT id FROM public.licenses_agreement " +
                "WHERE \"number\"=? AND licensor=(SELECT id FROM public.licenses_licensor WHERE title=?);";
        try (Handle handle = dbCms.open()) {
            agreementId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, agreementNumber)
                    .bind(1, licensor)
                    .mapTo(String.class).findOnly());
        }
        return agreementId;
    }

    /**
     * Возвращает id типа ассета
     *
     * @param title тип ассета
     * @return id типа ассета
     */
    public int getAssetTypeIdByTitle(String title) {
        int assetTypeId;
        String sql = "SELECT id FROM public.assets_assettype WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            assetTypeId = Integer.valueOf(handle.createQuery(sql).bind(0, title)
                    .mapTo(String.class).findOnly());
        }
        return assetTypeId;
    }

    /**
     * Возвращает id типа контента
     *
     * @param model тип контента
     * @return id типа контента
     */
    public int getContentTypeIdByContentType(String model) {
        switch (model.toLowerCase()) {
            case "ассет контента":
                model = "contentasset";
                break;
            case "ассет баннера":
                model = "bannerasset";
                break;
            case "коллекция":
                model = "collectionasset";
                break;
            case "изображение для слайда":
                model = "screenimageasset";
                break;
            case "видео для слайда":
                model = "screenvideoasset";
                break;
            default:
                model = null;
        }
        int contentTypeId;
        String sql = "SELECT id FROM public.django_content_type WHERE model=?;";
        try (Handle handle = dbCms.open()) {
            contentTypeId = Integer.valueOf(handle.createQuery(sql).bind(0, model)
                    .mapTo(String.class).findOnly());
        }
        return contentTypeId;
    }

    /**
     * Возвращает id онбординга
     *
     * @param title название онбординга
     * @return id онбординга
     */
    public int getOnBoardingIdByTitle(String title) {
        int onBoardingId;
        String sql = "SELECT id FROM public.onboarding_onboarding WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            onBoardingId = Integer.valueOf(handle.createQuery(sql).bind(0, title)
                    .mapTo(String.class).findOnly());
        }
        return onBoardingId;
    }

    /**
     * Возвращает id слайда онбординга
     *
     * @param title название слайда онбординга
     * @return id слайда онбординга
     */
    public int getOnBoardingScreenIdByTitle(String title) {
        int onBoardingScreenId;
        String sql = "SELECT id FROM public.onboarding_onboardingscreen WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            onBoardingScreenId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, title)
                    .mapTo(String.class).findOnly());
        }
        return onBoardingScreenId;
    }

    /**
     * Возвращает id типа устройства
     *
     * @param name название типа устройства
     * @return id типа устройства
     */
    public int getDeviceTypeIdByName(String name) {
        int deviceTypeId;
        String sql = "SELECT id FROM public.acl_devicetype WHERE \"name\"=?;";
        try (Handle handle = dbCms.open()) {
            deviceTypeId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, name)
                    .mapTo(String.class).findOnly());
        }
        return deviceTypeId;
    }

    /**
     * Возвращает id раздела часто задаваемых вопросов
     *
     * @param name название раздела часто задаваемых вопросов
     * @return id раздела часто задаваемых вопросов
     */
    public int getFaqSectionIdByName(String name) {
        int faqSectionId;
        String sql = "SELECT id FROM public.tech_pages_faqsection WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            faqSectionId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, name)
                    .mapTo(String.class).findOnly());
        }
        return faqSectionId;
    }

    /**
     * Возвращает id группы часто задаваемых вопросов
     *
     * @param name название группы часто задаваемых вопросов
     * @return id группы часто задаваемых вопросов
     */
    public int getFaqGroupIdByName(String name) {
        int faqGroupId;
        String sql = "SELECT id FROM public.tech_pages_faqgroup WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            faqGroupId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, name)
                    .mapTo(String.class).findOnly());
        }
        return faqGroupId;
    }

    /**
     * Возвращает id видео
     *
     * @param name название видео
     * @return id видео
     */
    public int getVideoIdByName(String name) {
        int videoId;
        String sql = "SELECT id FROM public.video_video WHERE title=?;";
        try (Handle handle = dbCms.open()) {
            videoId = Integer.valueOf(handle.createQuery(sql)
                    .bind(0, name)
                    .mapTo(String.class).findOnly());
        }
        return videoId;
    }
}
