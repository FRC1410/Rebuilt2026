package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Controller;


public class ReadyToRumbleCommand extends Command {

    private Controller controller;
    private Timer timer;
    private int time = 0;

    public ReadyToRumbleCommand(Controller controller) {
        this.controller = controller;
        this.timer = new Timer("GameTimer");
    }

    private class Tick extends TimerTask{
        @Override
        public void run(){
            time += 1;
            switch (time) {
                case 25 -> controller.rumble(1000);
                case 40 -> controller.rumble(1000);
                case 55 -> controller.rumble(1000);
                case 70 -> controller.rumble(1000);
                default -> {
                }
            }
        }
    }


    @Override
    public void initialize() {
        Tick tick = new Tick();
        this.timer.schedule(tick, new Date(), 1000);
    }
}
