# Tester with Spring Shell and springBoot tests

###Примечания
1. Поправлено [замечание](https://github.com/AndrK76/2023-07-otus-spring-koritsky/pull/3#discussion_r1299447175),
создана сущность Message, которая отвечает за сообщение пользователю
2. Теперь за общение с пользователем отвечает MessageService и реализации 
сервисов более верхнего уровня (WelcomeService, TesterService) ничего не знают
о вводе/выводе и локализации
3. Так же убран последний остававшийся @Lazy в MessageProviderLocalizedImpl
