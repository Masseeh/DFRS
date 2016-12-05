package client;

import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import shared.FSInterface;
import shared.FSInterfaceHelper;

import java.util.Scanner;

/**
 * Created by masseeh on 10/4/16.
 */
public class Console {

	private Scanner input;
	private String serverName = "frontEnd";
	private String city;
	org.omg.CORBA.Object objRef;
	ORB orb;
	NamingContextExt ncRef;

	public static void main (String[] args) {
		new Console().init(args);
	}

	public Console() {
		input = new Scanner(System.in).useDelimiter("\n");
	}

	public Console(String data) {
		input = new Scanner(data).useDelimiter(",");
	}

	public void init(String[] args) {

		orb = ORB.init(args,null);
		try {
			objRef = orb.resolve_initial_references("NameService");
			ncRef = NamingContextExtHelper.narrow(objRef);
		} catch (InvalidName invalidName) {
			invalidName.printStackTrace();
		}

		this.greeting();

	}

	public void greeting() {

		System.out.println("1.Manager");
		System.out.println("2.Client");
		int choice = input.nextInt();

		if (choice == 1) {
			managerConsole();
		}
		else {
			clientConsole();
		}
	}

	public void managerConsole() {
		System.out.println("Please enter your ID");
		String managerCity = input.next();
		if(managerCity.startsWith("MTL")) {
			city = "MTL";
		}
		else if(managerCity.startsWith("WST")) {
			city = "WST";
		}
		else {
			city = "NDH";
		}

		managerActions(city);

	}

	public void managerActions(String city) {
		System.out.println("1.add flight");
		System.out.println("2.remove flight");
		System.out.println("3.edit flight");
		System.out.println("4.get flight number");
		System.out.println("5.transfer flight");
		int choice = input.nextInt();
		switch (choice) {
		case 1:
			System.out.println("Enter destination:");
			String destination = input.next();
			System.out.println("Enter flight date:");
			String flightDate = input.next();
			System.out.println("Enter economy seat:");
			int eSeat = input.nextInt();
			System.out.println("Enter business seat:");
			int bSeat = input.nextInt();
			System.out.println("Enter first seat:");
			int fSeat = input.nextInt();
			addFlight(city,destination,flightDate, eSeat + "", bSeat + "", fSeat + "");
			break;
		case 2:
			System.out.println("Enter flight id");
			String id = input.next();
			removeFlight(city,id);
			break;
		case 3:
			System.out.println("Enter flight id");
			String flightId = input.next();
			System.out.println("1.flight date 2.destination 3.seats");
			int c = input.nextInt();
			if(c == 1) {
				System.out.println("Enter new value");
				String nValue = input.next();
				editFlight(city,flightId, "flightDate", nValue);
			}
			else if(c == 2) {
				System.out.println("Enter new value");
				String nValue = input.next();
				editFlight(city,flightId, "destination", nValue);
			}
			else {
				System.out.println("1.economy 2.business 3.first");
				int fClass = input.nextInt();
				System.out.println("Enter new value");
				int nValue = input.nextInt();
				editFlight(city,flightId, fClass + "", nValue + "");
			}
			break;
		case 4:
			String flightNumber = getFlightNumber(city);
			System.out.println(flightNumber);
			break;
		case 5:
			System.out.println("Enter passenger id");
			int pasId = input.nextInt();
			System.out.println("Enter current city");
			String currentCity = input.next();
			System.out.println("Enter other city");
			String otherCity = input.next();
			int res = transferFlight(city, pasId , currentCity , otherCity);
			break;
		default:
			break;
		}
	}

	private int transferFlight(String city, int pasId, String currentCity, String otherCity) {


		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
			int res = fsInterface.transferReservation(city , pasId + "" , currentCity , otherCity);
			return res;
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}


		return 0;
	}

	private String getFlightNumber(String city) {
		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
			String res = fsInterface.getBookedFlightCount(city);
			return res;
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}
		return "NA";
	}

	public void clientConsole() {
		System.out.println("Enter your location:");
		city = input.next();
		bookFlight(city);
	}

	public void bookFlight(String city) {
		System.out.println("Enter your first name:");
		String firstName = input.next();
		System.out.println("Enter your last name:");
		String lastName = input.next();
		System.out.println("Enter your address:");
		String address = input.next();
		System.out.println("Enter your destination:");
		String destination = input.next();
		System.out.println("Enter your flight date:");
		String date = input.next();
		System.out.println("Enter your phone:");
		String phone = input.next();
		System.out.println("Choose your flight class:");
		System.out.println("0.Economy 1.Business 2.First");
		int flightClass = input.nextInt();

		bookFlight(city,firstName, lastName, destination, address, phone, date, flightClass);

	}

	public void bookFlight(String city, String firstName, String lastName, String destination,
			String address, String phone, String date, int flightClass) {
		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
			int result = fsInterface.bookFlight(city,firstName, lastName, address, phone,
					destination , date, flightClass + "");
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}

	}

	public int addFlight(String managerId , String destination, String date, String e, String b, String f) {
		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
			int res = fsInterface.addFlight(managerId,destination, date, e,b,f);
			return res;
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}
		return 0;
	}

	public int removeFlight(String managerId , String id) {
		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));;
			int res = fsInterface.removeFlight(managerId,id + "");
			return res;
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}
		return 0;
	}

	public int editFlight(String city, String id, String fieldName, String value) {
		try {
			FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
			int res = fsInterface.editRecord(city,id + "", fieldName, value);
			return res;
		} catch (CannotProceed cannotProceed) {
			cannotProceed.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
			invalidName.printStackTrace();
		} catch (NotFound notFound) {
			notFound.printStackTrace();
		}
		return 0;
	}
}
