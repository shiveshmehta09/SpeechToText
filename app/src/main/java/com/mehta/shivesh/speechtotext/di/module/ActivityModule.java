package com.mehta.shivesh.speechtotext.di.module;

import com.mehta.shivesh.speechtotext.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Shivesh
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}