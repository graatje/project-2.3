package Connection;


public class CommunicationHandler {


    public void handleServerInput(String input) {
        //TODO: Server gave a command, take action accordingly.
        System.out.println(input);

        if (input.equals("OK")) return;

        String[] split = input.split(" ");



    }
}
