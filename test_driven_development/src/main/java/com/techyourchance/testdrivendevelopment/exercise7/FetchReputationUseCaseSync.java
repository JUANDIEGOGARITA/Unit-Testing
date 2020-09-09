package com.techyourchance.testdrivendevelopment.exercise7;

import org.jetbrains.annotations.Nullable;

interface FetchReputationUseCaseSync {

  UseCaseResult fetchReputation();

  enum Status {
    SUCCESS,
    FAILURE,
    NETWORK_ERROR
  }

  class UseCaseResult {
    private final Status mStatus;

    @Nullable
    private final int mReputation;

    public UseCaseResult(Status status, @Nullable int reputation) {
      mStatus = status;
      mReputation = reputation;
    }

    public Status getStatus() {
      return mStatus;
    }

    @Nullable
    public int getReputation() {
      return mReputation;
    }
  }
}
