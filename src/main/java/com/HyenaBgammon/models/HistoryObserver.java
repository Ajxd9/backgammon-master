package com.HyenaBgammon.models;

import java.util.List;

public interface HistoryObserver {
    void onHistoryUpdated(List<History> updatedHistory);
}