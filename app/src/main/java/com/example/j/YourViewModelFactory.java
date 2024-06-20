package com.example.j;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class YourViewModelFactory implements ViewModelProvider.Factory {
    private final SSEClient sseClient;

    public YourViewModelFactory(SSEClient sseClient) {
        this.sseClient = sseClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(YourViewModel.class)) {
            return (T) new YourViewModel(sseClient, "ybfieiozfffdytdzm47l");
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}