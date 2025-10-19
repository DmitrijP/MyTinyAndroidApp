package de.dmitrij.patuk.ourapp.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.hilt.android.AndroidEntryPoint;
import de.dmitrij.patuk.ourapp.databinding.FragmentHomeBinding;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private HomeViewModel vm;
    private @NonNull FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setVm(vm);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}