package com.example.wifiawareplayground.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.wifiawareplayground.R;
import com.example.wifiawareplayground.databinding.SessionHostBinding;

public class SessionHostFragment extends Fragment {

    public static final String TAG = "SessionHostFragment";
    private Context viewContext;

    private SessionHostBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SessionHostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewContext = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.joinSessionButton.setOnClickListener(v -> {
            Log.i(TAG, "join session");
            joinMessagingSession();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void joinMessagingSession() {
        Log.d(TAG, "joinMessagingSession");
        NavHostFragment.findNavController(this).navigate(R.id.messagingFragment);
    }
}
