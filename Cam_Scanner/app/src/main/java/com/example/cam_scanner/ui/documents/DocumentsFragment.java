package com.example.cam_scanner.ui.documents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cam_scanner.databinding.FragmentDocumentsBinding;

public class DocumentsFragment extends Fragment {

    private FragmentDocumentsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DocumentsViewModel documentsViewModel =
                new ViewModelProvider(this).get(DocumentsViewModel.class);

        binding = FragmentDocumentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDocuments;
        documentsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}