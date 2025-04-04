package com.example.demo.command.fraudlabel;

import java.util.ArrayList;
import java.util.List;

public class FraudLabelCommandInvoker {
    private List<FraudLabelCommand> commandsQueue = new ArrayList<>();
    public void addCommand(FraudLabelCommand command)
    {
        commandsQueue.add(command);
    }
    public void executeCommands()
    {
        for(FraudLabelCommand command : commandsQueue)
        {
            command.execute();
        }
        commandsQueue.clear();
    }

}
