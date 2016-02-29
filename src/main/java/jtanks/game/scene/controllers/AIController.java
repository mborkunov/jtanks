package jtanks.game.scene.controllers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.scene.units.Tank;

public class AIController extends Controller {

    @Override
    public void update(long time) {
        Tank tank = (Tank) node;

        if (tank.locked()) {
            return;
        }

        if (new Random().nextFloat() < 0.05) {
            try {
                Motion motion = tank.getMotion();
                Motion.Direction[] directions = Motion.Direction.values();
                if (directions.length > 0) {
                    int index = (int) ((Math.random() - 0.01) * directions.length);
                    Motion.Direction direction = directions[index];
                    motion.setDirection(direction);
                }
                motion.setSpeed(2);
            } catch (Exception ex) {
                String msg = "Exception has occurred while updating enemy unit state";
                Logger.getLogger(AIController.class.getName()).log(Level.SEVERE, msg, ex);
            }
        }
        if (new Random().nextFloat() < 0.7) {
            tank.shoot();
        }
    }

}
