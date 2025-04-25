package com.example.demo.command;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class CommandInvoker {
   
    private Stack<Command> commandHistory = new Stack<>();

    
    private Map<String, Command> undoCommand = new HashMap<>();
    private Timer timer = new Timer(true);

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }
    
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
            System.out.println("Undoing command: " + lastCommand.getClass().getSimpleName());
        }
    }
    
  
    
    public void clearHistory() {
        commandHistory.clear();
    }


    public String executeCommandWithTimeOut( Command command, Integer timeOuts)
    {
            command.execute();
            commandHistory.push(command);
            String commandId = UUID.randomUUID().toString();
            undoCommand.put(commandId, command);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (undoCommand.containsKey(commandId)) {
                        undoCommand.remove(commandId);
                        System.out.println("Command with ID " + commandId + " has timed out and is removed.");
                    }
                }
            }, timeOuts );
            return commandId;
    }
    public Boolean undoCommandWithTimeOut(String commandId) {
        Command command = undoCommand.get(commandId);
        if (command != null) {
            command.undo();
            undoCommand.remove(commandId);
            if(commandHistory.contains(command))
            {
                commandHistory.remove(command);
            }
            System.out.println("Undoing command with ID: " + commandId);
            return true;
        } else {
            System.out.println("No command found with ID: " + commandId);
        }
        return false;
    }
}