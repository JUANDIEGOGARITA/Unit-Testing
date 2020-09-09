package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.FailReason.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.FailReason.NETWORK_ERROR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {
  public static final String TERM = "term";
  public static final String ID = "id";
  public static final String FULL_NAME = "full name";
  public static final String PHONE = "phone";
  public static final String IMAGE = "image url";
  public static final double AGE = 20;

  @Mock
  GetContactsHttpEndpoint getContactsHttpEndpointMock;

  @Mock
  FetchContactsUseCase.Listener listener1;

  @Mock
  FetchContactsUseCase.Listener listener2;

  @Captor
  ArgumentCaptor<List<Contact>> acContactList;

  FetchContactsUseCase SUT;

  @Before
  public void setUp() throws Exception {
    SUT = new FetchContactsUseCase(getContactsHttpEndpointMock);
    success();
  }

  private List<ContactSchema> getContactSchemaList() {
    List<ContactSchema> contactSchemaList = new ArrayList<>();
    contactSchemaList.add(new ContactSchema(ID, FULL_NAME, PHONE, IMAGE, AGE));
    return contactSchemaList;
  }

  @Test
  public void fetchContacts_correctTermPassedToEndpoint() throws Exception {
    // Arrange
    ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
    // Act
    SUT.fetchContactsAndNotify(TERM);
    // Assert
    verify(getContactsHttpEndpointMock).getContacts(acString.capture(), any(GetContactsHttpEndpoint.Callback.class));
    assertThat(acString.getValue(), is(TERM));
  }

  @Test
  public void fetchCartItems_success_observersNotifiedWithCorrectData() throws Exception {
    // Arrange
    // Act
    SUT.registerListener(listener1);
    SUT.registerListener(listener2);
    SUT.fetchContactsAndNotify(TERM);
    // Assert
    verify(listener1).onContactsFetched(acContactList.capture());
    verify(listener2).onContactsFetched(acContactList.capture());
    List<List<Contact>> captures = acContactList.getAllValues();
    List<Contact> capture1 = captures.get(0);
    List<Contact> capture2 = captures.get(1);
    assertThat(capture1, is(getContactList()));
    assertThat(capture2, is(getContactList()));
  }

  @Test
  public void fetchCartItems_success_unsubscribedObserversNotNotified() throws Exception {
    // Arrange
    // Act
    SUT.registerListener(listener1);
    SUT.registerListener(listener2);
    SUT.unregisterListener(listener2);
    SUT.fetchContactsAndNotify(TERM);
    // Assert
    verify(listener1).onContactsFetched(any(List.class));
    verifyNoMoreInteractions(listener2);
  }

  @Test
  public void fetchCartItems_generalError_observersNotifiedOfFailure() throws Exception {
    // Arrange
    generalError();
    // Act
    SUT.registerListener(listener1);
    SUT.registerListener(listener2);
    SUT.fetchContactsAndNotify(TERM);
    // Assert
    verify(listener1).onContactsFailed();
    verify(listener2).onContactsFailed();
  }

  @Test
  public void fetchCartItems_networkError_observersNotifiedOfFailure() throws Exception {
    // Arrange
    networkError();
    // Act
    SUT.registerListener(listener1);
    SUT.registerListener(listener2);
    SUT.fetchContactsAndNotify(TERM);
    // Assert
    verify(listener1).onContactsFailedDueNetworkError();
    verify(listener2).onContactsFailedDueNetworkError();
  }


  private List<Contact> getContactList() {
    List<Contact> contactSchemaList = new ArrayList<>();
    contactSchemaList.add(new Contact(ID, FULL_NAME, IMAGE));
    return contactSchemaList;
  }

  private void success() {
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
      callback.onGetContactsSucceeded(getContactSchemaList());
      return null;
    }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
  }

  private void networkError() {
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
      callback.onGetContactsFailed(NETWORK_ERROR);
      return null;
    }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
  }

  private void generalError() {
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
      callback.onGetContactsFailed(GENERAL_ERROR);
      return null;
    }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
  }
}