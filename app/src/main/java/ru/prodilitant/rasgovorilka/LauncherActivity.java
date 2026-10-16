/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ----------------------------------------------------------------------------
 * Modifications Copyright (C) 2026 Виталий (prodilitant-dev)
 *
 * Этот файл был модифицирован для проекта РАСговорилка:
 *   - добавлен immersive mode (скрытие системных панелей)
 *   - добавлен FLAG_KEEP_SCREEN_ON
 *   - переопределены onResume и onWindowFocusChanged
 *
 * Модификации распространяются под GNU General Public License v3.0 or later.
 * См. LICENSE в корне проекта.
 * ----------------------------------------------------------------------------
 */
package ru.prodilitant.rasgovorilka;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

public class LauncherActivity
        extends com.google.androidbrowserhelper.trusted.LauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Не даём экрану гаснуть, пока приложение на переднем плане.
        // Полезно для AAC-приложений: ребёнок может долго думать перед выбором карточки,
        // и внезапное гашение экрана сбивает.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Setting an orientation crashes the app due to the transparent background on Android 8.0
        // Oreo and below. We only set the orientation on Oreo and above. This only affects the
        // splash screen and Chrome will still respect the orientation.
        // See https://github.com/GoogleChromeLabs/bubblewrap/issues/496 for details.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        enableImmersiveMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // После возврата из свёрнутого состояния (или из другого приложения)
        // система могла показать статус-бар и навигацию — переприменяем immersive.
        enableImmersiveMode();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Когда окно получает фокус — снова прячем системные панели.
        if (hasFocus) {
            enableImmersiveMode();
        }
    }

    /**
     * Полное скрытие системных панелей (статус-бар + навигация).
     * Панели можно вызвать свайпом от края экрана — они появятся на пару секунд
     * и уйдут автоматически (BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE).
     *
     * Для Android 11+ (API 30+) используется новый WindowInsetsController.
     * Для Android 7-10 (API 24-29) — устаревшие флаги SYSTEM_UI_FLAG_IMMERSIVE_STICKY.
     */
    private void enableImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30+)
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.systemBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // Android 7-10 (API 24-29)
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected Uri getLaunchingUrl() {
        // Get the original launch Url.
        Uri uri = super.getLaunchingUrl();

        return uri;
    }
}