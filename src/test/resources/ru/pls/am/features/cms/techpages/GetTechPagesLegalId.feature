# language: ru

@all
@cms
@tech_pages
Функционал: Получение информации о документе по id

  Сценарий: id документа отсутствует в базе
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/0/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |

  Структура сценария: id не опубликованного документа
    Дано пользователь "admin@acms.local" создает документ "Не опубликованный документ" с id "<id>" slug "slug-ne-opublikovannogo-dokumenta" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/<id>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/404NotFound.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | id |
      | 1  |

  Структура сценария: id опубликованного документа
    Дано пользователь "admin@acms.local" создает документ "<name>" с id "<id>" slug "<slug>" текстом
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует документ "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/<id>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentSchema.json"
    Тогда json ответа сервера содержит поля:
      | id    | integer | <id>   |
      | slug  | string  | <slug> |
      | title | string  | <name> |
      | text  | string  | <text> |
    Примеры:
      | id | name              | slug          | text           |
      | 1  | Какое-то название | kakoy-to-slug | Какой-то текст |

  Структура сценария: текст документа содержит html-теги
    Дано пользователь "admin@acms.local" создает документ "<name>" с id "<id>" slug "<slug>" текстом
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует документ "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/<id>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentSchema.json"
    Тогда json ответа сервера содержит поля:
      | id    | integer | <id>   |
      | slug  | string  | <slug> |
      | title | string  | <name> |
      | text  | string  | <text> |
    Примеры:
      | id | name              | slug          | text                       |
      | 1  | Какое-то название | kakoy-to-slug | <img src="https:\\ya.ru"/> |