package de.dmitrij.patuk.ourapp.ui.capture;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.dmitrij.patuk.ourapp.R;

public class CaptureFragment extends Fragment {

    private CaptureViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CaptureViewModel.class);
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }
}