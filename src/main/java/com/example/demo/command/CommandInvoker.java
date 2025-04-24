package com.example.demo.command;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CommandInvoker {
   // private Queue<Command> commandQueue = new LinkedList<>();
    private Stack<Command> executedCommands = new Stack<>();
    
    // public void queueCommand(Command command) {
    //     commandQueue.add(command);
    // }
    
    // public void executeAllCommands() {
    //     while (!commandQueue.isEmpty()) {
    //         Command command = commandQueue.poll();
    //         command.execute();
    //         executedCommands.push(command);
    //     }
    // }
    
    public void executeCommand(Command command) {
        command.execute();
        executedCommands.push(command);
    }
    
    public void undoLastCommand() {
        if (!executedCommands.isEmpty()) {
            Command lastCommand = executedCommands.pop();
            lastCommand.undo();
            System.out.println("Undoing command: " + lastCommand.getClass().getSimpleName());
        }
    }
    
    // public void clearQueue() {
    //     commandQueue.clear();
    // }
    
    public void clearHistory() {
        executedCommands.clear();
    }
}