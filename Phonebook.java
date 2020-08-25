/**
 * @author Anik Barua
 * @since 11-26-2019
 */

import java.io.*;
import java.util.*;

/*
* This phonebook replaces array with map and uses command line argument to get 
* phonebook file. Also for each person now it contains multiple phone numbers  
* accompanied with description.
 */
public class PhonebookApp {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Usage: PhonebookApp 'phonebook-filename'");
        } else {

            String filename = args[0];

            try {

                Phonebook phonebook = new Phonebook(filename);

                Scanner sc = new Scanner(System.in);
                System.out.print("lookup, quit (l/q)? ");

                while (sc.hasNext()) {
                    String letter = sc.next();

                    if (letter.equals("l")) {
                        System.out.print("last name? ");
                        String last = sc.next();
                        System.out.print("first name? ");
                        String first = sc.next();
                        PhonebookEntry phonebookEntry = phonebook.lookup(first, last);

                        if (phonebookEntry == null) {
                            System.out.println("-- Name not found\n");
                            System.out.print("lookup, quit (l/q)? ");
                            continue;
                        }
                        System.out.println(phonebookEntry.getName() + "'s " + "phone numbers: " + phonebookEntry.getPhoneNumber() + "\n");
                        System.out.print("lookup, quit (l/q)? ");
                    } else if (letter.equals("q")) {
                        System.exit(0);
                    } else {
                    }
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////
class Phonebook {

    private Map<Name, PhonebookEntry> treeMap = new TreeMap<>();

    public Phonebook(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));

        while (sc.hasNextLine()) {
            PhonebookEntry book = PhonebookEntry.read(sc);
            treeMap.put(book.getName(), book);
        }
    }

    public PhonebookEntry lookup(String first, String last) {
        Name name = new Name(last, first);
        if (treeMap.containsKey(name)) {
            return treeMap.get(name);
        }
        return null;
    }
}

////////////////////////////////////////////////////////////////////////////////////////
class PhonebookEntry {

    private Name name;
    private ArrayList<ExtendedPhoneNumber> number = new ArrayList<>();

    public PhonebookEntry(Name name, ArrayList<ExtendedPhoneNumber> number) {
        this.name = name;
        this.number = number;
    }

    public Name getName() {
        return this.name;
    }

    public ArrayList getPhoneNumber() {
        return this.number;
    }

    public void call(int x) {
        if (number.get(x).isTollFree()) {
            System.out.println("Dialing (toll-free) " + name + ": " + number);
        } else {
            System.out.println("Dialing " + name + ": " + number);
        }
    }

    public boolean equals(PhonebookEntry x) {
        return x.equals(x);
    }

    @Override
    public String toString() {
        return name + " " + number;
    }

    public static PhonebookEntry read(Scanner sc) throws IOException {
        Name name = Name.read(sc);
        int integer = sc.nextInt();

        ArrayList<ExtendedPhoneNumber> list = new ArrayList<>();

        for (int i = 0; i < integer; i++) {
            ExtendedPhoneNumber numbers = ExtendedPhoneNumber.read(sc);
            list.add(numbers);
        }
        return new PhonebookEntry(name, list);
    }
}

/////////////////////////////////////////////////////////////////////////////////////
class Name implements Comparable<Name> {

    private String first, last;

    public Name(String last, String first) {
        this.last = last;
        this.first = first;
    }

    public Name(String first) {
        this("", first);
    }

    public String getFormal() {
        return first + " " + last;
    }

    public String getOfficial() {
        return last + ", " + first;
    }

    public String getInitials() {
        return first.charAt(0) + "." + last.charAt(0) + ".";
    }

    public boolean equals(Name other) {
        return first.equals(other.first) && last.equals(other.last);
    }

    public String getLast() {
        return last;
    }

    @Override
    public String toString() {
        return first + " " + last;
    }

    public static Name read(Scanner sc) {
        String x = sc.next();
        String y = sc.next();
        Name xy = new Name(x, y);
        return xy;
    }

    @Override
    public int compareTo(Name name) {
        return this.getFormal().compareTo(name.getFormal());
    }
}

//////////////////////////////////////////////////////////////////////////
class PhoneNumber {

    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    public String getAreaCode() {
        return number.substring(1, 4);
    }

    public String getExchange() {
        return number.substring(5, 8);
    }

    public String getLineNumber() {
        return number.substring(9);
    }

    public boolean isTollFree() {
        char c = number.charAt(1);
        if (c == '8') {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return number;
    }

    public boolean equals(PhoneNumber other) {
        return number.equals(other.toString());
    }
}

////////////////////////////////////////////////////////////////////////
class ExtendedPhoneNumber extends PhoneNumber {

    private String description;

    public ExtendedPhoneNumber(String number, String description) {
        super(number);
        this.description = description;
    }

    @Override
    public String toString() {
        return getDescription() + ": " + getNumber();
    }

    public String getDescription() {
        return this.description;
    }

    public static ExtendedPhoneNumber read(Scanner sc) throws IOException {
        String description = sc.next();
        String number = sc.next();
        return new ExtendedPhoneNumber(number, description);
    }
}