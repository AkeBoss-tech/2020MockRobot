package frc.robot.commands.Elevator;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ElevatorManualCommand extends CommandBase {
    // I copied this code from the arm manual command
    private final Elevator elevator;
    private final DoubleSupplier speedSupplier;

    public ElevatorManualCommand(Elevator subsystem, DoubleSupplier ControllerInput) {
        elevator = subsystem;
        speedSupplier = ControllerInput;
        addRequirements(elevator);
    }

    @Override
    public void execute() {
        elevator.setSpeed(speedSupplier.getAsDouble());
    }

    // Extra Commands
    @Override
    public void initialize() {}

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }


}
