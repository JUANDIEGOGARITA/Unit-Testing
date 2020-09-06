package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;

import static com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.Status.FAILURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FetchReputationUseCaseSyncTest {

  private static final int REPUTATION = 100;
  private static final int ZERO_REPUTATION = 0;

  GetReputationHttpEndpointSyncDouble mGetReputationHttpEndpointSyncDouble;
  FetchReputationUseCaseSync SUT;

  @Before
  public void setUp() throws Exception {
    mGetReputationHttpEndpointSyncDouble = new GetReputationHttpEndpointSyncDouble();
    SUT = new FetchReputationUseCaseSyncImpl(mGetReputationHttpEndpointSyncDouble);
  }

  @Test
  public void fetchReputation_completes_successStatusReturned() {
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getStatus(), is(FetchReputationUseCaseSync.Status.SUCCESS));
  }

  @Test
  public void fetchReputation_completes_reputationValueReturned() {
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getReputation(), is(REPUTATION));
  }

  @Test
  public void fetchReputation_generalServerError_failureStatusReturned() {
    generalServerError();
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getStatus(), is(FAILURE));
  }

  @Test
  public void fetchReputation_generalServerError_zeroReputationReturned() {
    generalServerError();
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getReputation(), is(ZERO_REPUTATION));
  }

  @Test
  public void fetchReputation_networkError_failureStatusReturned() {
    networkError();
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getStatus(), is(FAILURE));
  }

  @Test
  public void fetchReputation_networkError_zeroReputationReturned() {
    networkError();
    UseCaseResult useCaseResult = SUT.fetchReputation();

    assertThat(useCaseResult.getReputation(), is(ZERO_REPUTATION));
  }

  private void generalServerError() {
    mGetReputationHttpEndpointSyncDouble.mGeneralError = true;
  }

  private void networkError() {
    mGetReputationHttpEndpointSyncDouble.mNetworkError = true;
  }

  private class GetReputationHttpEndpointSyncDouble implements GetReputationHttpEndpointSync {
    public boolean mGeneralError;
    public boolean mNetworkError;

    @Override
    public EndpointResult getReputationSync() {
      if (mGeneralError) {
        return new EndpointResult(EndpointStatus.GENERAL_ERROR, 0);
      }
      else if (mNetworkError) {
        return new EndpointResult(EndpointStatus.NETWORK_ERROR, 0);
      }
      else {
        return new EndpointResult(EndpointStatus.SUCCESS, REPUTATION);
      }
    }
  }
}