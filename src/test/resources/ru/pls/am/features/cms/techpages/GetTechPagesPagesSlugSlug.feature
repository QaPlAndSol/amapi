# language: ru

@all
@cms
@tech_pages
Функционал: Получение информации по опубликованной технической странице по slug

  Сценарий: slug технической страницы отсутствует в базе
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/slug/net_slug/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |

  Структура сценария: slug не опубликованной технической страницы
    Дано пользователь "admin@acms.local" создает техническую страницу "Какое-то название" с slug "<slug>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/slug/<slug>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | slug          |
      | kakoy-to-slug |

  Структура сценария: slug опубликованной технической страницы без заголовка и текста
    Дано пользователь "admin@acms.local" создает техническую страницу "<name>" с slug "<slug>"
    Дано пользователь "admin@acms.local" публикует техническую страницу "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/slug/<slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPageSchema.json"
    Тогда json ответа сервера содержит поля:
      | slug  | string | <slug> |
      | title | null   |        |
      | text  | string |        |
    Примеры:
      | name              | slug          |
      | Какое-то название | kakoy-to-slug |

  Структура сценария: slug опубликованной технической страницы с заголовком и текстом
    Дано пользователь "admin@acms.local" создает техническую страницу "<name>" с slug "<slug>" заголовком "<title>"
    Дано пользователь "admin@acms.local" добавляет текст технической страницы "<name>"
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует техническую страницу "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/slug/<slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPageSchema.json"
    Тогда json ответа сервера содержит поля:
      | slug  | string | <slug>  |
      | title | string | <title> |
      | text  | string | <text>  |
    Примеры:
      | name              | slug          | text           | title          |
      | Какое-то название | kakoy-to-slug | Какой-то текст | Какой-то тайтл |

  Структура сценария: текст технической страницы содержит html-теги
    Дано пользователь "admin@acms.local" создает техническую страницу "<name>" с slug "<slug>"
    Дано пользователь "admin@acms.local" добавляет текст технической страницы "<name>"
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует техническую страницу "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/slug/<slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPageSchema.json"
    Тогда json ответа сервера содержит поля:
      | slug  | string | <slug> |
      | title | null   |        |
      | text  | string | <text> |
    Примеры:
      | name              | slug          | text                       |
      | Какое-то название | kakoy-to-slug | <img src="https:\\ya.ru"/> |