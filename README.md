

# ProjectHub 

[![CI](https://github.com/pescaderojalauddin-source/projecthub/actions/workflows/ci.yml/badge.svg)](https://github.com/pescaderojalauddin-source/projecthub/actions/workflows/ci.yml)

> Мой курсовой проект: система управления задачами и проектами.
> Стек: **Spring Boot 3 + Thymeleaf + Bootstrap 5**.

## О проекте

ProjectHub — это веб-приложение, где можно создавать проекты, ставить задачи, назначать исполнителей и следить за дедлайнами. Всё работает через удобный интерфейс, а история изменений сохраняется в комментариях.

**Что внутри (база по ТЗ):**
*   Классическая трехслойная архитектура: `Controller` (Thymeleaf), `Service` (логика + транзакции), `Repository` (Spring Data JPA).
*   Сущности: **User**, **Project**, **Task**, **Comment** (связи 1:N).
*   Роли **USER** и **ADMIN** с разграничением прав (RBAC через `@PreAuthorize`).
*   Безопасность: Spring Security, BCrypt для паролей, защита от CSRF.
*   Валидация форм через `@Valid`.
*   Обработка ошибок: красивые страницы 400/403/404/500 и отдельный обработчик для REST API.
*   REST API (`/api/v1/**`) с документацией Swagger/OpenAPI.
*   Мониторинг через JavaMelody (доступен только админу).
*   Асинхронность: фоновое обновление статистики через `@Async` и `@Scheduled`.
*   Тесты: Unit (JUnit 5 + Mockito) и интеграционные с реальной БД через Testcontainers.

### Фичи сверх требований 

Решил не ограничиваться минимумом и добавил около 20 плюшек для удобства и красоты:
*   Личный дашборд «Мой день» с графиками Chart.js.
*   Глобальный поиск прямо в навбаре.
*   Прогресс-бары выполнения проектов.
*   Аватарки с инициалами (если нет фото).
*   Избранные проекты (можно добавить в закладки).
*   Конфетти при закрытии задачи 🎉
*   Календарь дедлайнов.
*   Горячие клавиши (например, `/` для поиска, `g d` для дашборда).
*   Toast-уведомления вместо алертов.
*   Поддержка Markdown в описаниях.
*   Система ачивок.
*   Приветствие пользователя в зависимости от времени суток.
*   Экспорт задач в CSV.
*   Приоритеты и теги для задач.
*   Burndown-график.
*   Упоминания (@mentions) в комментариях.
*   Возможность прикреплять файлы.
*   Утренний email-дайджест о просроченных задачах.

Полный список с примерами кода — в файле [`WHATS-DONE.md`](WHATS-DONE.md).

## Стек технологий

| Компонент | Технологии |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.5, Spring Web, Spring Security, Spring Data JPA, Validation |
| **ORM** | Hibernate / Jakarta Persistence |
| **Миграции БД** | Flyway |
| **Frontend** | Thymeleaf, Bootstrap 5, Bootstrap Icons |
| **База данных** | H2 (для разработки) или PostgreSQL 16 (для прода) |
| **Сборка** | Maven (wrapper `./mvnw`) |
| **API Docs** | springdoc-openapi 2.x (Swagger UI) |
| **Тесты** | JUnit 5, Mockito, Testcontainers (Postgres) |
| **Мониторинг** | Actuator, JavaMelody |

## Быстрый старт

Ничего лишнего устанавливать не нужно, кроме JDK 17+.

```bash
git clone https://github.com/pescaderojalauddin-source/projecthub.git
cd projecthub
./mvnw spring-boot:run
```

Открываем браузер: `http://localhost:8080`

### Демо-доступы (только для dev-режима)

| Логин | Пароль | Роль |
| :--- | :--- | :--- |
| `admin` | `admin123` | Администратор |
| `ivan` | `user123` | Пользователь |
| `maria` | `user123` | Пользователь |

>️ **Важно:** Эти пользователи создаются автоматически только если запущен профиль `dev` или задана переменная `PROJECTHUB_SEED_ADMIN_PASSWORD`. В боевом режиме авто-создание админа отключено в целях безопасности (см. ниже).

**H2 Console:**
В режиме разработки доступна консоль базы данных: `http://localhost:8080/h2-console`.
*   JDBC URL: `jdbc:h2:mem:projecthub`
*   User: `sa`
*   Password: (пусто)

### Безопасность и первый запуск на сервере

Чтобы не оставлять дыру с паролем `admin/admin123`, в проде сидинг отключен.
Как создать первого админа после деплоя (Render/VPS и т.д.):

1.  В настройках окружения (Environment Variables) задайте:
   *   `PROJECTHUB_SEED_ENABLED=true`
   *   `PROJECTHUB_SEED_ADMIN_PASSWORD=<придумайте_сложный_пароль>`
2.  Перезапустите приложение. В логах увидите сообщение об создании админа.
3.  Зайдите под новым логином/паролем.
4.  **Обязательно** удалите эти переменные или установите `PROJECTHUB_SEED_ENABLED=false` и перезапустите сервис снова.

## Запуск с PostgreSQL

Если хочется проверить работу с реальной базой:

```bash
docker compose up -d        # Поднимает Postgres в контейнере
SPRING_PROFILES_ACTIVE=postgres ./mvnw spring-boot:run
```

Дефолтные креды для БД (можно менять через env vars):
*   URL: `jdbc:postgresql://localhost:5432/projecthub`
*   User/Pass: `projecthub` / `projecthub`

## Сборка и Docker

```bash
# Сборка JAR
./mvnw -B clean package
# Сборка Docker образа
docker build -t projecthub:0.1.0 .
```

## Тесты

```bash
# Только юнит-тесты
./mvnw -B test
# Интеграционные тесты + отчет JaCoCo
./mvnw -B verify
# Тесты с реальным Postgres через Testcontainers
TESTCONTAINERS=1 ./mvnw -B verify
```

*   `*Test.java` — юнит-тесты сервисов.
*   `*IT.java` — интеграционные тесты контроллеров и API.
*   `PostgresContainerIT` — проверка миграций и CRUD на реальном Postgres (запускается, если `TESTCONTAINERS=1`).

## Документация и архитектура

### Генерация JavaDoc
```bash
./mvnw -B javadoc:javadoc
# Результат в target/site/apidocs/index.html
```

### Структура проекта
```
com.example.projecthub
├── config/         # Настройки Security, OpenAPI
├── controller/     # Обработка запросов, Thymeleaf
├── service/        # Бизнес-логика, транзакции, проверки прав
├── repository/     # Интерфейсы Spring Data JPA
├── entity/         # JPA сущности (User, Project, Task...)
├── dto/            # DTO для форм и API
├── exception/      # Глобальная обработка ошибок
└── init/           # Сидинг данных при старте
```

### Схема связей в БД
*   **User** (1) ── (∞) **Project** (владелец)
*   **Project** (1) ── (∞) **Task**
*   **Task** (1) ── (∞) **Comment**
*   **User** (1) ── (∞) **Comment** (автор)
*   **User** (1) ── (∞) **Task** (исполнитель, опционально)

## Безопасность

Я постарался закрыть основные уязвимости:
*   **Пароли:** хешируются через BCrypt.
*   **Доступ:** строгий RBAC. Обычный юзер не увидит админские страницы даже если подберет URL.
*   **CSRF:** включен и защищен.
*   **XSS:** Thymeleaf экранирует вывод по умолчанию (`th:text`).
*   **Rate Limiting:** защита от брутфорса на логине и регистрации (Bucket4j).
*   **Заголовки:** настроены HSTS, X-Frame-Options и другие security-headers.

## Маршруты (Основные)

| Метод | URL | Что делает | Кто может |
| :--- | :--- | :--- | :--- |
| GET | `/` | Главная (редирект) | Все |
| GET/POST | `/login` | Вход | Все |
| GET/POST | `/register` | Регистрация | Все |
| GET | `/projects` | Список проектов | USER, ADMIN |
| POST | `/projects/new` | Создать проект | USER, ADMIN |
| GET | `/projects/{id}` | Просмотр проекта | Владелец, ADMIN |
| POST | `/tasks/{id}/status` | Сменить статус задачи | Владелец, Исполнитель, ADMIN |
| GET | `/admin/stats` | Статистика | Только ADMIN |
| GET | `/swagger-ui.html` | Документация API | Все |
| GET | `/monitoring` | JavaMelody | Только ADMIN |

Полный список REST эндпоинтов (`/api/v1/...`) доступен в Swagger UI.


## Полезные ссылки

В репозитории есть подробная документация:
*   [`WHATS-DONE.md`](WHATS-DONE.md) — всё, что было сделано (с указанием классов).
*   [`HOW-IT-WORKS.md`](HOW-IT-WORKS.md) — как устроено приложение изнутри.
*   [`TZ-COMPLIANCE.md`](TZ-COMPLIANCE.md) — таблица соответствия техническому заданию.
*   [`DEPLOYMENT.md`](DEPLOYMENT.md) — гайд по деплою на Render, Railway, VPS.

Оригинальные ТЗ лежат в папке `docs/`.

