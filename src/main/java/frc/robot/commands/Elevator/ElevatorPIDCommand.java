package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ElevatorPIDCommand extends CommandBase {
    private final Elevator elevator;
    private final double setpoint;

    public ElevatorPIDCommand(Elevator subsystem, double ControllerInput) {
        elevator = subsystem;
        setpoint = ControllerInput;
        addRequirements(elevator);
    }

    @Override
    public void execute() {}

    // Extra Commands
    @Override
    public void initialize() {
        elevator.setPosition(setpoint);
    }

    @Override
    public void end(boolean interrupted) {
        elevator.endPID();
    }

    @Override
    public boolean isFinished() {
        return elevator.getState() != Elevator.State.PID;
    }

}
