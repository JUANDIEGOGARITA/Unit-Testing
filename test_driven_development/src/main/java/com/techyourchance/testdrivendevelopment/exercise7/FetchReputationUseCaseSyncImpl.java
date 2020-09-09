package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;

class FetchReputationUseCaseSyncImpl implements FetchReputationUseCaseSync {
  GetReputationHttpEndpointSync getReputationHttpEndpointSync;

  public FetchReputationUseCaseSyncImpl(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
    this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
  }

  @Override
  public UseCaseResult fetchReputation() {
    EndpointResult endpointResult = getReputationHttpEndpointSync.getReputationSync();
    try {
      switch (endpointResult.getStatus()) {
        case SUCCESS:
          return new UseCaseResult(Status.SUCCESS, endpointResult.getReputation());
        case GENERAL_ERROR:
        case NETWORK_ERROR:
          return new UseCaseResult(Status.FAILURE, 0);
        default:
          throw new NetworkErrorException();
      }
    }
    catch (NetworkErrorException e) {
      return new UseCaseResult(Status.FAILURE, 0);
    }
  }
}