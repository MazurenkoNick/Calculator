# Calculator
Розробити застосування, яке допомагатиме вчителю математики.
Застосування повинне надавати такі можливості:
1. Вводити математичні рівняння, що містять числа (цілі, або десяткові дроби), а 
також математичні операції +, -, *, / та круглі дужки, рівень вкладеності дужок –
довільний. У всіх рівняннях невідома величина позначається англійською літерою
x.
2. Перевіряти введене рівняння на коректність розміщення дужок
3. Перевіряти коректність введеного виразу (не повинно бути 2 знаків 
математичних операцій поспіль, наприклад, неприпустимий вираз 3+•4, в той 
же час, вираз 4•-7 є допустимим).
Приклади коректних рівнянь: 
2•x+5=17, -1.3•5/x=1.2, 2•x=10, 2•x+5+х+5=10, 17=2•x+5
4. Якщо рівняння є коректним, зберегти його у БД.
5. Надати можливість ввести корені рівняння, під час введення перевіряти, чи є 
задане число коренем, і якщо так – зберігати його в БД.
6. Реалізувати функції пошуку рівнянь у БД за їхніми коренями. Наприклад, 
можливий запит: знайти всі рівняння, що мають один із зазначених коренів або 
знайти всі рівняння, які мають рівно один корінь.
7. Проект має бути реалізований з використанням системи збирання Maven в 
одному із середовищ розробки: IntelliJ IDEA або Eclipse.
8. Проект має бути завантажений у репозиторій GitHub та надано посилання для 
його отримання. Також допустимо надіслати архів із проектом.
---
1. (1) Було створено контролер, методи якого приймають на вхід математичний вираз (`MathExpression` entity).
   У ньому створені допоміжні методи: `getFormattedExpression()`, який форматує вираз, прибираючи зайві символи;
   та `isEquation()`, який перевіряє, чи є вираз рівнянням.

2. (2, 3) Перевірка на валідність виразу здійснюється в першу чергу на рівні контроллера за допомогою анотації `@Valid`.
   Перед цим у класі `MathExpression` було розставлено потрібні анотації та створена власна: `@MathematicalExpression`
   ```java
     @MathematicalExpression
     private String expression;
   ```
   Для цієї анотації було створено валідатор `@Constraint(validatedBy = MathExpressionValidator.class)`.
   Його публічний метод `public boolean isValid(String expression, ConstraintValidatorContext context)`
   гарантує коректність розміщення дужок та порядку розставлення операторів за допомогою приватних методів
   `boolean validParenthesesSymmetry(String expression)` та `boolean validOperatorOrder(String expression)`.
   
3. (4) У контролері було створено метод `saveMathExpression(@Valid @RequestBody MathExpression expression)`, який валідує
   вираз і потім викликається метод сервісного класу `MathExpressionService#save(MathExpression expression)`,
   який форматує вираз та перевіряє, чи не існує такого ж самого в базі даних. Якщо даний вираз вже існує, то такий самий
   вираз не буде створено. Якщо ні, то новий вираз буде збережено.
   Приклад запиту:
   ```
   POST http://localhost:8080/expression/expression
   Content-Type: application/json

   {
     "expression": "52+-3*4/2+(2-3)*2"
   }
   ```
   
   Також було створено метод `MathExpressionController#calculateAndSave(@Valid @RequestBody MathExpression expression)`,
   який також валідує вираз та відправляє на виконання у `MathExpressionService#calculateAndSave(MathExpression expression)`.
   Там проводиться перевірка, чи не є вираз рівнянням, щоб його можна було розв'язати. Потім, вираз форматується та йде
   на виконання у `CalculatorService#calculate(MathExpression expression)`. Вираз конвертується в RPN (Reverse Polish Notation)
   за допомогою ReversePolishNotation#convertToRpn(MathExpression mathExpression), задля подальших обчислень.
   Після, RPN обчислюється за допомогою метода `calculateRpnExpression#(String expression)` та повертається результат обчислень.
   Після всіх обрахунків, у методі `MathExpressionService#calculateAndSave` перевіряється, чи не існує такий самий вираз у базі даних.
   Якщо існує, то нова відповідь обрахнку буде додана (якщо у минулому її не було). Якщо ні, то буде створено новий вираз у базі даних.
   Приклад запиту:
   ```
   POST http://localhost:8080/expression/calculate
   Content-Type: application/json

   {
     "expression": "2+3*(40-23*(5+2))"
   }
   ```
   
4. (5) Для цього було створено метод в контролері `checkEquationAndSave(@Valid @RequestBody MathExpression equation)`. Приклад запиту:
   ```
   POST http://localhost:8080/expression/checkEquation
   Content-Type: application/json

   {
     "expression": "5x+3=8",
     "answers": [1, 3, 5]
   }
   ```
   `answers` - корені рівняння. Для збереження рівняння в базі даних, у методі `MathExpressionService#checkRootsOfEquationAndSave`
   виконується ітерація кожного кореня та робиться перевірка його правильності відносно рівняння за допомогою методу
   `CalculatorService#checkRootOfEquation`, де рівняння розбивається на дві частини, з кожної сторони створюється RPN та
   відбувається їх розрахунок. Якщо різниця розрахунків <= 0.1E-9, то корінь вважається правильним. Якщо хоча б один з коренів є
   неправильним, то рівняння не буде збережено.
5. (6) У контролері було реалізовано пошук `MathExpression`: за самим виразом та за відповідями - 
   `MathExpressionController#getMathExpression(@RequestBody String expression)` та
   `MathExpressionController#getMathExpressions(@RequestBody List<Double> answers)` відповідно.
   Приклади запитів:
   ```
   GET http://localhost:8080/expression/searchByAnswers
   Content-Type: application/json

   [-361, 2]
   ```
   ```
   GET http://localhost:8080/expression/searchByExpression
   Content-Type: application/json

   52+-3*4/2+(2-3)*2
   ```
