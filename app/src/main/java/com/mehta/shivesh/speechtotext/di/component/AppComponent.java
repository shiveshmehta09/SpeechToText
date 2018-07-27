package com.mehta.shivesh.speechtotext.di.component;

import android.app.Application;

import com.mehta.shivesh.speechtotext.application.SpeechToTextApp;
import com.mehta.shivesh.speechtotext.di.module.ActivityModule;
import com.mehta.shivesh.speechtotext.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ActivityModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(SpeechToTextApp app);
}
