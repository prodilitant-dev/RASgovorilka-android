# РАСговорилка — Android (TWA)

Trusted Web Activity (TWA) обёртка для веб-приложения **РАСговорилка**. Позволяет установить коммуникатор на Android-устройство как полноценное нативное приложение.

* 🌐 **Веб-версия:** https://prodilitant-dev.github.io/RASgovorilka/
* 📦 **Скачать готовый APK:** [Releases](https://github.com/prodilitant-dev/RASgovorilka-android/releases)
* 📁 **Основной репозиторий:** [prodilitant-dev/RASgovorilka](https://github.com/prodilitant-dev/RASgovorilka)

---

## 📱 Установка

1. Перейдите в раздел **[Releases](https://github.com/prodilitant-dev/RASgovorilka-android/releases)** и скачайте актуальный файл `app-release-signed.apk`.
2. Разрешите в настройках Android установку приложений из неизвестных источников.
3. Установите и запустите APK.

*После установки приложение работает в том числе офлайн. При наличии сети данные и интерфейс обновляются автоматически (синхронно с веб-версией).*

---

## 🛠 Сборка (для разработки)

Репозиторий собран на базе [Bubblewrap](https://github.com/GoogleChromeLabs/bubblewrap). Вся основная логика находится в веб-приложении, здесь — только конфигурация обёртки и минимальный нативный код для полноэкранного режима.

### Требования

* Node.js 22 LTS (рекомендуется)
* Установленный CLI: `npm install -g @bubblewrap/cli`
* Настроенные JDK 17 и Android SDK (`bubblewrap doctor` для проверки)

### Клонирование и установка

```bash
git clone git@github.com:prodilitant-dev/RASgovorilka-android.git
cd RASgovorilka-android
```

### Сборка

```bash
bubblewrap build
```

На вопрос **«There are changes in twa-manifest.json. Would you like to apply them to the project before building?»** отвечайте **`n` (No)**. См. раздел «Кастомизация» ниже.

**На выходе:**
* `app-release-signed.apk` — готовый установочный файл.
* `app-release-bundle.aab` — сборка для Google Play (не используется).

⚠️ **Важно:** файл ключей подписи (`.keystore`) намеренно добавлен в `.gitignore` и не отслеживается. Для локальной сборки используйте свой ключ. При его отсутствии Bubblewrap предложит сгенерировать новый автоматически.

---

## 🎨 Кастомизация нативного кода

Проект содержит **ручные правки**, которых нет в стандартном шаблоне Bubblewrap. Они нужны для полноэкранного режима и корректной работы при сворачивании.

### Что кастомизировано

| Файл | Что изменено |
|---|---|
| `app/src/main/java/ru/prodilitant/rasgovorilka/LauncherActivity.java` | Immersive mode (скрытие статус-бара и навигации) через `WindowInsetsController` (Android 11+) и устаревшие флаги (Android 7–10). Повторное применение в `onResume` и `onWindowFocusChanged`. `FLAG_KEEP_SCREEN_ON` — экран не гаснет. |
| `app/src/main/res/values/styles.xml` | Тема `TwaTheme` с `windowLayoutInDisplayCutoutMode` для вырезов камеры. |
| `app/src/main/AndroidManifest.xml` | Тема приложения заменена на `@style/TwaTheme`. Добавлен `android:configChanges` для `LauncherActivity` — без него immersive слетает при повороте. |
| `app/build.gradle` | Версии `versionCode`/`versionName` синхронизированы с `twa-manifest.json`. |

### ⚠️ `bubblewrap update` использовать нельзя

`bubblewrap update` перезаписывает `AndroidManifest.xml`, `build.gradle` и другие файлы из шаблона. Все кастомизации будут потеряны.

**Правильный workflow:**
- ✅ `bubblewrap build` — для сборки APK.
- ❌ `bubblewrap update` — **никогда**.
- Ответ на «apply changes» — всегда **`n`**.
- Обновление версии — вручную в `twa-manifest.json` + `app/build.gradle`.

---

## ⚙️ Конфигурация

Ключевые параметры TWA-клиента задаются в файле `twa-manifest.json`:

* `packageId`: `ru.prodilitant.rasgovorilka`
* `host`: `prodilitant-dev.github.io`
* `startUrl`: `/RASgovorilka/`
* `display`: `fullscreen`
* `minSdkVersion`: `24` (Android 7.0+)

### Digital Asset Links

Для работы TWA без адресной строки Chrome необходим файл `assetlinks.json` в корне домена:
* **URL:** https://prodilitant-dev.github.io/.well-known/assetlinks.json
* **Репозиторий:** [prodilitant-dev/prodilitant-dev.github.io](https://github.com/prodilitant-dev/prodilitant-dev.github.io)

При смене ключа подписи (`.keystore`) необходимо обновить `sha256_cert_fingerprints` в `assetlinks.json`.

---

## Лицензия

Проект распространяется под **GNU General Public License v3.0 or later**.

Отдельные файлы (`Application.java`, `DelegationService.java`, `LauncherActivity.java`
и части `AndroidManifest.xml`, `styles.xml`) содержат код, сгенерированный
[Bubblewrap CLI](https://github.com/GoogleChromeLabs/bubblewrap) и распространяются
под **Apache License 2.0** (Copyright 2019 Google Inc.).

Модификации этих файлов — Copyright (C) 2026 Виталий (prodilitant-dev),
распространяются под **GPL-3.0-or-later**.

Совместимость: Apache 2.0 → GPL-3.0 (односторонняя).
Комбинированное произведение распространяется под GPL-3.0.

---

## 🤝 Обратная связь

* **Ошибки и предложения (Issues):** [prodilitant-dev/RASgovorilka/issues](https://github.com/prodilitant-dev/RASgovorilka/issues) *(активность ведется в основном репо)*
* **Telegram-канал:** [@RASgovorilka](https://t.me/RASgovorilka)
* **MAX-канал:** [@RASgovorilka](https://max.ru/channel_RASgovorilka)
* **Telegram (личные сообщения):** [@akvariumist2gr](https://t.me/akvariumist2gr)
* **Email:** prodilitant@gmail.com