package frc.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.drivetrain.ManualDriveCommand;
import frc.robot.commands.drivetrain.ToggleReverseCommand;
import frc.robot.commands.drivetrain.ToggleSlowTurnCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SnailSubsystem;
import frc.robot.util.SnailController;

import java.util.ArrayList;

// Commands
import frc.robot.commands.Arm.ArmManualCommand;
import frc.robot.commands.Arm.ArmPIDCommand;
import frc.robot.commands.Elevator.ElevatorManualCommand;
import frc.robot.commands.Elevator.ElevatorPIDCommand;
import frc.robot.commands.RollerIntake.IntakeEjectingCommand;
import frc.robot.commands.RollerIntake.IntakeIntakingCommand;
import frc.robot.commands.RollerIntake.IntakeNeutralCommand;

// Subsystems
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.RollerIntake;

// Constants
import static frc.robot.Constants.ElectricalLayout.CONTROLLER_DRIVER_ID;
import static frc.robot.Constants.ElectricalLayout.CONTROLLER_OPERATOR_ID;
import static frc.robot.Constants.UPDATE_PERIOD;;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the Robot
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // Controllers
    private SnailController driveController;
    private SnailController operatorController;
    
    private ArrayList<SnailSubsystem> subsystems;

    // Subsystems
    private RollerIntake rollerIntake;
    private Arm arm;
    private Elevator elevator;
    private Drivetrain drivetrain;

    // idk what this is
    private Notifier updateNotifier;
    private int outputCounter;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveController = new SnailController(CONTROLLER_DRIVER_ID);
        operatorController = new SnailController(CONTROLLER_OPERATOR_ID);

        configureSubsystems();
        configureAutoChoosers();
        configureButtonBindings();

        outputCounter = 0;

        SmartDashboard.putBoolean("Testing", false);

        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(UPDATE_PERIOD);
    }

    /**
     * Declare all of our subsystems and their default bindings
     */
    private void configureSubsystems() {
        // declare each of the subsystems here
        // Roller Intake
        rollerIntake = new RollerIntake();
        rollerIntake.setDefaultCommand(new IntakeNeutralCommand(rollerIntake));

        // Arm
        arm = new Arm();
        arm.setDefaultCommand(new ArmManualCommand(arm, () -> {
            return operatorController.getLeftY();
        }));

        // Elevator
        elevator = new Elevator();
        elevator.setDefaultCommand(new ElevatorManualCommand(elevator, () -> {
            return operatorController.getRightY();
        }));

        drivetrain = new Drivetrain();
        drivetrain.setDefaultCommand(new ManualDriveCommand(drivetrain, driveController::getDriveForward,
            driveController::getDriveTurn));

        // Adding the subsystems to the list
        subsystems = new ArrayList<>();
        subsystems.add(rollerIntake);
        subsystems.add(arm);
        subsystems.add(elevator);
        // add each of the subsystems to the arraylist here

        subsystems.add(drivetrain);
    }

    /**
     * Define button -> command mappings.
     */
    private void configureButtonBindings() {
        // Operator Controller
        // Roller Intake
        operatorController.getButton(Button.kB.value).whileActiveOnce(new IntakeEjectingCommand(rollerIntake));
        operatorController.getButton(Button.kX.value).whileActiveOnce(new IntakeIntakingCommand(rollerIntake));
        
        // Arm (PID)
        operatorController.getButton(Button.kY.value).whileActiveOnce(new ArmPIDCommand(arm, Constants.Arm.TOP));
        operatorController.getButton(Button.kA.value).whileActiveOnce(new ArmPIDCommand(arm, Constants.Arm.DOWN));

        // Drive Controller
        // Elevator (PID)
        operatorController.getButton(Button.kStart.value).whileActiveOnce(new ElevatorPIDCommand(elevator, Constants.Elevator.TOP));
        operatorController.getButton(Button.kBack.value).whileActiveOnce(new ElevatorPIDCommand(elevator, Constants.Elevator.DOWN));

        driveController.getButton(Button.kStart.value).whenPressed(new ToggleReverseCommand(drivetrain));
        driveController.getButton(Button.kBack.value).whenPressed(new ToggleSlowTurnCommand(drivetrain));
    }

    /**
     * Set up the choosers on shuffleboard for autonomous
     */
    public void configureAutoChoosers() {
        
    }

    /**
     * Do the logic to return the auto command to run
     */
    public Command getAutoCommand() {
        return null;
    }

    /**
     * Update all of the subsystems
     * This is run in a separate loop at a faster rate to:
     * a) update subsystems faster
     * b) prevent packet delay from driver station from delaying response from our robot
     */
    private void update() {
        for(SnailSubsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    public void displayShuffleboard() {
        if(outputCounter % 3 == 0) {
            subsystems.get(outputCounter / 3).displayShuffleboard();
        }

        outputCounter = (outputCounter + 1) % (subsystems.size() * 3);
    }

    public void tuningInit() {
        for(SnailSubsystem subsystem : subsystems) {
            subsystem.tuningInit();
        }
    }

    public void tuningPeriodic() {
        if(outputCounter % 3 == 0) {
            subsystems.get(outputCounter / 3).tuningPeriodic();
        }
    }
}
