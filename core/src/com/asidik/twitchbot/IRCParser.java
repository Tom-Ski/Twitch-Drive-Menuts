package com.asidik.twitchbot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Javier on 03/02/2015.
 */
public class IRCParser {

    private ConcurrentHashMap<InputType, Integer> votes = new ConcurrentHashMap<InputType, Integer>();

    public enum MessageType {
        PRIVMSG
    }

    public enum InputType {
        UP, DOWN, RIGHT, LEFT
    }

    private String regexPattern = "^(:(?<prefix>\\S+) )?(?<command>\\S+)( (?!:)(?<params>.+?))?( :(?<trail>.+))?$";
    private Pattern regex = Pattern.compile(regexPattern);

    public boolean parse(String line) {
        Matcher match = regex.matcher(line);
        try {
            if (match.matches()) {
                switch (MessageType.valueOf(match.group("command"))) {
                    case PRIVMSG:
                        return parseMessage(match.group("trail"));
                    default:
                        return false;
                }
            }
        } catch (IllegalArgumentException e) { }
        return false;
    }

    public ConcurrentHashMap<InputType, Integer> getVotes() {
        return votes;
    }

    /**
     * Returns true or false depending if the output should be debugged or not
     * this is actually useless but whatever.
     * @param line
     * @return
     */
    private boolean parseMessage(String line) {
        System.out.println("Args: " + line);
        InputType type = null;
        try {
            type = InputType.valueOf(line.toUpperCase());
            if (votes.containsKey(type)) {
                votes.put(type, votes.get(type) + 1);
            } else {
                votes.put(type, 1);
            }
            return true;
        } catch (Exception e) {}
        return false;
    }

}
