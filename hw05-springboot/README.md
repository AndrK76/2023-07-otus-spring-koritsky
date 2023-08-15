# Localized Tester on Spring Boot

### Примечания
1. Поигрался с AnsiColor, но по умолчанию в Win10 для консоли (и cmd и pwsh)
это отключено, сделать не проблема [github](https://github.com/spring-projects/spring-boot/issues/9063)
(с Ansi Color на терминале давно знаком, даже следы в интернете остались --> 
[архив sql.ru](https://murcode.ru/forum/3-oracle/188647-oracle-terminal-vopros/))
2. В локализации попробовал сделать именно иерархию, вначале язык, потом при необходимости
территория, поэтому такие "интересные" локали получились. 
3. Убрал setter из QuestionsDaoCsvConfig, добавлять ради тестов - лишнее, обошёл через mock

