package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;
import ru.netology.page.PayPage;

import static com.codeborne.selenide.AssertionMode.SOFT;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLTest {
    PayPage payPage = new PayPage();
    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    void setUp() {
        payPage = open("http://localhost:8080", PayPage.class); //перед каждым тестом открывается страница и ей присваивается значение loginPage
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("1 Успешная оплата тура «Путешествие дня» при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте» по разрешенной банковской карте (номер карты заполнен с пробелами после каждых 4 символов)(переход к форме через кнопку 'Купить' и получение ответа из базы SQL")
    void shouldConfirmPayWithApprovedCard() {
        Configuration.assertionMode = SOFT;
        payPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageOk = dataGenerator.getMessageOk();
        payPage.fillFormWithValidData(number, month, year, holder, cvc, messageOk);
        String actual = SQLHelper.getStatusFromPayment();
        String expected = dataGenerator.getApproved();
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("2 Успешная оплата в кредит тура «Путешествие дня» при валидном значении заполненных полей формы «Купить в кредит» по разрешенной банковской карте (номер карты заполнен с пробелами после каждых 4 символов)(переход к форме через кнопку 'Купить в кредит' и  получение ответа из базы SQL")
    void shouldConfirmPayWithApprovedCreditCard() {
        Configuration.assertionMode = SOFT;
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageOk = dataGenerator.getMessageOk();
        payPage.fillFormWithValidData(number, month, year, holder, cvc, messageOk);
        String actual = SQLHelper.getStatusFromPaymentCredit();
        String expected = dataGenerator.getApproved();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("3 Отказ в оплате тура «Путешествие дня» при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте» по запрещенной карте  (переход к форме через кнопку 'Купить' и получение ответа из базы SQL")
    void shouldConfirmPayWithDeclinedCard() {
        Configuration.assertionMode = SOFT;
        payPage.openFormToPay();
        var number = dataGenerator.getDeclinednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = dataGenerator.getMessageNOk();
        payPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
        Long actual = SQLHelper.getNumberRowsFromDBTablePayment();
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("4 Отказ в кредите на покупку тура «Путешествие дня» при валидном значении заполненных полей формы «Купить в кредит» по запрещенной карте (номер карты заполнен с пробелами после каждых 4 символов).  (Переход к форме через кнопку 'Купить в кредит' и получение ответа из базы SQL")
    void shouldConfirmPayWithDeclinedCreditCard() {
        Configuration.assertionMode = SOFT;
        payPage.openFormToPayCredit();
        var number = dataGenerator.getDeclinednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = dataGenerator.getMessageNOk();
        payPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
        Long actual = SQLHelper.getNumberRowsFromDBTablePaymentCredit();
        assertEquals(0, actual);
    }
}