# language: ru

@all
@cms
@tech_pages
Функционал: Получение структуры опубликованных вопросов и ответов

  Сценарий: Разделов нет
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |

  Структура сценария: Раздел не опубликован
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с id "<id>" порядковым номером "<number>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | 0 |
      | next     | null    |   |
      | previous | null    |   |
      | results  | count   | 0 |
    Примеры:
      | section         | id | number |
      | Какая-то секция | 1  | 1      |

  Структура сценария: Раздел опубликован, имеет лого и не имеет групп
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с id "<id>" порядковым номером "<number>"
    Дано пользователь "admin@acms.local" добавляет в раздел часто задаваемых вопросов "<section>" лого "<logo>"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer    | 1         |
      | next              | null       |           |
      | previous          | null       |           |
      | results           | count      | 1         |
      | results[0].id     | integer    | <id>      |
      | results[0].logo   | stringEnds | <logo>    |
      | results[0].number | integer    | <number>  |
      | results[0].title  | string     | <section> |
      | results[0].groups | count      | 0         |
    Примеры:
      | section         | id | number | logo           |
      | Какая-то секция | 1  | 1      | LogoForFaq.jpg |

  Сценарий: Разделы отсортированы по порядоковым номерам
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "Какая-то секция 1" с id "1" порядковым номером "2"
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "Какая-то секция 2" с id "3" порядковым номером "3"
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "Какая-то секция 3" с id "2" порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "Какая-то секция 1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "Какая-то секция 2"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "Какая-то секция 3"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer | 3                 |
      | next              | null    |                   |
      | previous          | null    |                   |
      | results           | count   | 3                 |
      | results[0].number | integer | 1                 |
      | results[0].title  | string  | Какая-то секция 3 |
      | results[1].number | integer | 2                 |
      | results[1].title  | string  | Какая-то секция 1 |
      | results[2].number | integer | 3                 |
      | results[2].title  | string  | Какая-то секция 2 |

  Структура сценария: Раздел опубликован, имеет не опубликованную группу
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count             | integer | 1         |
      | next              | null    |           |
      | previous          | null    |           |
      | results           | count   | 1         |
      | results[0].logo   | null    |           |
      | results[0].title  | string  | <section> |
      | results[0].groups | count   | 0         |
    Примеры:
      | section         | group           |
      | Какая-то секция | Какая-то группа |

  Структура сценария: Раздел опубликован, имеет опубликованную группу без вопросов
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>" с id "<id>"
    Дано пользователь "admin@acms.local" публикует группу часто задаваемых вопросов "<group>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count                        | integer | 1         |
      | next                         | null    |           |
      | previous                     | null    |           |
      | results                      | count   | 1         |
      | results[0].title             | string  | <section> |
      | results[0].groups            | count   | 1         |
      | results[0].groups[0].id      | integer | <id>      |
      | results[0].groups[0].title   | string  | <group>   |
      | results[0].groups[0].answers | count   | 0         |
    Примеры:
      | section         | group           | id |
      | Какая-то секция | Какая-то группа | 1  |

  Структура сценария: Раздел опубликован, имеет опубликованную группу с не опубликованным вопросом
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" публикует группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" создает в группе "<group>" вопрос "<question>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count                        | integer | 1         |
      | next                         | null    |           |
      | previous                     | null    |           |
      | results                      | count   | 1         |
      | results[0].title             | string  | <section> |
      | results[0].groups            | count   | 1         |
      | results[0].groups[0].title   | string  | <group>   |
      | results[0].groups[0].answers | count   | 0         |
    Примеры:
      | section         | group           | question        |
      | Какая-то секция | Какая-то группа | Какой-то вопрос |

  Структура сценария: Раздел опубликован, имеет опубликованную группу с опубликованным вопросом
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" публикует группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" создает в группе "<group>" вопрос "<question>" с id "<id>"
    Дано пользователь "admin@acms.local" публикует вопрос и ответ "<question>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count                                    | integer | 1          |
      | next                                     | null    |            |
      | previous                                 | null    |            |
      | results                                  | count   | 1          |
      | results[0].title                         | string  | <section>  |
      | results[0].groups                        | count   | 1          |
      | results[0].groups[0].title               | string  | <group>    |
      | results[0].groups[0].answers             | count   | 1          |
      | results[0].groups[0].answers[0].id       | integer | <id>       |
      | results[0].groups[0].answers[0].question | string  | <question> |
      | results[0].groups[0].answers[0].answer   | string  |            |
    Примеры:
      | section         | group           | question        | id |
      | Какая-то секция | Какая-то группа | Какой-то вопрос | 1  |

  Структура сценария: Раздел опубликован, имеет опубликованную группу с опубликованным вопросом и ответом
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" публикует группу часто задаваемых вопросов "<group>"
    Дано пользователь "admin@acms.local" создает в группе "<group>" вопрос "<question>" с id "<id>"
    Дано пользователь "admin@acms.local" добавляет вопросу "<question>" ответ
    """
<answer>
    """
    Дано пользователь "admin@acms.local" публикует вопрос и ответ "<question>"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count                                    | integer | 1          |
      | next                                     | null    |            |
      | previous                                 | null    |            |
      | results                                  | count   | 1          |
      | results[0].title                         | string  | <section>  |
      | results[0].groups                        | count   | 1          |
      | results[0].groups[0].title               | string  | <group>    |
      | results[0].groups[0].answers             | count   | 1          |
      | results[0].groups[0].answers[0].id       | integer | <id>       |
      | results[0].groups[0].answers[0].question | string  | <question> |
      | results[0].groups[0].answers[0].answer   | string  | <answer>   |
    Примеры:
      | section         | group           | question        | answer         | id |
      | Какая-то секция | Какая-то группа | Какой-то вопрос | Какой-то ответ | 1  |

# ================================================== Таргетирование ====================================================

#  Сценарий: Какой-то сценарий c таргетированием
#    # TODO Допроверить когда будет сделано таргетирование
#    Дано пользователь "admin@acms.local" создает тип устройства "<string>" с id "<string>"
#
#    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "<section>" с id "<id>" порядковым номером "<number>"
#    Дано пользователь "admin@acms.local" добавляет в раздел часто задаваемых вопросов "<section>" лого "<logo>"
#    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "<section>"
#    Дано пользователь "admin@acms.local" создает в разделе "<section>" группу часто задаваемых вопросов "<group>" с id "<id>"
#    Дано пользователь "admin@acms.local" публикует группу часто задаваемых вопросов "<group>"
#    Дано пользователь "admin@acms.local" создает в группе "<group>" вопрос "<question>" с id "<id>"
#    Дано пользователь "admin@acms.local" добавляет вопросу "<question>" ответ
#    """
#
#    """
#    Дано пользователь "admin@acms.local" публикует вопрос и ответ "<question>"

# ===================================================== Пагинация ======================================================

  Структура сценария: Список разделов содержит 20 разделов, лимит по умолчанию
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count              | integer | <count> |
      | next               | null    |         |
      | previous           | null    |         |
      | results            | count   | <count> |
      | results[0].number  | integer | 1       |
      | results[19].number | integer | <count> |
    Примеры:
      | section         | count |
      | Какая-то секция | 20    |

  Структура сценария: Список разделов превышает лимит по умолчанию на 1
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация в начале списка, середине и конце
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | <limit>          |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/faq/?limit=<limit>&offset=2 |
      | previous | null       |                                         |
      | results  | count      | <limit>                                 |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | limit  | <limit>          |
      | query    | offset | 1                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                                 |
      | next     | stringEnds | /tech_pages/faq/?limit=<limit>&offset=3 |
      | previous | stringEnds | /tech_pages/faq/?limit=<limit>          |
      | results  | count      | <limit>                                 |

    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | limit  | <limit>          |
      | query    | offset | 2                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                        |
      | next     | null       |                                |
      | previous | stringEnds | /tech_pages/faq/?limit=<limit> |
      | results  | count      | <limit>                        |
    Примеры:
      | section         | count | limit |
      | Какая-то секция | 4     | 2     |

  Структура сценария: Пагинация limit 0
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | 0                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация limit NaN
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | NaN              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация limit -1
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | -1               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация limit Infinity
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | Infinity         |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация limit null
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | null             |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset 0
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | 0                |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset NaN
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | NaN              |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset -1
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | -1               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset Infinity
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | Infinity         |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset null
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | null             |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                             |
      | next     | stringEnds | /tech_pages/faq/?limit=20&offset=20 |
      | previous | null       |                                     |
      | results  | count      | 20                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 21    |

  Структура сценария: Пагинация offset = limit
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | <count>          |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                   |
      | next     | null       |                           |
      | previous | stringEnds | /tech_pages/faq/?limit=20 |
      | results  | count      | 0                         |
    Примеры:
      | section         | count |
      | Какая-то секция | 20    |

  Структура сценария: Пагинация offset > limit
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |        | /tech_pages/faq/ |
      | query    | offset | 21               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer    | <count>                            |
      | next     | null       |                                    |
      | previous | stringEnds | /tech_pages/faq/?limit=20&offset=1 |
      | results  | count      | 0                                  |
    Примеры:
      | section         | count |
      | Какая-то секция | 20    |

  Структура сценария: Пагинация limit > сущностей
    Дано пользователь "admin@acms.local" создает "<count>" разделов часто задаваемых вопросов "<section>" начиная с порядкового номера "1"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | limit | 21               |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"
    Тогда json ответа сервера содержит поля:
      | count    | integer | <count> |
      | next     | null    |         |
      | previous | null    |         |
      | results  | count   | <count> |
    Примеры:
      | section         | count |
      | Какая-то секция | 20    |

# ============================================= Общие правила работы API ===============================================

  Сценарий: Не используемый параметр запроса
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "Какая-то секция" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "Какая-то секция"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |       | /tech_pages/faq/ |
      | query    | query | query            |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"

  Сценарий: Не используемое тело запроса
    Дано пользователь "admin@acms.local" создает раздел часто задаваемых вопросов "Какая-то секция" с порядковым номером "1"
    Дано пользователь "admin@acms.local" публикует раздел часто задаваемых вопросов "Какая-то секция"
    Когда отправляем get запрос к сервису "CMS" с параметрами:
      | endpoint |  | /tech_pages/faq/ |
      | body     |  | какой-то текст   |
    Тогда статус-код ответа 200
    Тогда json ответа сервера проходит валидацию по схеме "cms/techpages/TechPagesFaqSchema.json"