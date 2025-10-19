package de.dmitrij.patuk.ourapp.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import de.dmitrij.patuk.ourapp.ui.libs.SingleLiveEvent;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final SingleLiveEvent<Void> logoutEvent = new SingleLiveEvent<>();

    public LiveData<String> getTitle() {
        return title;
    }

    public SingleLiveEvent<Void> getLogoutEvent() {
        return logoutEvent;
    }

    public void logout() {
        logoutEvent.call();
    }


    @Inject
    public SettingsViewModel() {
        title.setValue("Our Settings");
    }
}