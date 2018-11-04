# language: ru

@all
@cms
@metadata
Функционал: Получение жанра по slug

  Структура сценария: slug жанра отсутствует в базе
  Должна вернуться 404 ошибка
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /metadata/genres/slug/<genre_slug>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/ErrorDetail.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | genre_slug      |
      | net_takogo_slug |

  Структура сценария: slug не опубликованного жанра
  Должна вернуться 404 ошибка
    Дано пользователь "<user>" создает жанр "<genre_name>" с slug "<genre_slug>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /metadata/genres/slug/<genre_slug>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/ErrorDetail.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | user             | genre_slug | genre_name        |
      | admin@acms.local | 1001       | Какой-то там жанр |

  Структура сценария: slug опубликованного жанра с датой публикации позже текущей
  Должна вернуться 404 ошибка
    Дано пользователь "<user>" создает жанр "<genre_name>" с slug "<genre_slug>"
    Дано пользователь "<user>" публикует жанр "<genre_name>" "01.01.2050 00:00:00"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /metadata/genres/slug/<genre_slug>/ |
    Тогда статус-код ответа 404
    Тогда json ответа сервера проходит валидацию по схеме "cms/ErrorDetail.json"
    Тогда json ответа сервера содержит поля:
      | detail | string | Не найдено. |
    Примеры:
      | user             | genre_slug | genre_name        |
      | admin@acms.local | 1001       | Какой-то там жанр |

  Структура сценария: slug опубликованного жанра
  Должны вернуться поля id и name
    Дано пользователь "<user>" создает жанр "<genre_name>" с slug "<genre_slug>"
    Дано пользователь "<user>" публикует жанр "<genre_name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /metadata/genres/slug/<genre_slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/metadata/MetadataGenreSchema.json"
    Тогда json ответа сервера содержит поля:
      | name | string | <genre_name> |
    Примеры:
      | user             | genre_slug | genre_name        |
      | admin@acms.local | 1001       | Какой-то там жанр |

  Сценарий: slug опубликованного жанра с описанием
  Должны вернуться поля id и name, поле description не возвращается
    Дано пользователь "<user>" создает жанр "<genre_name>" с slug "<genre_slug>"
    Дано пользователь "<user>" публикует жанр "<genre_name>"
    Дано пользователь "<user>" добавляет описание жанра "<genre_name>"
    """
    Какое-то описание жанра
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /metadata/genres/slug/<genre_slug>/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/metadata/MetadataGenreSchema.json"
    Тогда json ответа сервера содержит поля:
      | name | string | <genre_name> |
    Примеры:
      | user             | genre_slug | genre_name        |
      | admin@acms.local | 1001       | Какой-то там жанр |