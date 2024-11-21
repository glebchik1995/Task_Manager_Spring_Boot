# Разработка Системы Управления Задачами (Task Management System)

Этот документ содержит инструкции по локальному запуску проекта системы управления задачами, разработанного на Java с использованием Spring Boot, Spring Security и Docker Compose.

## Требования:

* Java 17+
* Docker
* Docker Compose
* Maven

## Запуск проекта:

1. *Клонирование репозитория:*

bash
git clone <ссылка_на_ваш_репозиторий>

2. *Включение переменных окружения (если необходимо):*
   Если ваш проект использует переменные окружения для подключения к базе данных, убедитесь, что они установлены перед запуском приложения. Например, для PostgreSQL:

bash
* export POSTGRES_USER=<ваш_пользователь>
* export POSTGRES_PASSWORD=<ваш_пароль>
* export POSTGRES_HOST=<хост_базы_данных>
* export POSTGRES_PORT=<порт_базы_данных>
* export POSTGRES_DB=<имя_базы_данных>

3. *Установка зависимостей:*

bash
cd <название_проекта>
mvn clean install

4. *Запуск с помощью Docker Compose:*

bash
docker-compose up -d

Эта команда поднимет все необходимые контейнеры: базу данных PostgreSQL, приложение Spring Boot и другие необходимые службы.

5. *Доступ к API:*

После успешного запуска Docker Compose, вы можете взаимодействовать с API системы управления задачами по адресу:


http://localhost:8080/api/v1/

*Убедитесь, что ваше приложение доступно по указанному адресу. Если это не так, проверьте журналы Docker контейнеров и лог-файлы Spring Boot приложения.*

6. *Swagger UI:*

Откройте в браузере адрес:


http://localhost:8080/swagger-ui/index.html

Это откроет страницу Swagger UI, где можно ознакомиться с описанием API и использовать интерактивные примеры запросов.

## Подготовка к работе:

* *Проверка базы данных:* Убедитесь, что база данных настроена и подключение успешно, проверив таблицы и структуры данных.
* *Настройка `application.properties`:* В файле `application.properties` настройте параметры подключения к базе данных, имя пользователя, пароль и другие необходимые параметры. Вы найдете примеры конфигурации для PostgreSQL в файлах проекта. Вы можете переопределить эти параметры в переменных окружения.
* *Проверка настроек:* Проверьте корректность настроек подключения к базе данных, JWT токенов, аутентификации и авторизации.


## Дополнительная информация:

* *Docker Compose:* Подробную информацию о Docker Compose см. на [официальном сайте](https://docs.docker.com/compose/).
* *Spring Boot:* Подробную информацию о Spring Boot см. на [официальном сайте](https://spring.io/projects/spring-boot).
* *Spring Security:* Подробную информацию о Spring Security см. на [официальном сайте](https://spring.io/projects/spring-security).


## Тестирование (после успешного запуска):


Вы можете использовать Swagger UI для проверки всех API-функций. Также, в репозитории присутствуют тесты для основных функциональных возможностей, которые можно запустить с помощью Maven (mvn test).


*Обратите внимание:* Замените `<ссылка_на_ваш_репозиторий>` на действительную ссылку на ваш репозиторий. Также, убедитесь, что файлы `docker-compose.yml` и `application.properties` правильно настроены для вашей среды.