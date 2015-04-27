package ncBrowse;

/**
 * Constants for orientations (and alignments).<p>
 *
 * This class may not be instantiated.
 *
 * @version 1.0, Apr 11 1996
 * @author  David Geary
 */
public class Orientation {
    public static final Orientation BAD    = new Orientation();
    public static final Orientation NORTH  = new Orientation();
    public static final Orientation SOUTH  = new Orientation();
    public static final Orientation EAST   = new Orientation();
    public static final Orientation WEST   = new Orientation();
    public static final Orientation CENTER = new Orientation();
    public static final Orientation TOP    = new Orientation();
    public static final Orientation LEFT   = new Orientation();
    public static final Orientation RIGHT  = new Orientation();
    public static final Orientation BOTTOM = new Orientation();

    public static final Orientation HORIZONTAL = 
        new Orientation();
    public static final Orientation VERTICAL   = 
        new Orientation();

    static public Orientation fromString(String s) {
        Orientation o = BAD;

        switch (s) {
            case "NORTH":
            case "north":
                o = NORTH;
                break;
            case "SOUTH":
            case "south":
                o = SOUTH;
                break;
            case "EAST":
            case "east":
                o = EAST;
                break;
            case "WEST":
            case "west":
                o = WEST;
                break;
            case "CENTER":
            case "center":
                o = CENTER;
                break;
            case "TOP":
            case "top":
                o = TOP;
                break;
            case "LEFT":
            case "left":
                o = LEFT;
                break;
            case "RIGHT":
            case "right":
                o = RIGHT;
                break;
            case "BOTTOM":
            case "bottom":
                o = BOTTOM;
                break;
            case "VERTICAL":
            case "vertical":
                o = VERTICAL;
                break;
            case "HORIZONTAL":
            case "horizontal":
                o = HORIZONTAL;
                break;
        }

        return o;
    }
    public String toString() {
        String s = "";

        if(this == Orientation.NORTH)
            s = getClass().getName() + "=NORTH";
        else if(this == Orientation.SOUTH)
            s = getClass().getName() + "=SOUTH";
        else if(this == Orientation.EAST)
            s = getClass().getName() + "=EAST";
        else if(this == Orientation.WEST)
            s = getClass().getName() + "=WEST";
        else if(this == Orientation.CENTER)
            s = getClass().getName() + "=CENTER";
        else if(this == Orientation.TOP)
            s = getClass().getName() + "=TOP";
        else if(this == Orientation.LEFT)
            s = getClass().getName() + "=LEFT";
        else if(this == Orientation.RIGHT)
            s = getClass().getName() + "=RIGHT";
        else if(this == Orientation.BOTTOM)
            s = getClass().getName() + "=BOTTOM";
        else if(this == Orientation.HORIZONTAL)
            s = getClass().getName() + "=HORIZONTAL";
        else if(this == Orientation.VERTICAL)
            s = getClass().getName() + "=VERTICAL";
        else if(this == Orientation.BAD)
            s = getClass().getName() + "=BAD";

        return s;
    }
    private Orientation() { }  // Defeat instantiation
}

