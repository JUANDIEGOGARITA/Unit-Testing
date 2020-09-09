package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {


  FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
  UsersCache cache;

  public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache cache) {
    this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
    this.cache = cache;
  }

  @Override
  public UseCaseResult fetchUserSync(String userId) {
    User userCached = cache.getUser(userId);
    if (userCached == null) {
      try {
        FetchUserHttpEndpointSync.EndpointResult result = fetchUserHttpEndpointSync.fetchUserSync(userId);
        FetchUserHttpEndpointSync.EndpointStatus status = result.getStatus();

        switch (status) {
          case SUCCESS:
            User tempUserCached = new User(userId, result.getUsername());
            cache.cacheUser(tempUserCached);
            return new UseCaseResult(Status.SUCCESS, tempUserCached);
          case AUTH_ERROR:
          case GENERAL_ERROR:
            return new UseCaseResult(Status.FAILURE, null);
          default:
            throw new NetworkErrorException();
        }
      }
      catch (NetworkErrorException e) {
        return new UseCaseResult(Status.NETWORK_ERROR, null);
      }
    }
    else {
      return new UseCaseResult(Status.SUCCESS, userCached);
    }
  }
}
