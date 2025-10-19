package de.dmitrij.patuk.ourapp.config;

import android.content.Context;

import net.openid.appauth.AuthorizationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import de.dmitrij.patuk.ourapp.services.AuthService;
import de.dmitrij.patuk.ourapp.services.AuthStateManager;

@Module
@InstallIn(SingletonComponent.class)
public class AppModuleConfig {
    @Provides
    @Singleton
    public AuthStateManager provideAuthStateManager(@ApplicationContext Context context) {
        return AuthStateManager.getInstance(context);
    }

    @Provides
    @Singleton
    public AuthorizationService provideAuthorizationService(@ApplicationContext Context context) {
        return new AuthorizationService(context);
    }

    @Provides
    @Singleton
    public AuthService provideAuthService(AuthorizationService authorizationService,
                                          AuthStateManager authStateManager){
        return new AuthService(authorizationService,authStateManager);
    }
}
