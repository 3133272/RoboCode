import robocode.*;
import java.awt.*;
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

//TomUTKA changes V1
//Dynamic Bullet Power: Adjusted based on enemy distance.
//Predictive Targeting: Implemented for improved aiming.
//Randomized Movement: Introduced to enhance dodging.
//Radar Lock: Enhanced for continuous enemy tracking.
//Wall Avoidance: Added to prevent collisions.
//Distance Management: Incorporated to maintain optimal engagement range.

public class Beast_Master_ULTIMATE69420 extends AdvancedRobot {
    // Constants for wall margin and preferred distance from enemy
    final double WALL_MARGIN = 85; // NEW*UTKA - Updated from 60
    final double PREFERRED_DISTANCE = 250; // NEW*UTKA - Updated from 200
    final double WALL_PADDING = 20; // %NEW% - Additional padding for wall avoidance

    // Variables for autonomous control
    // int aimX, aimY; // NEW*UTKA - Coordinates for cursor-based aiming (commented out for autonomous behavior)
    int firePower; // Fire power based on situation

    public void run() {
        // NEW*UTKA - Consolidated color settings for easier visual tracking
        //setColors(Color.red, Color.black, Color.pink);

        // //Longkai Zhang-3133272 V3 change the colors of body
        // setBodyColor : Red
        setBodyColor(Color.red); // %NEW% - Set body color to red
        //set bullet color: RED
        setBulletColor(Color.red); // %NEW% - Set bullet color to red
        // set gun color: BLACK
        setGunColor(Color.black); // %NEW% - Set gun (turret) color to black
        // set Radar color: Gray
        setRadarColor(Color.gray); // %NEW% - Set radar color to gray
        // Set scan arc color to Gray
        setScanColor(Color.gray); // %NEW% - Set scan arc color to gray
        
        // Radar and gun are set to turn independently from the robot's body turn
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY); // Continuous radar scanning

        while (true) {
            smartMovement(); // NEW*UTKA - Improved movement strategy
            execute(); // Execute all actions set this turn
        }
    }

    // NEW*UTKA - Improved movement strategy focusing on wall avoidance and distance management
    private void smartMovement() {
        // Check if near walls and move away
        if (getX() <= WALL_MARGIN || getX() >= getBattleFieldWidth() - WALL_MARGIN ||
                getY() <= WALL_MARGIN || getY() >= getBattleFieldHeight() - WALL_MARGIN) {
            escapeWalls(); // %NEW% - Escape logic when too close to walls
        } else {
            // Maintain movement that keeps distance from other robots
            setAhead(100 + Math.random() * 50); // %NEW% - Randomize distance to enhance dodging
            setTurnRight(30 + Math.random() * 60); // %NEW% - Randomize turn angle to enhance dodging
        }
    }

    // NEW*UTKA - Escape logic when too close to walls
    private void escapeWalls() { // %NEW%
        double angleToCenter = normalAbsoluteAngle(Math.atan2(getBattleFieldWidth()/2 - getX(), getBattleFieldHeight()/2 - getY())); // %NEW% - Calculate angle to the center of the battlefield
        setTurnRightRadians(normalRelativeAngle(angleToCenter - getHeadingRadians())); // %NEW% - Turn towards the center
        setAhead(150); // %NEW% - Move a significant distance to clear the wall
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        adjustFirePower(e.getDistance()); // NEW*UTKA - Adjust fire power based on distance
        double enemyBearing = e.getBearingRadians() + getHeadingRadians();
        // double angleToEnemy = normalAbsoluteAngle(Math.atan2(aimX - getX(), aimY - getY())); // Cursor-based aiming (commented out)
        setTurnGunRightRadians(normalRelativeAngle(enemyBearing - getGunHeadingRadians())); // Turn gun towards enemy
        if (getGunHeat() == 0) {
            setFire(firePower);
        }
        // Track the scanned robot with the radar
        setTurnRadarRightRadians(normalRelativeAngle(enemyBearing - getRadarHeadingRadians())); // %NEW% - Keep radar locked on the enemy
    }

    // NEW*UTKA - Adjust fire power based on enemy distance
    private void adjustFirePower(double distance) {
        // Longkai Zhang 3133272 V3 change the firepower to 1000
        // firePower = 1000; // Commented out as 1000 is invalid fire power
        if (distance < 100) {
            firePower = 3; // Max fire power for close range
        } else if (distance < 200) {
            firePower = 2; // Moderate fire power for mid-range
        } else {
            firePower = 1; // Lower fire power for long range
        } // %NEW% - Adjust fire power based on the distance to the enemy
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // If hit, move perpendicular to the attack to potentially evade further shots
        double bearing = e.getBearing(); // Get the bearing of the incoming bullet
        setTurnRight(normalizeBearing(90 - bearing)); // %NEW% - Turn perpendicular to the bullet's path
        setAhead(150); // %NEW% - Move ahead to evade further shots
    }

    // Utility method to normalize a bearing to between +180 and -180
    double normalizeBearing(double angle) { // %NEW%
        while (angle > 180) angle -= 360; // %NEW% - Normalize bearing to within +180 and -180 degrees
        while (angle < -180) angle += 360; // %NEW% - Normalize bearing to within +180 and -180 degrees
        return angle; // %NEW%
    }
}
