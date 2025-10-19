package de.dmitrij.patuk.ourapp.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import de.dmitrij.patuk.ourapp.databinding.FragmentSettingsBinding;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private SettingsViewModel vm;
    private @NonNull FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.setViewModel(vm);
        binding.setLifecycleOwner(this);

        vm.getLogoutEvent().observe(getViewLifecycleOwner(), v -> {
            // context oder andere UI Element zugreifen will
            Snackbar.make(binding.getRoot(), "Logout", Snackbar.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

}