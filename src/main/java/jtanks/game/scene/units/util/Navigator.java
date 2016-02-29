package jtanks.game.scene.units.util;

import java.util.ArrayList;
import jtanks.game.map.Map;

public class Navigator {

    //private Unit unit;
    //private Field field;
    private Map map;

  //  public Navigator(Unit unit) {
//        this.unit = unit;
        //field = ((Battle) JTanks.getInstance().getGameState().getScreen()).getField();
        //map = field.getMap();
    //}
    
    public Motion.Direction[] getAvailableDirections() {
        ArrayList<Motion.Direction> directions = new ArrayList<Motion.Direction>();

        for (Motion.Direction direction : Motion.Direction.values()) {
            /*if (getBarrier(direction).canPass(unit)) {
                directions.add(direction);
            }*/
        }

        /*Byte[] res = new Byte[directions.size()];
        byte[] result = new byte[directions.size()];
        directions.toArray(res);
        
        for (int i = 0; i < result.length; i++) {
            result[i] = res[i].byteValue();
        }*/
        return directions.toArray(new Motion.Direction[directions.size()]);
    }
   /*
    public Unit getUnit() {
        return null;
    }

    public Barrier getBarrier() {
        return getBarrier(unit.getMotion().getDirection());
    }

    public Barrier getBarrier(Motion.Direction direction) {
        Barrier barrier = new Barrier();
        Position position = unit.getPosition();
        Motion motion = unit.getMotion();
        
        float areaSize = 1f; //field.getView().getAreaSize();
        float halfArea = 0.5f; //(int) Math.round(areaSize / 2.0);
        
        float x = (float) position.getX();
        float y = (float) position.getY();

        float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        
        float fault = unit.fault / 2f;
        float speed = (float) motion.getSpeed(true);

        switch (direction) {
            case EAST:
                x += areaSize + speed - fault;
                y += halfArea;
                y1 = (int) (y + halfArea - fault);
                y2 = (int) (y - halfArea + fault);
                break;
            case SOUTH:
                x += halfArea;
                y += areaSize + speed - fault;
                x1 = (int) (x - halfArea + fault);
                x2 = (int) (x + halfArea - fault);
                break;
            case NORTH:
                x += halfArea;
                y -= (speed - fault);
                x1 = (int) (x - halfArea + fault);
                x2 = (int) (x + halfArea - fault);
                break;
            case WEST:
                y += halfArea;
                x -= (speed - fault);
                y1 = (int) (y + halfArea - fault);
                y2 = (int) (y - halfArea + fault);
                break;
            default:
                break;
        }

        if (x < 0 || y < 0) {
            barrier.add(new OffScreen());
            barrier.add(new OffScreen());
            barrier.add(new OffScreen());
            return barrier;
        }

        return barrier;
    }*/
}
