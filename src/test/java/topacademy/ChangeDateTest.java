package topacademy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ChangeDateTest {
    DataGenerator dataGenerator = new DataGenerator();

    String city = DataGenerator.genCity();

    String name = DataGenerator.genName();

    String phone = DataGenerator.genPhone();

    String planDate = DataGenerator.genDate(3, "dd.MM.yyyy");

    String changeDate = DataGenerator.genDate(7, "dd.MM.yyyy");

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    public void sendForm() {
        // Проверка успешной отправки формы с валидными данными и изменением даты встречи
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
    public void validCity() {
        // Проверка валидации поля города с неверным названием города
        $("[data-test-id=city] input").setValue("Берёзовский");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void noCity() {
        // Проверка валидации поля города, когда оно пустое
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void noDate() {
        // Проверка валидации поля даты, когда оно пустое
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    public void validName() {
        // Проверка валидации поля имени с неверным именем (содержащим не русские символы)
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue("Gogenov-Jozef");
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void noName() {
        // Проверка валидации поля имени, когда оно пустое
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void validPhone() {
        // Проверка валидации поля телефона с неверным номером телефона
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue("+7999900443");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно"),Duration.ofSeconds(20));
    }

    @Test
    public void noPhone() {
        // Проверка валидации поля телефона, когда оно пустое
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void noCheckBox() {
        // Проверка валидации, когда флажок согласия не отмечен
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}