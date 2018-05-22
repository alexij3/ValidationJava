import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> ipAddresses = Arrays.asList(
                "0.0.0.0",
                "255.0.255.255",
                "a.b.c.ddd",
                "1.2.3.456",
                "1a.23.0.255",
                "012.01.0.0"
        );

        System.out.println("[IP]");
        List<String> hostNames = Arrays.asList(
                "https://another-host-name.org.ua:8080",
                "http://host-name.cm.ua.org",

                "hostname.ua",
                "http://0372.ua",
                "asodaosdo"
        );

        List<String> names = Arrays.asList(
                "Alex",
                "123",
                "Alex123",
                "Andrew",
                "O. J. Smith"
        );

        List<String> emails = Arrays.asList(
                "something@gmail.com",
                "123sooo@mail.ru",
                "soo-soo.fo@@yandex.ru",
                "..gllgl",
                "@mail.ru"
        );

        List<String> sockets = Arrays.asList(
                "http://socketone.com:60000",
                "http://localhost:8080",
                "https://-socket:8080",
                "http://sok3t",
                "http://soo#::08"
        );

        for (String str : ipAddresses)
            System.out.println(str + " is IP: " + Validator.isIP(str));

        System.out.println();
        System.out.println("[Host names]");

        for (String str : hostNames)
            System.out.println(str + " is a host name: " + Validator.isHost(str));

        System.out.println();
        System.out.println("[Names]");
        for (String str : names)
            System.out.println(str + " is name: " + Validator.isName(str));

        System.out.println();
        System.out.println("[Emails]");
        for (String str : emails)
            System.out.println(str + " is an email: " + Validator.isEmail(str));

        System.out.println();
        System.out.println("[Sockets]");
        for (String str : sockets)
            System.out.println(str + " is a socket: " + Validator.isSocket(str));
    }
}

class Validator{
    public static boolean isIP(String ip){
        List<String> ipArr = new ArrayList<>();
        String stringToAdd;
        for (int i = 0; i < ip.length(); i++){
            stringToAdd = "";
            int periodIndex = ip.indexOf('.', i);
            if (periodIndex != -1 && periodIndex != ip.length()-1){
                for (int j = i; j < periodIndex; j++){
                    stringToAdd += ip.charAt(j);
                }
                ipArr.add(stringToAdd);
                i = periodIndex;
            }else if (periodIndex == -1){
                if (i == ip.length()) ipArr.add(("" + ip.charAt(i)));
                else{
                    stringToAdd = "";
                    for (int j = i; j < ip.length(); j++){
                        stringToAdd += ip.charAt(j);
                    }
                    ipArr.add(stringToAdd);
                    break;
                }
            }
        }

        if (ipArr.size() < 4 || ipArr.size() > 4){
            return false;
        }else{
            for (String str : ipArr){
                if (str.charAt(0) == '0' && str.length() > 1) return false;
                    try {
                        Integer strNumber = Integer.parseInt(str);
                        if (strNumber < 0 || strNumber > 255) return false;
                    }catch(NumberFormatException e){
                        return false;
                    }
            }
        }

        return true;
    }

    public static boolean isHost(String hostName){
        if (!hostName.startsWith("http://") && !hostName.startsWith("https://")) return false;
        if (!hostName.contains(".")) return false;

        int firstCharIndex = hostName.lastIndexOf('/')+1;

        int periodIndex = hostName.indexOf('.', firstCharIndex);
        if (periodIndex == firstCharIndex) return false;

        if (hostName.substring(firstCharIndex).length() > 253) return false;

        String hostNamePart = hostName.substring(firstCharIndex, periodIndex);
        if (!(Character.isLetterOrDigit(hostNamePart.charAt(0)))) return false;
        char [] hostArr = hostNamePart.toCharArray();

        for (char c : hostArr)
            if (!Character.isLetterOrDigit(c) && c != '-') return false;

        firstCharIndex = periodIndex+1;


        while(true){
            periodIndex = hostName.indexOf('.', firstCharIndex);
            if (periodIndex == firstCharIndex) return false;

            if (periodIndex == -1){
                if (firstCharIndex != hostName.length()){
                    hostNamePart = hostName.substring(firstCharIndex);

                    if (!hostNamePart.chars().allMatch(Character::isLetter)) return false;

                    if (hostNamePart.length() > 63) return false;
                    if (hostNamePart.length() < 2) return false;

                    break;
                }
                else break;
            }

            hostNamePart = hostName.substring(firstCharIndex, periodIndex);
            if (!hostNamePart.chars().allMatch(Character::isLetterOrDigit)) return false;

            if (hostNamePart.length() > 63) return false;
            if (hostNamePart.length() < 2) return false;

            if (!(hostNamePart.chars().allMatch(Character::isLowerCase) && hostNamePart.chars().allMatch(Character::isLetterOrDigit))
                    && !hostNamePart.contains("-")) return false;
            if (hostName.charAt(hostName.length()-1) == '.') return false;

            firstCharIndex = periodIndex+1;

        }

        return true;
    }

    public static boolean isEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        if (!Character.isLetterOrDigit(email.charAt(0))) return false;
        if (!Character.isLetter(email.charAt(email.length()-1))) return false;
        if (email.chars().filter(el -> el == '@').count() > 1) return false;
        if (email.contains("..")) return false;

        String subEmail = email.substring(0, email.indexOf("@"));
        String [] subEmailSplit = subEmail.split("\\.");

        for (String str : subEmailSplit)
            if (!str.chars().allMatch(Character::isLetterOrDigit)) return false;

        String subEmailAfterAt = email.substring(email.indexOf("@"));
        String [] subEmailAfterAtSplit = subEmailAfterAt.split("\\.");

        if (subEmailAfterAtSplit.length < 2) return false;

        for (String str : subEmailSplit)
            if (!str.chars().allMatch(Character::isLetterOrDigit)) return false;


        return true;
    }

    public static boolean isName(String name){
        return name.chars().allMatch(Character::isLetter);
    }

    public static boolean isSocket(String socket){
        if (!socket.contains("/")) return false;
        if (!socket.startsWith("http://") && !socket.startsWith("https://")) return false;
        if (!Character.isDigit(socket.charAt(socket.length()-1))) return false;
        if (!socket.contains(":")) return false;
        if (socket.contains("::")) return false;
        if (socket.contains("..")) return false;

        String subSocket = socket.substring(socket.lastIndexOf("/")+1, socket.lastIndexOf(":"));
        String [] subSocketSplit;
        if (!subSocket.contains(".")){
            return subSocket.equals("localhost");
        }

        subSocketSplit = subSocket.split("\\.");;
        for (String str : subSocketSplit)
            if (!str.chars().allMatch(Character::isLetterOrDigit)) return false;

        String afterDoublePoint = socket.substring(socket.lastIndexOf(":")+1);
        try{
            if (Integer.parseInt(afterDoublePoint) < 0 || Integer.parseInt(afterDoublePoint) > 65535) return false;
        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }
}

