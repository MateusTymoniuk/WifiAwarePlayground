package com.example.wifiawareplayground.fragments;

import android.content.Context;
import android.net.wifi.aware.DiscoverySessionCallback;
import android.net.wifi.aware.PeerHandle;
import android.net.wifi.aware.PublishConfig;
import android.net.wifi.aware.PublishDiscoverySession;
import android.net.wifi.aware.SubscribeConfig;
import android.net.wifi.aware.SubscribeDiscoverySession;
import android.net.wifi.aware.WifiAwareSession;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiawareplayground.MainActivity;
import com.example.wifiawareplayground.WifiAwareHelper;
import com.example.wifiawareplayground.databinding.MessagingBinding;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MessagingFragment extends Fragment {
    private MessagingBinding binding;
    private Context viewContext;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected List<String> mDataset = new ArrayList<>();
    private WifiAwareSession mWifiAwareSession;
    private PublishDiscoverySession mPublishSession;
    private SubscribeDiscoverySession mSubscribeSession;

    private Set<PeerHandle> peers;
    private int messageId = 1;
    private final DiscoverySessionCallback mDiscoverySessionCallback = new DiscoverySessionCallback() {
        @Override
        public void onPublishStarted(PublishDiscoverySession session) {
            mPublishSession = session;
            // TODO: Handle publish session started
        }

        @Override
        public void onSubscribeStarted(SubscribeDiscoverySession session) {
            mSubscribeSession = session;
            // TODO: Handle subscribe session started
        }

        @Override
        public void onMessageReceived(PeerHandle peerHandle, byte[] message) {
            // TODO: Handle received message
            mDataset.add(new String(message));
        }

        // TODO: Implement other callback methods


        @Override
        public void onServiceDiscovered(PeerHandle peerHandle, byte[] serviceSpecificInfo, List<byte[]> matchFilter) {
            super.onServiceDiscovered(peerHandle, serviceSpecificInfo, matchFilter);
            peers.add(peerHandle);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        WifiAwareHelper helper = activity.getWifiAwareHelper();
        mWifiAwareSession = helper.getWifiAwareSession();

        PublishConfig publishConfig = new PublishConfig.Builder()
                .setServiceName(WifiAwareHelper.SERVICE_NAME)
                .build();

        Handler handler = activity.getHandler();

        mWifiAwareSession.publish(publishConfig, mDiscoverySessionCallback, handler);

        SubscribeConfig subscribeConfig = new SubscribeConfig.Builder()
                .setServiceName(WifiAwareHelper.SERVICE_NAME)
                .build();

        mWifiAwareSession.subscribe(subscribeConfig, mDiscoverySessionCallback, handler);

//        initMockDataset();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MessagingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewContext = view.getContext();
        createMessagesListView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sendMessageButton.setOnClickListener(v -> {
            sendMessage();
            binding.messageToSend.setText("");
        });
    }

    private void createMessagesListView() {
        mAdapter = new CustomAdapter(mDataset);
        binding.messagesListView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.messagesListView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mPublishSession.close();
        mSubscribeSession.close();
    }

    private void initMockDataset() {
        for (int i = 0; i < 60; i++) {
            mDataset.add(i, "This is element #" + i);
        }
    }

    private void sendMessage() {
        if (Objects.isNull(peers) || peers.isEmpty()) {
            Toast.makeText(viewContext, "No peers", Toast.LENGTH_SHORT).show();
            return;
        }
        String message = binding.messageToSend.getText().toString();
        peers.forEach(peer ->
                mPublishSession.sendMessage(peer, messageId, message.getBytes(StandardCharsets.UTF_8)));
    }
}
