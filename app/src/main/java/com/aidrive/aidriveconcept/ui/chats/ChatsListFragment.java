package com.aidrive.aidriveconcept.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.adapter.ChatsListAdapter;
import com.aidrive.aidriveconcept.databinding.ChatListFragmentBinding;
import com.softradix.callbacks.OnChatCallback;

public class ChatsListFragment extends Fragment implements OnChatCallback {

    private ChatsViewModel mViewModel;

    public static ChatsListFragment newInstance() {
        return new ChatsListFragment();
    }

    private ChatListFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ChatListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
        showDefaultFragment();
    }

    private void setRecyclerView() {

        binding.chatsList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.chatsList.setAdapter(new ChatsListAdapter(this));
    }

    private void showDefaultFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chatFragment, new ChatFragment(), "Chat").commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initMqttStatusListener() {

    }


    @Override
    public void onSelectUserChat(int position) {
        Navigation.findNavController(requireActivity(), R.id.chatFragment);
    }
}