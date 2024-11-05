package ru.topacademy.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.topacademy.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ChangeDateTest {
    DataGenerator DataGenerator = new DataGenerator();

    String city = DataGenerator.genCity();
    String name = DataGenerator.genName();
    String phone = DataGenerator.genPhone();
    String planDate = DataGenerator.genDate(3, "dd.MM.yyyy");
    String changeDate = DataGenerator.genDate(7, "dd.MM.yyyy");

    @BeforeEach
    public void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        open("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    @DisplayName("1.Проверка успешной отправки формы с валидными данными и изменением даты встречи")
    public void sendForm() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(20));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + planDate), Duration.ofSeconds(15));
        $(".calendar-input__custom-control input").doubleClick().sendKeys(changeDate);
        $(".button").click();
        $("[data-test-id=replan-notification] .notification__title").shouldHave(exactText("Необходимо подтверждение"), Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button").click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + changeDate));
    }

    @Test
    @DisplayName("2.Проверка валидации поля города с неверным названием города")
    public void validCity() {
        $("[data-test-id=city] input").setValue("Берёзовский");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("3.Проверка валидации поля города, когда оно пустое")
    public void noCity() {
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("4.Проверка валидации поля даты, когда оно пустое")
    public void noDate() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    @DisplayName("5.Проверка валидации поля имени с неверным именем содержащим не русские символы")
    public void validName() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue("Gogenov-Jozef");
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("6.Проверка валидации поля имени, когда оно пустое")
    public void noName() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("7.Проверка валидации поля телефона с неверным номером телефона")
    public void validPhone() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue("+7999900443");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно"),Duration.ofSeconds(20));
    }

    @Test
    @DisplayName("8.Проверка валидации поля телефона, когда оно пустое")
    public void noPhone() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("9.Проверка валидации, когда флажок согласия не отмечен")
    public void noCheckBox() {
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}