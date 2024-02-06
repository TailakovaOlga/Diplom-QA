package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;
import ru.netology.page.PayPage;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {
    PayPage payPage;
    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    void setUp() {
        payPage = open("http://localhost:8080", PayPage.class); //перед каждым тестом открывается страница и ей присваивается значение loginPage
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("Переход к форме заполнения данных карты нажатием кнопки 'Купить в кредит'")
    void shouldOpenFormByButtonPayCredit() {
        payPage.openFormToPayCredit();
    }

    @Test
    @DisplayName("2 Отправка запроса на покупку тура «Путешествие дня» при валидном значении заполненных полей формы 'Купить в кредит' по разрешенной банковской карте")
    void shouldSendRequestWithValidDataByCreditForm() {
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
    @DisplayName("4  Отказ в кредите на покупку тура «Путешествие дня» при валидном значении заполненных полей формы «Купить в кредит» по запрещенной карте")
    void sendRequestWithDeclinedCardInPayCreditForm() {
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
    @DisplayName("6 Отказ в оплате тура «Путешествие дня» в кредит при валидном значении заполненных полей формы  «Купить в кредит» по валидной карте номера которой нет в базе (запрещенная - 1111 1111 1111 1111 1111)")
    void sendRequestWithProhibitiondCardNumberInPayCreditForm() {
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
    @DisplayName("8 Отказ в оплате тура «Путешествие дня» при покупке в кредит при введении данных банковской карты просроченной на месяц. Заполняем все поля кроме поля «Месяц» валидными данными, в поле «Месяц» вводим прошедший месяц")
    void sendRequestWithExpiredDateInFieldМщтерInPayCreditForm() {
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
    @DisplayName("10 Отказ в оплате тура «Путешествие дня» при покупке в кредит при введении данных банковской карты просроченной на год. Заполняем все поля кроме поля «Год» валидными данными, в поле «Год» вводим прошедший год")
    void sendRequestWithExpiredDateInFieldYearInPayCreditForm() {
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
    @DisplayName("14  Оставление поля «Номер карты» пустым, при валидном значении заполненных полей формы «Купить в кредит»,остальные поля заполнены валидными значениями при покупке в кредит тура «Путешествие дня»")
    void sendRequestWithEmptyFieldCardNumberInPayCreditForm() {
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
    @DisplayName("16 Заполнение поля «Номер карты» 15 рандомными цифрами, при валидном значении заполненных полей формы «Купить в кредит», тура «Путешествие дня»")
    void sendRequestWithFieldCardNumberLess15SymbolInCreditForm() {
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
    @DisplayName("18 Заполнение поля «Номер карты» 17 рандомными цифрами, при валидном значении заполненных полей формы «Купить в кредит», тура «Путешествие дня»")
    void sendRequestWithFieldCardNumberLess17SymbolInCreditForm() {
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
    @DisplayName("20 Заполнение поля «Номер карты» 16 знаками (буквами кириллицы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Купить в кредит»  тура («Путешествие дня»")
    void sendRequestWithInvalidLetterCardNumberInCreditPayForm() {
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
    @DisplayName("22 Заполнение поля «Номер карты» 16 рандомными цифрами, остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Купить в кредит»  тура («Путешествие дня»")
    void sendRequestWithInvalidRandomNumderCardNumberInCreditPayForm() {
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
    @DisplayName("24 Заполнение поля «Номер карты» 16 знаками (буквами латиницы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Купить в кредит»  тура («Путешествие дня»")
    void sendRequestWithInvalidLatinLetterCardNumberInCreditPayForm() {
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
    @DisplayName("26 Заполнение поля «Номер карты» 16 знаками (спецсимволы), остальные поля заполнены валидными остальные поля заполнены валидными значениями в форме «Купить в кредит»  тура («Путешествие дня»")
    void sendRequestWithInvalidSpecialSimbolCardNumberInCreditPayForm() {
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
    @DisplayName("28 Оставление поля «Месяц» пустым, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldMonthInPayCreditForm() {
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
    @DisplayName("30 Заполнение поля «Месяц» значением 00, остальные поля заполнены валиднными значениями полей  в форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithMonth00InPayCreditForm() {
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
    @DisplayName("32 Заполнение поля «Месяц» значением 13, остальные поля заполнены валидными значениями полей  в форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithMonth13InPayCreditForm() {
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
    @DisplayName("34 Заполнение поля «Месяц» любым символами(спецсимволом), остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithInvalidSymbolInFieldMonthInPayCreditForm() {
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
    @DisplayName("36 Оставление поля «Год» пустым, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldYearInPayCreditForm() {
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
    @DisplayName("38 Оставление поля «Владелец» пустым, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldHolderInPayCreditForm() {
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
    @DisplayName("41 Заполнение поля «Владелец» нижнем регистре, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithFieldHolderLowerRegisterInPayCreditForm() {
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
    @DisplayName("43 Заполнение поля «Владелец» буквами (кириллицей), остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithFieldHolderInvalidLitersInPayCreditForm() {
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
    @DisplayName("45 Заполнение поля «Владелец» спецсимволами, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithFieldHolderSpecialSymbolsInPayCreditForm() {
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
    @DisplayName("47 Оставление поля «CVC/CVV» пустым, остальные поля заполнены валидными значениями полей формы «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithEmptyFieldCVCCVVInPayCreditForm() {
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
    @DisplayName("49 Заполнение поля «CVC/CVV» 2 рандомными цифрами, остальные поля заполнены валидными значениями полей форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWith2SymbolInFieldCVCInPayCreditForm() {
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
    @DisplayName("51 Заполнение поля «CVC/CVV» 4 рандомными цифрами, остальные поля заполнены валидными значениями полей форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWith4SymbolInFieldCVCInPayCreditForm() {
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
    @DisplayName("53 Заполнение поля «CVC/CVV» 3 рандомными буквами (кириллицей),  остальные поля заполнены валидными значениями полей в форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithInvalidLitersInFieldCVCInPayCreditForm() {
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
    @DisplayName("55 Заполнение поля «CVC/CVV» 3 рандомными буквами (латиницей),  остальные поля заполнены валидными значениями полей в форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithInvalidLatinLitersInFieldCVCInPayCreditForm() {
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
    @DisplayName("57 Заполнение поля «CVC/CVV» 3 спецсимволами,  остальные поля заполнены валидными значениями полей в форме «Купить в кредит» тура «Путешествие дня»")
    void sendRequestWithInvalidSpecialSymbolsInFieldCVCInPayCreditForm() {
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


