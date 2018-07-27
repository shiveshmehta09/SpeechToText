package com.mehta.shivesh.speechtotext.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mehta.shivesh.speechtotext.db.AppDb;
import com.mehta.shivesh.speechtotext.db.dao.SpeechDao;
import com.mehta.shivesh.speechtotext.network.ApiInterface;
import com.mehta.shivesh.speechtotext.repo.AppRepository;
import com.mehta.shivesh.speechtotext.utils.AppConstants;
import com.mehta.shivesh.speechtotext.utils.AppExecutor;
import com.mehta.shivesh.speechtotext.utils.MainThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    AppRepository provideAppRepository(ApiInterface apiInterface, SpeechDao dao, AppExecutor executor) {
        return new AppRepository(apiInterface, dao, executor);
    }

    @Provides
    AppExecutor provideExecutor() {
        return new AppExecutor(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    @Provides
    @Singleton
    SpeechDao provideDao(AppDb db) {
        return db.speechDao();
    }

    @Provides
    @Singleton
    AppDb provideDb(Application application) {
        return Room.databaseBuilder(application, AppDb.class, AppConstants.DATABASE)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }


    @Provides
    GsonConverterFactory provideGsonConvertorFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    Gson provide() {
        return new GsonBuilder().create();
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
