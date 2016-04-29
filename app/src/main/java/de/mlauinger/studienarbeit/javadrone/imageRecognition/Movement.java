package de.mlauinger.studienarbeit.javadrone.imageRecognition;

import de.mlauinger.studienarbeit.javadrone.controller.DroneController;

public class Movement {

/*
Values:
    255
    255
    230
    224
    157
    0

 */
    private static final int CIRCLE_SIZE = 4;
    private static final int PERSENTAGE = 20;
    static volatile int counter = 0;

    public void followCircle(CirclePos circle, DroneController controller) {
        //Circle not detected
        counter++;
        if (counter < 2) {
            return;
        }
        counter = 0;
        if (circle.getPosx() < 2 && circle.getPosy() < 2) {
            return;
        }

        //Circle not in the range of fuzziness
        if (calculateDeltaHor(circle) > circle.getWidth() / PERSENTAGE) {

            //Turn left to move Circle to the Center of the Image
            if (circle.getPosx() - circle.getWidth() / 2 < 0) {
                System.out.println("Turn left");
                controller.turnDrone(0.005f);
            }

                //Turn right to move Circle to the Center of the Image
                if (circle.getPosx() - circle.getWidth() / 2 > 0) {
                    System.out.println("Turn right");
                    controller.turnDrone(-0.005f);
                }

        }

        //check if Circle radius is not in the fuzziness
        if (circle.getRadius() < calculateMinRadius(circle) || circle.getRadius() > calculateMaxRadius(circle)) {

            //get closer to Circle to adjust the size of the circle
            if (circle.getRadius() < circle.getWidth() / CIRCLE_SIZE) {
                System.out.println("move closer");
                controller.flyDrone(-0.005f, 0);
                return;
            }


            //move away to Circle to adjust the size of the circle
            if (circle.getRadius() > circle.getWidth() / CIRCLE_SIZE) {
                System.out.println("move away");
                controller.flyDrone(0.005f, 0);
                return;
            }
        }

    }

    private int calculateMaxRadius(CirclePos circle) {
        int maxRadius;
        maxRadius = circle.getWidth() / CIRCLE_SIZE + circle.getWidth() / PERSENTAGE;
        return maxRadius;
    }

    private int calculateMinRadius(CirclePos circle) {
        int minRadius;
        minRadius = circle.getWidth() / CIRCLE_SIZE - circle.getWidth() / PERSENTAGE;
        return minRadius;
    }

    private int calculateDeltaHor(CirclePos circle) {
        int delta;
        delta = circle.getPosx() - circle.getWidth() / 2;
        if (delta < 0) {
            delta = delta * (-1);
        }
        return delta;
    }
}
