
Подключение SUT к PostgreSQL

1. Запустить Docker 
2. Открыть проект в IntelliJ IDEA
3. Запустить контейнеры:
   docker-compose up -d
4. Запустить приложение:

  java -Dspring.datasource.url=jdbc:postgresql://185.119.57.176:5432/app -jar artifacts/aqa-shop.jar
  
5. Открыть второй терминал
6. Запустить тесты:
   ./gradlew clean test -DdbUrl=jdbc:postgresql://185.119.57.176:5432/app
7. Создать отчёт Allure и открыть в браузере
   ./gradlew allureServe
8. Закрыть отчёт:
   CTRL + C -> y -> Enter
9. Перейти в  терминал
10. Остановить приложение:
   CTRL + C (или закрыть окно терминала)
11. Остановить контейнеры:
   docker-compose down
   
Подключение SUT к MySQL

1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале в корне проекта запустить контейнеры:
   docker-compose up -d
4. Запустить приложение:
   
  java -Dspring.datasource.url=jdbc:mysql://185.119.57.176:3306/app -jar artifacts/aqa-shop.jar
 
5. Открыть второй терминал
6. Запустить тесты:
   ./gradlew clean test -DdbUrl=jdbc:mysql://185.119.57.176:3306/app
7. Создать отчёт Allure и открыть в браузере
   ./gradlew allureServe
8. Закрыть отчёт:
   CTRL + C -> y -> Enter
9. Перейти в первый терминал
10. Остановить приложение:
   CTRL + C (или закрыть окно терминала)
11. Остановить контейнеры:
   docker-compose down
   
db.url=jdbc:mysql://185.119.57.176:3306/app или db.url=jdbc:postgresql://185.119.57.176:5432/app -- адрес и тип тестовой базы данных. Должен совпадать с адресом базы SUT. Обязательный.
db.user -- имя пользователя базы данных.  По-умолчанию 'app'
db.pass -- пароль пользователя базы данных.  По-умолчанию 'pass'
test.host -- адрес тестируемого хоста. 

