import java.util.*;

class Room {
    int roomNumber;
    boolean isOccupied;
    Room next;

    Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isOccupied = false;
        this.next = null;
    }
}

class Guest {
    String name;
    int roomNumber;
    long checkInTime;
    static String roomType;
    Guest next;

    Guest(String name, int roomNumber,String roomType, long checkInTime) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.roomType=roomType;
        this.checkInTime = checkInTime;
        this.next = null;
    }
}

class LinkedList {
    Room roomHead;
    Guest guestHead;
    double hourlyRate;
    double dailyRate;

    LinkedList(int numRooms,double hourlyRate,double dailyRate) {
        roomHead = null;
        guestHead = null;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        
        for (int i = 1; i <= numRooms; i++) {
            addRoom(i);
        }
    }

    void addRoom(int roomNumber) {
        Room newRoom = new Room(roomNumber);
        if (roomHead == null) {
            roomHead = newRoom;
        } else {
            Room currentRoom = roomHead;
            while (currentRoom.next != null) {
                currentRoom = currentRoom.next;
            }
            currentRoom.next = newRoom;
        }
    }

    void addGuest(String name, int roomNumber, String roomType,long checkInTime) {
        Guest newGuest = new Guest(name, roomNumber,roomType, checkInTime);
        if (guestHead == null) {
            guestHead = newGuest;
        } else {
            Guest currentGuest = guestHead;
            while (currentGuest.next != null) {
                currentGuest = currentGuest.next;
            }
            currentGuest.next = newGuest;
        }
        
        setRoomOccupied(roomNumber, true);
    }

    void setRoomOccupied(int roomNumber, boolean occupied) {
        Room currentRoom = roomHead;
        while (currentRoom != null) {
            if (currentRoom.roomNumber == roomNumber) {
                currentRoom.isOccupied = occupied;
                return;
            }
            currentRoom = currentRoom.next;
        }
    }

    void checkIn() {
        Scanner sc=new Scanner(System.in);
        int flag=0;
        while(flag!=1)
        {
        System.out.println("1. hourly rated  //hourly rate : 100");
        System.out.println("2. daily rated   //1-day rate :500");
        System.out.print("enter your choice :");
        int choice=sc.nextInt();
        int roomNumber;
        String name ;
        String roomType;

        switch(choice)
        {
            case 1:
                {
                System.out.println("available rooms are : ");
                viewAvailableRooms();
                System.out.println();

                System.out.println("Enter check-in room number: ");
                roomNumber = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter guest name: ");
                name = sc.nextLine();
                System.out.println();
                Room room = findAvailableRoom(roomNumber);
                if (room == null) {
                    System.out.println("Invalid room number or room is already occupied.");
                    return;
                }
                roomType="hourly";
                long checkInTime = System.currentTimeMillis();

                addGuest(name, roomNumber,roomType, checkInTime);

                System.out.println("Check-in successful."); 
                flag=1;
                }
                break;

            case 2:
                {
                System.out.println("available rooms are : ");
                viewAvailableRooms();
                System.out.println();
                
                System.out.println("Enter check-in room number: ");
                roomNumber = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter guest name: ");
                name = sc.nextLine();
                
                Room room = findAvailableRoom(roomNumber);
                if (room == null) {
                    System.out.println("Invalid room number or room is already occupied.");
                    return;
                }
                roomType="daily";
                long checkInTime = System.currentTimeMillis();

                addGuest(name, roomNumber,roomType, checkInTime);

                System.out.println("Check-in successful."); 
                flag=1;
                }
                break;
            
            default :
                System.out.println("enter valid choice");
            }
        }

        
    }

    Room findAvailableRoom(int roomNumber) {
        Room currentRoom = roomHead;
        while (currentRoom != null) {
            if (currentRoom.roomNumber == roomNumber && !currentRoom.isOccupied) {

                return currentRoom;
            }
            currentRoom = currentRoom.next;
        }
        return null;
    }

     void checkOut() {
        System.out.println("occupied rooms are : ");
        viewOccupiedRooms();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number for check-out: ");
        int roomNumber = scanner.nextInt();

        Guest guest = findGuest(roomNumber);
        if (guest == null) {
            System.out.println("Room not found or not occupied.");
            return;
        }

        long checkOutTime = System.currentTimeMillis();
        long millisecondsStayed = checkOutTime - guest.checkInTime;
        long hoursStayed = millisecondsStayed / (60 * 60 * 1000);
        long daysStayed=millisecondsStayed / (60 * 60 * 1000 * 24);
        if (millisecondsStayed % (60 * 60 * 1000) != 0) {
            hoursStayed++; // Round up if there are any remaining minutes
        }
        if (millisecondsStayed % (60 * 60 * 1000 * 24) != 0) {
            daysStayed++; // Round up if there are any remaining minutes
        }

        double bill ;
        if(Guest.roomType=="hourly")
        {
            bill = hoursStayed * hourlyRate;
            System.out.println("Guest: " + guest.name);
            System.out.println("Room Number: " + guest.roomNumber);
            System.out.println("Time Stayed: " + hoursStayed + " hours");
            System.out.println("Total Bill: " + bill);

        removeGuest(roomNumber);
        setRoomOccupied(roomNumber, false);

        System.out.println("Check-out successful.");
        }
        else if(Guest.roomType=="daily")
        {
            bill = daysStayed * dailyRate;
            System.out.println("Guest: " + guest.name);
            System.out.println("Room Number: " + guest.roomNumber);
            System.out.println("days Stayed: " + daysStayed + " days");
            System.out.println("Total Bill: " + bill);

        removeGuest(roomNumber);
        setRoomOccupied(roomNumber, false);

        System.out.println("Check-out successful.");
        }
    }

    Guest findGuest(int roomNumber) {
        Guest currentGuest = guestHead;
        while (currentGuest != null) {
            if (currentGuest.roomNumber == roomNumber) {
                return currentGuest;
            }
            currentGuest = currentGuest.next;
        }
        return null;
    }

    void removeGuest(int roomNumber) {
        if (guestHead == null) {
            return;
        }

        if (guestHead.roomNumber == roomNumber) {
            guestHead = guestHead.next;
            return;
        }

        Guest currentGuest = guestHead;
        while (currentGuest.next != null) {
            if (currentGuest.next.roomNumber == roomNumber) {
                currentGuest.next = currentGuest.next.next;
                return;
            }
            currentGuest = currentGuest.next;
        }
    }

    void viewAvailableRooms() {
        System.out.println("Available Rooms:");
        Room currentRoom = roomHead;
        while (currentRoom != null) {
            if (!currentRoom.isOccupied) {
                System.out.println("Room " + currentRoom.roomNumber);
            }
            currentRoom = currentRoom.next;
        }
    }

    void viewOccupiedRooms() {
        System.out.println("Occupied Rooms:");
        Guest currentGuest = guestHead;
        while (currentGuest != null) {
            System.out.println("Room " + currentGuest.roomNumber);
            currentGuest = currentGuest.next;
        }
    }
}

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of rooms in the hotel: ");
        int numRooms = scanner.nextInt();
        System.out.print("Enter hourly rate of rooms in the hotel: ");
        int hourlyrate = scanner.nextInt();
        System.out.print("Enter daily rate of rooms in the hotel: ");
        int dailyrate = scanner.nextInt();

        LinkedList linkedList = new LinkedList(numRooms,hourlyrate,dailyrate);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Check-in");
            System.out.println("2. Check-out");
            System.out.println("3. View Available Rooms");
            System.out.println("4. View Occupied Rooms");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    linkedList.checkIn();
                    break;
                case 2:
                    linkedList.checkOut();
                    break;
                case 3:
                    linkedList.viewAvailableRooms();
                    break;
                case 4:
                    linkedList.viewOccupiedRooms();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }
}

