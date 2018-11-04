# language: ru

@all
@cms
@tech_pages
Функционал: Получение списка документов

  Сценарий: Список документов пуст, документов нет
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Сценарий: Список документов пуст, документ не опубликован
    Дано пользователь "admin@acms.local" создает документ "Не опубликованный документ" с id "1" slug "slug-ne-opublikovannogo-dokumenta" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Структура сценария: Список документов содержит 1 опубликованный документ и не содержит не опубликованный
    Дано пользователь "admin@acms.local" создает документ "Не опубликованный документ" с id "1" slug "slug-ne-opublikovannogo-dokumenta" текстом
    """
Какой-то текст
    """
    Дано пользователь "admin@acms.local" создает документ "<name>" с id "2" slug "<slug>" текстом
    """
Какой-то текст
    """
    Дано пользователь "admin@acms.local" публикует документ "<name>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer | 1      |
      | next              | null    |        |
      | previous          | null    |        |
      | results           | count   | 1      |
      | results[0].id     | integer | 2      |
      | results[0].slug   | string  | <slug> |
      | results[0].number | integer | 2      |
      | results[0].title  | string  | <name> |
    Примеры:
      | name              | slug          |
      | Какое-то название | kakoy-to-slug |

# ===================================================== Пагинация ======================================================

  Структура сценария: Список документов содержит 20 документов, лимит по умолчанию
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count              | integer | <count> |
      | next               | null    |         |
      | previous           | null    |         |
      | results            | count   | <count> |
      | results[0].number  | integer | 1       |
      | results[19].number | integer | <count> |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Список документов превышает лимит по умолчанию на 1
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация в начале списка, середине и конце
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | <limit>            |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                   |
      | next     | stringEnds | /tech_pages/legal/?limit=<limit>&offset=2 |
      | previous | null       |                                           |
      | results  | count      | <limit>                                   |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | limit  | <limit>            |
      | query    | offset | 1                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                   |
      | next     | stringEnds | /tech_pages/legal/?limit=<limit>&offset=3 |
      | previous | stringEnds | /tech_pages/legal/?limit=<limit>          |
      | results  | count      | <limit>                                   |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | limit  | <limit>            |
      | query    | offset | 2                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                          |
      | next     | null       |                                  |
      | previous | stringEnds | /tech_pages/legal/?limit=<limit> |
      | results  | count      | <limit>                          |
    Примеры:
      | name              | count | limit |
      | Какое-то название | 4     | 2     |

  Структура сценария: Пагинация limit 0
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | 0                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit NaN
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | NaN                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit -1
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | -1                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit Infinity
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | Infinity           |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация limit null
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | null               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset 0
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | 0                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset NaN
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | NaN                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset -1
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | -1                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset Infinity
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | Infinity           |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset null
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | null               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                               |
      | next     | stringEnds | /tech_pages/legal/?limit=20&offset=20 |
      | previous | null       |                                       |
      | results  | count      | 20                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 21    |

  Структура сценария: Пагинация offset = limit
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | <count>            |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                     |
      | next     | null       |                             |
      | previous | stringEnds | /tech_pages/legal/?limit=20 |
      | results  | count      | 0                           |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Пагинация offset > limit
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/legal/ |
      | query    | offset | 21                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                              |
      | next     | null       |                                      |
      | previous | stringEnds | /tech_pages/legal/?limit=20&offset=1 |
      | results  | count      | 0                                    |
    Примеры:
      | name              | count |
      | Какое-то название | 20    |

  Структура сценария: Пагинация limit > сущностей
    Дано пользователь "admin@acms.local" создает "<count>" документов с названием "<name>" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | limit | 21                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"
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
    Дано пользователь "admin@acms.local" создает документ "Какое-то название" с slug "kakoy-to-slug" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/legal/ |
      | query    | query | query              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"

  Сценарий: Не используемое тело запроса
    Дано пользователь "admin@acms.local" создает документ "Какое-то название" с slug "kakoy-to-slug" текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/legal/ |
      | body     |  | какой-то текст     |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesDocumentsSchema.json"