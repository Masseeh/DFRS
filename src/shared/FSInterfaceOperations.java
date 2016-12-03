package shared;


/**
* shared/FSInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from frontEnd/Interface.idl
* Saturday, December 3, 2016 2:14:02 AM EST
*/

public interface FSInterfaceOperations 
{
  int bookFlight (String firsName, String lastName, String address, String phone, String destination, String date, String flightClass);
  String getBookedFlightCount ();
  int editRecord (String recordId, String fieldName, String newValue);
  int addFlight (String destination, String date, String ec, String bus, String fir);
  int removeFlight (String recordId);
  int transferReservation (String clientId, String currentCity, String otherCity);
} // interface FSInterfaceOperations
