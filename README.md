### Описание приложения
Простое приложение для составления списка покупок.

Функциональность:
* Добавление позиций с указанием наименования и количества
* Изменение текущих позиций (наименование и количество)
* Удаление позиций

При добавлении позиции, уже существующей в базе данных, введенное количество складывается с тем, которое находится в БД.

### Используемы технологии
* JSP 2.2
* PrimeFaces 6.0
* Hibernate 4.3
* БД PostgeSQL 9.3
* Сервер Topcat 8.5

Все используемые библиотеки (.jar) хранятся в папке **Libraries**

### База данных
Backup базы данных (база данных + данные): **backup_nikitina_bd.backup**

Скрипт для создания таблицы: **table_item_create.sql**

### Доп.коментарии
Для работы приложения необходимо изменить файл **/JSFproject/src/hibernate.cfg.xml** для подключения к БД.
Строки:
```XML
<property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
<property name="hibernate.connection.driver_class">org.postgresql.Driver</property>

<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/ann_app</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">Qwerty123</property>
```
