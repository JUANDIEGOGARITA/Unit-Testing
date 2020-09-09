package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.FailReason;

import java.util.ArrayList;
import java.util.List;

class FetchContactsUseCase {

  public interface Listener {
    void onContactsFetched(List<Contact> contacts);

    void onContactsFailed();

    void onContactsFailedDueNetworkError();
  }

  private final List<Listener> listeners = new ArrayList<>();
  private final GetContactsHttpEndpoint endpoint;

  public FetchContactsUseCase(GetContactsHttpEndpoint endpoint) {
    this.endpoint = endpoint;
  }

  public void fetchContactsAndNotify(String term) {
    endpoint.getContacts(term, new GetContactsHttpEndpoint.Callback() {
      @Override
      public void onGetContactsSucceeded(List<ContactSchema> contactSchemaList) {
        notifySucceeded(contactSchemaList);
      }

      @Override
      public void onGetContactsFailed(FailReason failReason) {
        switch (failReason) {
          case GENERAL_ERROR:
            notifyFailed();
            break;
          case NETWORK_ERROR:
            notifyFailedDueNetworkError();
            break;
        }
      }
    });

  }

  private void notifySucceeded(List<ContactSchema> contactSchemaList) {
    for (Listener listener : listeners) {
      listener.onContactsFetched(getContactsFromSchemas(contactSchemaList));
    }
  }

  private void notifyFailed() {
    for (Listener listener : listeners) {
      listener.onContactsFailed();
    }
  }

  private void notifyFailedDueNetworkError() {
    for (Listener listener : listeners) {
      listener.onContactsFailedDueNetworkError();
    }
  }

  private List<Contact> getContactsFromSchemas(List<ContactSchema> contactSchemaList) {
    List<Contact> contactList = new ArrayList<>();
    for (ContactSchema schema : contactSchemaList) {
      contactList.add(new Contact(
        schema.getId(),
        schema.getFullName(),
        schema.getImageUrl()
      ));
    }
    return contactList;
  }

  public void registerListener(Listener listener) {
    listeners.add(listener);
  }

  public void unregisterListener(Listener listener) {
    listeners.remove(listener);
  }
}