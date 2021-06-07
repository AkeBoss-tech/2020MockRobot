package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class TurnAngleCommand extends CommandBase {
    
    private final Drivetrain drivetrain;
    private final double angle;

    public TurnAngleCommand(Drivetrain drivetrain, double angle) {
        this.drivetrain = drivetrain;
        this.angle = angle;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.turnAngle(angle);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.endPID();
    }

    @Override
    public boolean isFinished() {
        return drivetrain.getState() != Drivetrain.State.TURN;
    }
}