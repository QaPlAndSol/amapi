# language: ru

@all
@cms
@tech_pages
Функционал: Получение информации о документе по slug

  Сценарий: slug документа отсутствует в базе
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/slug/bez_slug/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |

  Структура сценария: slug не опубликованного документа
    Дано пользователь "admin@acms.local" создает документ "Не опубликованный документ" с slug "<slug>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/slug/<slug>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | slug                              |
      | slug-ne-opublikovannogo-dokumenta |

  Структура сценария: slug опубликованного документа
    Дано пользователь "admin@acms.local" создает документ "<name>" с slug "<slug>" текстом
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует документ "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/slug/<slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentSchema.json"
    Тогда json ответа сервера содержит поля:
      | slug  | string | <slug> |
      | title | string | <name> |
      | text  | string | <text> |
    Примеры:
      | name              | slug          | text           |
      | Какое-то название | kakoy-to-slug | Какой-то текст |

  Структура сценария: текст документа содержит html-теги
    Дано пользователь "admin@acms.local" создает документ "<name>" с slug "<slug>" текстом
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует документ "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/slug/<slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentSchema.json"
    Тогда json ответа сервера содержит поля:
      | slug  | string | <slug> |
      | title | string | <name> |
      | text  | string | <text> |
    Примеры:
      | name              | slug          | text                       |
      | Какое-то название | kakoy-to-slug | <img src="https:\\ya.ru"/> |