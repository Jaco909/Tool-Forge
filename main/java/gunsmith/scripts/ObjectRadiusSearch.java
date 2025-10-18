package gunsmith.scripts;

import necesse.level.gameObject.RockOreObject;
import necesse.level.maps.Level;


import java.awt.*;
import java.util.ArrayList;

public class ObjectRadiusSearch {
    public void ObjectRadiusSearch() {
    }
    public static Point ObjectRadiusSearchPoint(Point startingPoint, int radius, Level level) {
        return checkGridOutwards(startingPoint,radius,level);
    }
    private static Point checkGridOutwards(Point startingPoint, int radius, Level level) {
        int startingX = startingPoint.x;
        int startingY = startingPoint.y;

        for (int r = 1; r <= radius; r++) {
            for (int x_offset = -r; x_offset <= r; x_offset++) {
                Point point = checkSquare(startingX + x_offset, startingY + r, level);
                if (point != null) {
                    return point;
                }
            }

            //Bottom edge
            for (int x_offset = -r; x_offset <= r; x_offset++) {
                Point point = checkSquare(startingX + x_offset, startingY - r, level);
                if (point != null) {
                    return point;
                }
            }

            //Left edge
            for (int y_offset = r - 1; y_offset > -r; y_offset--) {
                Point point = checkSquare(startingX - r, startingY + y_offset, level);
                if (point != null) {
                    return point;
                }
            }

            //Right edge
            for (int y_offset = r - 1; y_offset > -r; y_offset--) {
                Point point = checkSquare(startingX + r, startingY + y_offset, level);
                if (point != null) {
                    return point;
                }
            }
        }
        return null;
    }
    private static Point checkSquare(int x, int y, Level level) {
        Point checkedPoint = new Point(x,y);
        if (level.getObject(x,y) instanceof RockOreObject) {
            return checkedPoint;
        } else {
            return null;
        }
    }
}
