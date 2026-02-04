package player.skills;

import player.Controller;
import player.Player;

public abstract class Skill {

    protected double coolDownTime;
    protected double coolDown;

    protected boolean active;
    protected boolean ready;

    public abstract void updateTimer(double dt);

    public abstract void update(double dt, Player player);

    public abstract void handleInputs(Controller c);
}
