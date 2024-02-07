package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;
import ru.netology.page.PayPage;
import io.qameta.allure.selenide.AllureSelenide;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

public class CashTest {
    PayPage payPage;
    DataGenerator dataGenerator = new DataGenerator();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        payPage = open("http://localhost:8080", PayPage.class); //перед каждым тестом открывается страница и ей присваивается значение loginPage
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("1 Отправка запроса на покупку тура «Путешествие дня» при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте» по разрешенной банковской карте")
    void shouldSendRequestWithValidDataByPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageOk = dataGenerator.getMessageOk();
        payPage.fillFormWithValidData(number, month, year, holder, cvc, messageOk);
    }

    @Test
    @DisplayName("3 Отказ в  оплате тура «Путешествие дня» при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте» по запрещенной карте")
    void sendRequestWithDeclinedCardInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getDeclinednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = dataGenerator.getMessageNOk();
        payPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
    }

    @Test
    @DisplayName("5 Отказ в оплате тура «Путешествие дня» в кредит при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте» по валидной карте номера которой нет в базе (запрещенная - 1111 1111 1111 1111 1111)")
    void sendRequestWithProhibitiondCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = DataGenerator.generateCardWith17SymbolAtBeginning(dataGenerator.getApprovednumberCard());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = dataGenerator.getMessageNOk();
        payPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
    }

    @Test
    @DisplayName("7 Отказ в оплате тура «Путешествие дня» при покупке «Обычная оплата по дебетовой карте» при введении данных банковской карты просроченной на месяц. Заполняем все поля кроме поля «Месяц» валидными данными, в поле «Месяц» вводим прошедший месяц")
    void sendRequestWithExpiredDateInFieldМщтерInPayForm() {
        payPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateDateExpiredCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateDateExpiredCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorExpiredYear();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("9 Отказ в оплате тура «Путешествие дня» при покупке «Обычная оплата по дебетовой карте» при введении данных банковской карты просроченной на год. Заполняем все поля кроме поля «Год» валидными данными, в поле «Год» вводим прошедший год")
    void sendRequestWithExpiredDateInFieldYearInPayForm() {
        payPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateDateExpiredCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateDateExpiredCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorExpiredYear();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("13  Оставление поля «Номер карты» пустым, при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте»,остальные поля заполнены валидными значениями при покупке в кредит тура «Путешествие дня»")
    void sendRequestWithEmptyFieldCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getEmpty();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("15 Заполнение поля «Номер карты» 15 рандомными цифрами, при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте», тура «Путешествие дня»")
    void sendRequestWithFieldCardNumberLess15SymbolInPayForm() {
        payPage.openFormToPay();
        var number = DataGenerator.generateCardLess16Symbol();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("17 Заполнение поля «Номер карты» 17 рандомными цифрами, при валидном значении заполненных полей формы «Обычная оплата по дебетовой карте», тура «Путешествие дня»")
    void sendRequestWithFieldCardNumberLess17SymbolInPayForm() {
        payPage.openFormToPay();
        var number = DataGenerator.generateCardLess16Symbol();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("19 Заполнение поля «Номер карты» 16 знаками (буквами кириллицы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Обычная оплата по дебетовой карте»  тура («Путешествие дня»")
    void sendRequestWithInvalidLetterCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = DataGenerator.generateCardWith16InvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("21 Заполнение поля «Номер карты» 16 рандомными цифрами, остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Обычная оплата по дебетовой карте»  тура («Путешествие дня»")
    void sendRequestWithInvalidRandomNumderCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = DataGenerator.generateCardWith16InvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("23 Заполнение поля «Номер карты» 16 знаками (буквами латиницы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Обычная оплата по дебетовой карте»  тура («Путешествие дня»")
    void sendRequestWithInvalidLatinLetterCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = DataGenerator.generateCardWith16InvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("25 Заполнение поля «Номер карты» 16 знаками (спецсимволы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Обычная оплата по дебетовой карте»  тура («Путешествие дня»")
    void sendRequestWithInvalidSpecialSimbolCardNumberInPayForm() {
        payPage.openFormToPayCredit();
        var number = DataGenerator.generateCardWith16InvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("27 Оставление поля «Месяц» пустым, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldMonthInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = dataGenerator.getEmpty();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("29 Заполнение поля «Месяц» значением 00, остальные поля заполнены валиднными значениями полей  в форме «Обычная оплата по дебетовой карте»» тура «Путешествие дня»")
    void sendRequestWithMonth00InPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = dataGenerator.getMonth00();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorMonth();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("31 Заполнение поля «Месяц» значением 13, остальные поля заполнены валидными значениями полей  в форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithMonth13InPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.generateMonthMore12();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorMonth();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("33 Заполнение поля «Месяц» любым символами(спецсимволом), остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте»» тура «Путешествие дня»")
    void sendRequestWithInvalidSymbolInFieldMonthInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.generateMonthOrYearWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("35 Оставление поля «Год» пустым, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldYearInPayForm() {
        payPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = dataGenerator.getEmpty();
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("37 Оставление поля «Владелец» пустым, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldHolderInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = dataGenerator.getEmpty();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmptyHolder();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("39 Заполнение поля «Владелец» нижнем регистре, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithFieldHolderLowerRegisterInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = dataGenerator.getEmpty();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmptyHolder();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("41 Заполнение поля «Владелец» буквами (кириллицей), остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithFieldHolderInvalidLitersInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = dataGenerator.getEmpty();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmptyHolder();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("43 Заполнение поля «Владелец» спецсимволами, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте»» тура «Путешествие дня»")
    void sendRequestWithFieldHolderSpecialSymbolsInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = dataGenerator.getEmpty();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = dataGenerator.getErrorEmptyHolder();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("45 Оставление поля «CVC/CVV» пустым, остальные поля заполнены валидными значениями полей формы «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldCVCCVVInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = dataGenerator.getEmpty();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("47 Заполнение поля «CVC/CVV» 2 рандомными цифрами, остальные поля заполнены валидными значениями полей форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWith2SymbolInFieldCVCInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWith2Symbol();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("49 Заполнение поля «CVC/CVV» 4 рандомными цифрами, остальные поля заполнены валидными значениями полей форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWith4SymbolInFieldCVCInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWith2Symbol();
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("51 Заполнение поля «CVC/CVV» 3 рандомными буквами (кириллицей),  остальные поля заполнены валидными значениями полей в форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithInvalidLitersInFieldCVCInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("53 Заполнение поля «CVC/CVV» 3 рандомными буквами (латиницей),  остальные поля заполнены валидными значениями полей в форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithInvalidLatinLitersInFieldCVCInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("55 Заполнение поля «CVC/CVV» 3 спецсимволами,  остальные поля заполнены валидными значениями полей в форме «Обычная оплата по дебетовой карте» тура «Путешествие дня»")
    void sendRequestWithInvalidSpecialSymbolsInFieldCVCInPayForm() {
        payPage.openFormToPayCredit();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var messageError = dataGenerator.getErrorEmpty();
        payPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }
}

