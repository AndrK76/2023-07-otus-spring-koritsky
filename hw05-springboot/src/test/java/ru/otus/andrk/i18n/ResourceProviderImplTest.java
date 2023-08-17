package ru.otus.andrk.i18n;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.service.i18n.LocaleProvider;
import ru.otus.andrk.service.i18n.ResourceProvider;
import ru.otus.andrk.service.i18n.ResourceProviderImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ResourceProviderImpl.class})
@TestPropertySource("classpath:test.properties")
public class ResourceProviderImplTest {

    @Autowired
    private ResourceProvider resourceProvider;

    @MockBean
    private QuestionsDaoCsvConfig questionsDaoCsvConfig;

    @MockBean
    private LocaleProvider localeProvider;

    @Value("${test-system.questions.resource-folder}")
    private String resourceFolder;

    @Value("${test-system.questions.resource-file-name}")
    private String resourceName;


    @BeforeEach
    void initCsvConfig(){
        when(questionsDaoCsvConfig.getResourceFolder()).thenReturn(resourceFolder);
        when(questionsDaoCsvConfig.getResourceName()).thenReturn(resourceName);
    }


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
        String actualResult = resourceProvider.getResourcePath(prefix, postfix);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

}
