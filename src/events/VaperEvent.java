//package events;
//
//import controllers.SmokeSprite;
//import controllers.Vaper;
//
//import javax.swing.*;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class VaperEvent extends JPanel {
//    private Vaper v;
//    boolean lost = false;
//    private int secondsPassed = 0;
//    private Timer timer = new Timer();
////    private SmokeSprite smoke;
//
//    public VaperEvent(Vaper v, String imagePATH, int delay) {
//        this.v = v;
//        this.add(v);
//        v.setVisible(false);
//
//        //this.add(smoke);
//        //implement smoky door
//
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                checkClickStatus();
//            }
//        }, 0, 1000); // Initial delay of 0 ms, repeat every 1000 ms (1 second)
//    }
//
//    private void checkClickStatus() {
//        if (v.isClicked) {
//            timer.cancel();
//        } else {
//            secondsPassed++;
//            if (secondsPassed >= 30) {
//                this.timesUp();
//                timer.cancel();
//            }
//        }
//    }
//
//    public void timesUp() {
//        lost = true;
//    }
//
//    private void OpenedDoor() {
//        v.setVisible(true);
//    }
//}
//
