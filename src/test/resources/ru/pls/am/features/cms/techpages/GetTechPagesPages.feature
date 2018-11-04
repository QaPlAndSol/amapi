# language: ru

@all
@cms
@tech_pages
Функционал: Получение списка опубликованных технических страниц

  Сценарий: Список технических страниц пуст, технических страниц нет
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Сценарий: Список технических страниц пуст, техническая страница не опубликована
    Дано пользователь "admin@acms.local" создает техническую страницу "Не опубликованная страница" с id "1" slug "slug-ne-opublikovannoy-stranicy"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Структура сценария: Список технических страниц содержит 1 опубликованную страницу и не содержит не опубликованную
    Дано пользователь "admin@acms.local" создает техническую страницу "Не опубликованная страница" с id "1" slug "slug-ne-opublikovannoy-stranicy"
    Дано пользователь "admin@acms.local" создает техническую страницу "<name>" с id "2" slug "<slug>" заголовком "<title>"
    Дано пользователь "admin@acms.local" добавляет текст технической страницы "<name>"
    """
<text>
    """
    Дано пользователь "admin@acms.local" публикует техническую страницу "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count            | integer | 1       |
      | next             | null    |         |
      | previous         | null    |         |
      | results          | count   | 1       |
      | results[0].id    | integer | 2       |
      | results[0].slug  | string  | <slug>  |
      | results[0].title | string  | <title> |
      | results[0].text  | string  | <text>  |
    Примеры:
      | name              | slug          | title          | text           |
      | Какое-то название | kakoy-to-slug | Какой-то тайтл | Какой-то текст |

  Структура сценария: Список технических страниц содержит 1 опубликованную страницу без заголовка и текста
    Дано пользователь "admin@acms.local" создает техническую страницу "<name>" с id "2" slug "<slug>"
    Дано пользователь "admin@acms.local" публикует техническую страницу "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count            | integer | 1      |
      | next             | null    |        |
      | previous         | null    |        |
      | results          | count   | 1      |
      | results[0].id    | integer | 2      |
      | results[0].slug  | string  | <slug> |
      | results[0].title | null    |        |
      | results[0].text  | string  |        |
    Примеры:
      | name              | slug          |
      | Какое-то название | kakoy-to-slug |

# ===================================================== Пагинация ======================================================

  Структура сценария: Список технических страниц содержит 20 страниц, лимит по умолчанию
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer | <count> |
      | next              | null    |         |
      | previous          | null    |         |
      | results           | count   | <count> |
      | results[0].title  | null    |         |
      | results[0].text   | string  |         |
      | results[19].title | null    |         |
      | results[19].text  | string  |         |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Список технических страниц превышает лимит по умолчанию на 1
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация в начале списка, середине и конце
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | <limit>            |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                   |
      | next     | stringEnds | /tech_pages/pages/?limit=<limit>&offset=2 |
      | previous | null       |                                           |
      | results  | count      | <limit>                                   |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | limit  | <limit>            |
      | query    | offset | 1                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                   |
      | next     | stringEnds | /tech_pages/pages/?limit=<limit>&offset=3 |
      | previous | stringEnds | /tech_pages/pages/?limit=<limit>          |
      | results  | count      | <limit>                                   |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | limit  | <limit>            |
      | query    | offset | 2                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                          |
      | next     | null       |                                  |
      | previous | stringEnds | /tech_pages/pages/?limit=<limit> |
      | results  | count      | <limit>                          |
    Примеры:
      | name              | count | limit |
      | Какое-то название | 4     | 2     |

  Структура сценария: Пагинация limit 0
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | 0                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit NaN
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | NaN                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit -1
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | -1                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit Infinity
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | Infinity           |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit null
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | null               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset 0
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | 0                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset NaN
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | NaN                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset -1
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | -1                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset Infinity
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | Infinity           |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset null
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | null               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/pages/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset = limit
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | <count>            |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                     |
      | next     | null       |                             |
      | previous | stringEnds | /tech_pages/pages/?limit=20 |
      | results  | count      | 0                           |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Пагинация offset > limit
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/pages/ |
      | query    | offset | 21                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                              |
      | next     | null       |                                      |
      | previous | stringEnds | /tech_pages/pages/?limit=20&offset=1 |
      | results  | count      | 0                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Пагинация limit > сущностей
    Дано пользователь "admin@acms.local" создает "<count>" технических страниц с названием "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | limit | 21                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | <count> |
      | next     | null    |         |
      | previous | null    |         |
      | results  | count   | <count> |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

# ============================================= Общие правила работы API ===============================================

  Сценарий: Не используемый параметр запроса
    Дано пользователь "admin@acms.local" создает техническую страницу "Какое-то название" с slug "kakoy-to-slug"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/pages/ |
      | query    | query | query              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"

  Сценарий: Не используемое тело запроса
    Дано пользователь "admin@acms.local" создает техническую страницу "Какое-то название" с slug "kakoy-to-slug"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/pages/ |
      | body     |  | какой-то текст     |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesPagesSchema.json"