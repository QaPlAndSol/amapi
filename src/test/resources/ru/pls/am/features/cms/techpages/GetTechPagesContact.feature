# language: ru

@all
@cms
@tech_pages
Функционал: Получение списка блоков контактов

  Сценарий: Список блоков контактов пуст
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/contact/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Структура сценария: Список блоков контактов содержит 1 блок
    Дано пользователь "admin@acms.local" создает блок контактов с id "1" типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/contact/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer | 1       |
      | next              | null    |         |
      | previous          | null    |         |
      | results           | count   | 1       |
      | results[0].id     | integer | 1       |
      | results[0].number | integer | 1       |
      | results[0].title  | string  | <title> |
      | results[0].text   | string  | <text>  |
    Примеры:
      | title          | text           |
      | Какой-то тайтл | Какой-то текст |

# ===================================================== Пагинация ======================================================

  Структура сценария: Список блоков контактов содержит 20 блоков, лимит по умолчанию
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/contact/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count              | integer | <count> |
      | next               | null    |         |
      | previous           | null    |         |
      | results            | count   | <count> |
      | results[0].number  | integer | 1       |
      | results[0].title   | string  | <title> |
      | results[0].text    | string  | <text>  |
      | results[19].number | integer | 20      |
      | results[19].title  | string  | <title> |
      | results[19].text   | string  | <text>  |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 20    |

  Структура сценария: Список блоков контактов превышает лимит по умолчанию на 1
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/contact/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count              | integer    | <count>                                 |
      | next               | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous           | null       |                                         |
      | results            | count      | 20                                      |
      | results[0].number  | integer    | 1                                       |
      | results[0].title   | string     | <title>                                 |
      | results[0].text    | string     | <text>                                  |
      | results[19].number | integer    | 20                                      |
      | results[19].title  | string     | <title>                                 |
      | results[19].text   | string     | <text>                                  |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация в начале списка, середине и конце
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | <limit>              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                     |
      | next     | stringEnds | /tech_pages/contact/?limit=<limit>&offset=2 |
      | previous | null       |                                             |
      | results  | count      | <limit>                                     |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | limit  | <limit>              |
      | query    | offset | 1                    |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                     |
      | next     | stringEnds | /tech_pages/contact/?limit=<limit>&offset=3 |
      | previous | stringEnds | /tech_pages/contact/?limit=<limit>          |
      | results  | count      | <limit>                                     |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | limit  | <limit>              |
      | query    | offset | 2                    |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                            |
      | next     | null       |                                    |
      | previous | stringEnds | /tech_pages/contact/?limit=<limit> |
      | results  | count      | <limit>                            |
    Примеры:
      | title          | text           | count | limit |
      | Какой-то тайтл | Какой-то текст | 4     | 2     |

  Структура сценария: Пагинация limit 0
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | 0                    |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация limit NaN
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | NaN                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация limit -1
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | -1                   |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация limit Infinity
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | Infinity             |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация limit null
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | null                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset 0
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | 0                    |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset NaN
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | NaN                  |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset -1
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | -1                   |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset Infinity
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | Infinity             |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset null
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | null                 |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/contact/?limit=20&offset=20 |
      | previous | null       |                                         |
      | results  | count      | 20                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 21    |

  Структура сценария: Пагинация offset = limit
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | <count>              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                       |
      | next     | null       |                               |
      | previous | stringEnds | /tech_pages/contact/?limit=20 |
      | results  | count      | 0                             |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 20    |

  Структура сценария: Пагинация offset > limit
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/contact/ |
      | query    | offset | 21                   |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                |
      | next     | null       |                                        |
      | previous | stringEnds | /tech_pages/contact/?limit=20&offset=1 |
      | results  | count      | 0                                      |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 20    |

  Структура сценария: Пагинация limit > сущностей
    Дано пользователь "admin@acms.local" создает "<count>" блоков контактов с типом "<title>" и текстом
    """
<text>
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | limit | 21                   |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | <count> |
      | next     | null    |         |
      | previous | null    |         |
      | results  | count   | <count> |
    Примеры:
      | title          | text           | count |
      | Какой-то тайтл | Какой-то текст | 20    |

# ============================================= Общие правила работы API ===============================================

  Сценарий: Не используемый параметр запроса
    Дано пользователь "admin@acms.local" создает блок контактов с типом "Какой-то тайтл" и текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/contact/ |
      | query    | query | query                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"

  Сценарий: Не используемое тело запроса
    Дано пользователь "admin@acms.local" создает блок контактов с типом "Какой-то тайтл" и текстом
    """
Какой-то текст
    """
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/contact/ |
      | body     |  | какой-то текст       |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesContactSchema.json"