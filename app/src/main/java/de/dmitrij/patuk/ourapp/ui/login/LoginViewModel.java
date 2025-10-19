package de.dmitrij.patuk.ourapp.ui.login;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import de.dmitrij.patuk.ourapp.services.AuthService;
import de.dmitrij.patuk.ourapp.ui.libs.SingleLiveEvent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final AuthService authService;

    @Inject
    public LoginViewModel(AuthService authService) {
        this.authService = authService;
    }

    private final SingleLiveEvent<Void> launchAuthIntent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> getLaunchAuthIntent() {
        return launchAuthIntent;
    }

    private final SingleLiveEvent<Void> loginSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> getLoginSuccess() {
        return loginSuccess;
    }

    private final SingleLiveEvent<Void> loginError = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> getLoginError() {
        return loginError;
    }


    public void handleLoginResult(Intent activityResult) {
        authService.onActivityResult(activityResult);
    }

    public Intent getAuthorizationIntent(){
        return authService.getAuthorizationIntent();
    }

    private final CompositeDisposable disposable = new CompositeDisposable();
    public void startLoginProcess(){
        disposable.add(
                authService.launchAuthorization()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                token -> {
                                    loginSuccess.call();
                                },
                                error -> {
                                    loginError.call();
                                }
                        )
        );
        launchAuthIntent.call();
    }

    protected void onCleared(){
        super.onCleared();
        disposable.clear();
    }
}