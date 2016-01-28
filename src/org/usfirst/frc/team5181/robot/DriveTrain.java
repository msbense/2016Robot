package org.usfirst.frc.team5181.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	
	private double speedLimit;
	private boolean speedBool;
	Victor leftFront;
	Victor leftBack;
	Victor rightFront;
	Victor rightBack;
	RobotDrive drive;
	
	Gamepad gp;
	public DriveTrain(Gamepad gp, double speedLimit) {
		leftFront = new Victor(Statics.LEFTPortFront);
		leftBack = new Victor(Statics.LEFTPortBack);
		rightFront = new Victor(Statics.RIGHTPortFront);
		rightBack = new Victor(Statics.RIGHTPortBack);
		
		drive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		
		this.gp = gp;
		this.speedLimit = speedLimit;
	}
	
	/**
	 * Single joystick to control robot drive
	 * @param controlStickX magnitude of turning axis
	 * @param controlStickY magnitude of driving axis
	 */
	public void ArcadeDrive(double controlStickX, double controlStickY) {
		double xRotation = controlStickX;
		double yDrive = controlStickY;
		
		xRotation = Filters.powerCurving(controlStickX, 0.75); //filter rotational values
		
		if(Math.abs(yDrive) >= speedLimit) {
			yDrive = 	(Math.abs(yDrive)    /    yDrive) * speedLimit;
			DriverStation.reportError("" +speedLimit, false);
		}
		if(Math.abs(xRotation) >= speedLimit) {
			xRotation = (Math.abs(xRotation) / xRotation) * speedLimit;
		}
		
		drive.arcadeDrive(-yDrive, -(xRotation));
	}
	
	public void tankDrive(double controlStickLeft, double controlStickRight) {
		if(controlStickLeft >= speedLimit) {
			controlStickLeft = speedLimit;
		}
		if(controlStickRight >= speedLimit) {
			controlStickRight = speedLimit;
		}
		
		drive.tankDrive(controlStickLeft, controlStickRight);
	}
	public void updateSpeedLimit() {

		//Speed Limit Control
		if(gp.RIGHT_Bumper_State && !speedBool) {
			speedLimit += 0.1;
			speedBool = true;
		}
		else if(gp.B_Button_State) {
			speedLimit = 0;
		}
		else if(gp.LEFT_Bumper_State && !speedBool) {
			speedLimit -= 0.1;
			speedBool = true;
		}
		else if(!gp.LEFT_Bumper_State && !gp.RIGHT_Bumper_State && speedBool) {
			speedBool = false;
		}
		
	}
	
}
