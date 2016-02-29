package jtanks.game.scene.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import jtanks.game.scene.units.Enemy;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.geometry.Model;
import jtanks.game.geometry.Position;
import jtanks.game.map.Map;
import jtanks.game.scene.Node;
import jtanks.game.scene.gameplay.BattleField;
import jtanks.game.scene.units.Tank;
import jtanks.game.scene.units.Unit;

public class UnitController extends Controller {

    private int tankRotateSpeed = 360; // Degrees per second

    private Unit unit;

    @Override
    public void register(Node node) {
        super.register(node);

        if (node instanceof Unit) {
            this.unit = (Unit) node;
        } else {
            throw new IllegalArgumentException("Controller can use only units");
        }
    }

    @Override
    public void update(long time) {
        if (unit.locked()) {
            return;
        }

        if (unit instanceof Tank) {
            Tank tank = ((Tank) unit);

            if (tank.rotating()) {
                rotate(tank, time);
            }
        }

        double x = unit.getModel().getPosition().getX();
        double y = unit.getModel().getPosition().getY();

        double nextX = x, nextY = y;

        double speed = unit.getMotion().getSpeed();

        if (time > 1000) {
            Logger.getAnonymousLogger().warning("Slow performance. Game process may be corrupted.");
        }
        speed *= time / (double) 1000;
        switch (unit.getMotion().getDirection()) {
            case NORTH:
                nextY -= speed;
                break;
            case SOUTH:
                nextY += speed;
                break;
            case WEST:
                nextX -= speed;
                break;
            case EAST:
                nextX += speed;
                break;
        }

        Position position = unit.getModel().getPosition();
        Motion.Direction direction;
        if ((direction = isOffscreen(nextX, nextY)) != null) {
            unit.offscreenAction(direction);
        } else {
            unit.lock.lock();
            try {
                position.setX(nextX);
                position.setY(nextY);

                // checking collisions
                List<Node> nodes;
                if ((nodes = unit.hasCollision(unit.getRoot())).size() > 0) {
                    unit.highlight = true;
                    for (Node item : nodes) {
                        item.highlight = true;
                        item.affect(unit);
                    }
                }

                boolean rotating = (unit instanceof Tank && ((Tank) unit).rotating());

                if (unit.getMotion().isLocked() || rotating) {
                    unit.getModel().getPosition().setX(x);
                    unit.getModel().getPosition().setY(y);
                }
            } finally {
                unit.lock.unlock();
            }
        }
    }

    private Motion.Direction isOffscreen(double nextX, double nextY) {
        Map map = ((BattleField) unit.getRoot()).map;

        Model model = unit.getModel(unit.getMotion().getDirection());
        double offsetX = model.getOffset().getX();
        double offsetY = model.getOffset().getY();
        double modelWidth = model.getWidth();
        double modelHeight = model.getHeight();

        boolean x1, y1, x2, y2;
        x1 = nextX + offsetX < 0;
        y1 = nextY + offsetY < 0;
        x2 = nextX + modelWidth + offsetX  >= map.getWidth();
        y2 = nextY + modelHeight + offsetY >= map.getHeight();
        
        Motion.Direction direction = null;

        if (x1) {
            direction = Motion.Direction.WEST;
        } else if (x2) {
            direction = Motion.Direction.EAST;
        } else if (y1) {
            direction = Motion.Direction.NORTH;
        } else if (y2) {
            direction = Motion.Direction.SOUTH;
        }

        return (x1 || y1 || x2 || y2) ? direction : null;
    }

    private void rotate(Tank tank, long time) {
        int index = Arrays.asList(Motion.Direction.values()).indexOf(tank.getMotion().getDirection());

        int currentAngle = tank.getAngle();
        int desiredAngle = index * 90;

        boolean clockwise;

        int deltaAngle;
        if (desiredAngle > currentAngle) {
            deltaAngle = desiredAngle - currentAngle;
            clockwise = desiredAngle - currentAngle <= currentAngle + 360 - desiredAngle;
        } else {
            deltaAngle = currentAngle - desiredAngle;
            clockwise = currentAngle - desiredAngle > desiredAngle + 360 - currentAngle;
        }

        int rotateAngle = (int) (tankRotateSpeed / 1000f * time);
        if (tank instanceof Enemy) {
            rotateAngle *= 1.3;
        }

        if (deltaAngle < rotateAngle) {
            rotateAngle = deltaAngle;
        }

        int angle = tank.getAngle() + (clockwise ? rotateAngle : - rotateAngle);

        while (angle >= 360) {
            angle -= 360;
        }

        while (angle < 0) {
            angle = 360 + angle;
        }

        tank.setAngle(angle);
    }
}
