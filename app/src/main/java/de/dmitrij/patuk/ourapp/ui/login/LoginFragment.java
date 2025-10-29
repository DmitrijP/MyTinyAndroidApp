package de.dmitrij.patuk.ourapp.ui.login;

import static android.app.Activity.RESULT_CANCELED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dagger.hilt.android.AndroidEntryPoint;
import de.dmitrij.patuk.ourapp.R;
import de.dmitrij.patuk.ourapp.databinding.FragmentLoginBinding;
import de.dmitrij.patuk.ourapp.navigation.MyNavDestinations;
import de.dmitrij.patuk.ourapp.navigation.MyNavigator;
import de.dmitrij.patuk.ourapp.ui.MainActivity;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;
    private @NonNull FragmentLoginBinding binding;

    private final ActivityResultLauncher<Intent> authResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleAuthIntentResult);

    private void handleAuthIntentResult(ActivityResult activityResult) {
        if (activityResult.getResultCode() == RESULT_CANCELED) {
            Toast.makeText(requireContext(), "Login wurde abgebrochen", Toast.LENGTH_LONG).show();
            return;
        }

        viewModel.handleLoginResult(activityResult.getData());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getLaunchAuthIntent().observe(this.getViewLifecycleOwner(), v -> {
            authResultLauncher.launch(viewModel.getAuthorizationIntent());
        });

        viewModel.getLoginSuccess().observe(this.getViewLifecycleOwner(), v -> {
            ((MyNavigator)requireActivity()).navigate(MyNavDestinations.HOME);
        });

        viewModel.getLoginError().observe(this.getViewLifecycleOwner(), v -> {
            Toast.makeText(requireContext(), "Login fehlgeschlagen.", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}