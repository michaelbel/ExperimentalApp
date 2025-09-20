<div align="center">
    <img src=".github/pics/cover.png" alt="Cover">
</div>

[![](https://img.shields.io/badge/Пост_в_Telegram-24A1DE?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/foundout/291)
[![](https://img.shields.io/badge/Пост_на_Boosty-F15F2C?style=for-the-badge&logo=boosty&logoColor=F6F6F6)](https://boosty.to/michaelbel/posts/9efececa-2e78-40e8-bd74-4f381c55983a)
[![](https://img.shields.io/badge/Видео_на_YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/eGKcebZcBmo)


Plurals
=

[![workflow-status](https://img.shields.io/github/actions/workflow/status/michaelbel/previewseekbar/ci.yml?style=for-the-badge&logo=github&labelColor=3F464F)](https://github.com/michaelbel/previewseekbar/actions)
[![last-commit](https://img.shields.io/github/last-commit/michaelbel/previewseekbar?style=for-the-badge&logo=github&labelColor=3F464F)](https://github.com/michaelbel/previewseekbar/commits)

Plurals – это строковый ресурс, который подставляет правильную словоформу в тексте с числом. Набор форм зависит от языка: где-то хватает «one» и «other», а в сложных локалях есть «few», «many», иногда «zero» и «two». Система сама решает, какую форму подставить: «1 день», «2 дня», «5 дней». Благодаря этому интерфейс звучит естественно на любом языке, без ручной склейки строк и поломки грамматики. Выбор используемой строки осуществляется на основе грамматической необходимости. Правила могут быть сложными для определенного языка и количества. Поэтому Android предоставляет методы для выбора подходящего ресурса. Чаще всего разработчики обманываются буквальной трактовкой форм: zero не означает только число 0, а two не подходит только для числа 2. Если не опираться на plurals, получить верную фразу будет невозможно.

<img src=".github/pics/app.gif" alt="App Video" width="33%">