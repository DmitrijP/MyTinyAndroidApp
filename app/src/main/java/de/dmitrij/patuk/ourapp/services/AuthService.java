package de.dmitrij.patuk.ourapp.services;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import java.util.List;

import de.dmitrij.patuk.ourapp.config.MyConfig;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.SingleSubject;

public class AuthService {
    private final String TAG = AuthService.class.getSimpleName();

    private final AuthorizationService authorizationService;
    private final AuthStateManager authStateManager;

    private SingleSubject<String> currentAuthSubject;

    public AuthService(AuthorizationService authorizationService, AuthStateManager authStateManager) {
        this.authorizationService = authorizationService;
        this.authStateManager = authStateManager;
    }

    public Single<String> launchAuthorization(){
        currentAuthSubject = SingleSubject.create();
        return currentAuthSubject;
    }

    public Intent getAuthorizationIntent(){
        return authorizationService.getAuthorizationRequestIntent(getAuthRequest());
    }

    public void onActivityResult(Intent intent){
        Log.d(TAG, "Authorization response received");

        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException exception = AuthorizationException.fromIntent(intent);

        if(response != null || exception != null){
            authStateManager.updateAfterAuthorization(response, exception);
        }

        if(response != null && response.authorizationCode != null){
            Log.d(TAG, "Authorization code: " + response.authorizationCode);
            authorizationService.performTokenRequest(response.createTokenExchangeRequest(), this::handleCodeEchangeResponse);
            return;
        }

        if(exception != null){
            //handle exception
            Log.e(TAG, "Authorization exception: ", exception);
            currentAuthSubject.onError(exception);
            return;
        }

        currentAuthSubject.onError(new Exception("No authorization response"));
    }

    private void handleCodeEchangeResponse(TokenResponse tokenResponse, AuthorizationException e) {
        Log.d(TAG, "Token response received");
        authStateManager.updateAfterTokenResponse(tokenResponse, e);

        if(!authStateManager.getCurrent().isAuthorized()){
            Log.e(TAG, "Not authorized");
            currentAuthSubject.onError(new Exception("Not authorized"));
            return;
        }

        Log.d(TAG, "Token: " + authStateManager.getCurrent().getAccessToken());
        if(authStateManager.getCurrent().getAccessToken() == null){
            currentAuthSubject.onError(new Exception("No access token"));
            return;
        }
        currentAuthSubject.onSuccess(authStateManager.getCurrent().getAccessToken());
    }

    private AuthorizationRequest getAuthRequest(){
        AuthorizationServiceConfiguration configuration;
        if(authStateManager.getCurrent().getAuthorizationServiceConfiguration() != null){
            configuration = authStateManager.getCurrent().getAuthorizationServiceConfiguration();
        }else {
            configuration = new AuthorizationServiceConfiguration(
                    Uri.parse(MyConfig.SSO_AUTH),
                    Uri.parse(MyConfig.SSO_TOKEN));
        }

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                configuration,
                MyConfig.CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(MyConfig.OAUTH_CALLBACK));

        return builder.setScopes(List.of(MyConfig.SSO_SCOPE_OPENID)).build();
    }
}
