package ru.netology.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Value;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public DataGenerator() {
        }

        @Getter
        private final String approvednumberCard = "4444444444444441";
        @Getter
        private String declinednumberCard = "4444444444444442";
        @Getter
        private final String approved = "APPROVED";
        private String declined = "DECLINED";
        @Getter
        private String month00 = "00";
        @Getter
        private String empty = "";
        @Getter
        private String messageOk = "Операция одобрена Банком.";
        @Getter
        private String messageNOk = "Ошибка! Банк отказал в проведении операции.";
        @Getter
        private String errorEmpty = "Неверный формат";
        @Getter
        private String errorEmptyHolder = "Поле обязательно для заполнения";
        @Getter
        private String errorMonth = "Неверно указан срок действия карты";
        @Getter
        private String errorExpiredYear = "Истёк срок действия карты";
        @Getter
        private String errorNotExistYear = "Неверно указан срок действия карты";


        // генерация значений для поля "Номер карты".

        public static String generateCardWith16Symbol() {
            int first = new Random().ints(1_000, 9999).findFirst().getAsInt();
            int second = new Random().ints(1_000, 9999).findFirst().getAsInt();
            int third = new Random().ints(1_000, 9999).findFirst().getAsInt();
            int fourth = new Random().ints(1_000, 9999).findFirst().getAsInt();
            return String.valueOf(first) + String.valueOf(second) + String.valueOf(third) + String.valueOf(fourth);
        }

        public static String generateCardWith17SymbolAtBeginning(String approvednumberCard) {
            String firstSymbol = String.valueOf(new Random().nextInt(10));
            return firstSymbol + approvednumberCard;
        }

        public static String generateCardWith17SymbolInEnd(String approvednumberCard) {
            String lastSymbol = String.valueOf(new Random().nextInt(10));
            return approvednumberCard + lastSymbol;
        }

        public static List<String> generateStringWithInvalidSymbol() {
            String letters = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
            String specialSymbols = "$&@?<>~!%#";
            String allSymbols = specialSymbols + letters;
            String[] arraySymbols = allSymbols.split("");
            List<String> list = new ArrayList<>(List.of(arraySymbols));
            Collections.shuffle(list);
            return list;
        }

        public static String generateCardWith16InvalidSymbol(List<String> list) {
            return (String.join("", list)).substring(0, 16);
        }

        public static String generateCardLess16Symbol() {
            String cardNumber16Symbol = new Faker().finance().creditCard(CreditCardType.VISA);
            int lenghtCard = new Random().nextInt(16);
            return cardNumber16Symbol.substring(0, lenghtCard);
        }

        // генерация значений для полей "Месяц" и "Год".

        public static String generateValidDateCard() {
            int plusMonths = new Random().nextInt(72);
            return LocalDateTime.now().plusMonths(plusMonths).format(DateTimeFormatter.ofPattern("MM.yy"));
        }

        public static String generateDateExpiredCard() {
            int minusMonths = new Random().nextInt(100) + 1;
            return LocalDateTime.now().minusMonths(minusMonths).format(DateTimeFormatter.ofPattern("MM.yy"));
        }

        public static String generateCardWithNotExistDate() {
            int months = new Random().ints(73, 900).findFirst().getAsInt();
            return LocalDateTime.now().plusMonths(months).format(DateTimeFormatter.ofPattern("MM.yy"));
        }

        public static String getMonthCard(String date) {
            return date.substring(0, 2);
        }

        public static String getYearCard(String date) {
            return date.substring(3, 5);
        }

        public static String generateYearCard4Symbol() {
            String yearCard = String.valueOf(LocalDateTime.now().plusMonths(12).format(DateTimeFormatter.ofPattern("MM.yyyy")));
            return yearCard.substring(3, 7);
        }

        public static String generateMonthOrYear1Symbol() {
            return String.valueOf(new Random().nextInt(9) + 1);
        }

        public static String generateMonthMore12() {
            return String.valueOf(new Random().ints(13, 100));
        }

        public static String generateMonthOrYearWithInvalidSymbol(List<String> list) {
            return (String.join("", list)).substring(0, 2);
        }


        // генерация значений для поля "Владелец".

        public static String generateNameOfHolderCard() {
            String name = new Faker().name().fullName();
            String symbol = String.valueOf(new Random().nextInt(1000));
            return name + " " + symbol;
        }

        // генерация значений для поля "CVC/CVV".

        public static String generateValidCVC() {
            return String.valueOf(new Random().nextInt(900) + 100);
        }

        public static String generateCVCWith2Symbol() {
            return String.valueOf(new Random().nextInt(100));
        }

        public static String generateCVCWithInvalidSymbol(List<String> list) {
            return (String.join("", list)).substring(0, 3);
        }

        @Value
        public static class Date {
            String number;
            String month;
            String year;
            String holder;
            String cvc;
        }

        public static Date generateAllEmptyField() {
            return new Date("", "", "", "", "");
        }

        public static Date generateValidField(String approvednumberCard) {
            return new Date(approvednumberCard, getMonthCard(generateValidDateCard()), getYearCard(generateValidDateCard()), generateNameOfHolderCard(), generateValidCVC());
        }
    }

