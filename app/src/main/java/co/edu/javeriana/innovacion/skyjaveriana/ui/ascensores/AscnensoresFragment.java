package co.edu.javeriana.innovacion.skyjaveriana.ui.ascensores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import co.edu.javeriana.innovacion.skyjaveriana.databinding.FragmentAscensoresBinding;

public class AscnensoresFragment extends Fragment {

    private FragmentAscensoresBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAscensoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}