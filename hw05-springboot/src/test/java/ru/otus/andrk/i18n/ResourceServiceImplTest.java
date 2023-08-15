package ru.otus.andrk.i18n;

import org.assertj.core.util.Strings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.service.i18n.LocaleProvider;
import ru.otus.andrk.service.i18n.ResourceService;
import ru.otus.andrk.service.i18n.ResourceServiceImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {QuestionsDaoCsvConfig.class, ResourceServiceImpl.class})
@TestPropertySource("classpath:test.properties")
public class ResourceServiceImplTest {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private QuestionsDaoCsvConfig questionsDaoCsvConfig;

    @MockBean
    private LocaleProvider localeProvider;


    void initLocaleProvider(String currentLocale) {
        when(localeProvider.getCurrent())
                .thenReturn(Locale.forLanguageTag(currentLocale));
    }

    @ParameterizedTest
    @CsvSource({"ru,ru", "en,en", "fr,", "en-US,en", "en-GB,en/GB", "ru-RU,ru"})
    public void givenLanguageCodeShouldResultCorrectAndExistResourceName(String locale, String expectedLangPath) {
        String prefix = questionsDaoCsvConfig.getResourceFolder();
        String postfix = questionsDaoCsvConfig.getResourceName();
        initLocaleProvider(locale);

        String expectedResult = prefix +
                (Strings.isNullOrEmpty(expectedLangPath) ? "" : "/" + expectedLangPath) +
                "/" + postfix;
        String actualResult = resourceService.getLocalizedResourceName(prefix, postfix);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

}
