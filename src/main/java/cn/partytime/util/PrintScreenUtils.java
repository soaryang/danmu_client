package cn.partytime.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by administrator on 2016/12/9.
 */
@Slf4j
public class PrintScreenUtils {



    public static void screenShotAsFile(String savePath) {
        try {
            Robot robot = new Robot();
            BufferedImage bfImage = robot.createScreenCapture(new Rectangle(0, 0, 1024, 1024));
            File path = new File(savePath);
            File file = new File(path, "screen.jpg");
            ImageIO.write(bfImage, "jpg", file);
        } catch (AWTException e) {
            log.error("",e);
        } catch (IOException e) {
            log.error("",e);
        }
    }


}
