# language: ru

@all
@cms
@catalogue
Функционал: Получение списка жанров для каталога определенного типа

  Структура сценария:
    Дано пользователь "admin@acms.local" создает жанр "<name>" с id "<id>"
    Когда отправляем get запрос к сервису "cms" с параметрами:
      | query | catalogue_type | movies |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/CatalogueGenreSchema.json"
    Тогда json ответа сервера содержит поля:
      |          | count   | 1      |
      | [0].id   | integer | <id>   |
      | [0].name | string  | <name> |
    Примеры:
      | name          | id |
      | Какой-то жанр | 1  |