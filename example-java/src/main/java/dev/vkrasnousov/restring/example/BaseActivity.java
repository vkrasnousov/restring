package dev.vkrasnousov.restring.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.ViewPumpAppCompatDelegate;

import dev.vkrasnousov.restring.Restring;

public abstract class BaseActivity extends AppCompatActivity {

    private AppCompatDelegate appCompatDelegate = null;

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (appCompatDelegate == null) {
            appCompatDelegate = new ViewPumpAppCompatDelegate(
                    super.getDelegate(),
                    this,
                    Restring::wrapContext
            );
        }
        return appCompatDelegate;
    }
}
