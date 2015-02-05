package com.asidik.twitchbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Javier on 03/02/2015.
 */
public class IRCParser {

    InputManager manager;

    public IRCParser (InputManager manager) {
        this.manager = manager;
    }

    private String regexPattern = "^(:(?<prefix>\\S+) )?(?<command>\\S+)( (?!:)(?<params>.+?))?( :(?<trail>.+))?$";
    private Pattern regex = Pattern.compile(regexPattern);

    public boolean parse (String line) {
        Matcher match = regex.matcher(line);
        try {
            if (match.matches()) {
                switch (MessageType.valueOf(match.group("command"))) {
                    case PRIVMSG:
                        return parseMessage(match.group("prefix").split("!")[0], match.group("trail"));
                    default:
                        return false;
                }
            }
        } catch (IllegalArgumentException e) { }
        return false;
    }


    private void addVote (UserInput userInput) {
        manager.registerVote(userInput);
    }

    /**
     * Returns true or false depending if the output should be debugged or not
     * this is actually useless but whatever.
     * @param line
     * @return
     */
    private boolean parseMessage(String user, String line) {
        System.out.println("Args: " + line);
        InputType type = null;
        try {
            type = InputType.valueOf(line.toUpperCase());
            UserInput input = new UserInput(user, type);
            System.out.println(user);
            addVote(input);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public enum MessageType {
        PRIVMSG
    }

}
