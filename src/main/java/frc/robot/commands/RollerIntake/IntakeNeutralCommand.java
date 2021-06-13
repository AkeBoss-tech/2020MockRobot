package frc.robot.commands.RollerIntake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RollerIntake;

public class IntakeNeutralCommand extends CommandBase {

    private RollerIntake intake;

    public IntakeNeutralCommand(RollerIntake intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intake.neutral();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}