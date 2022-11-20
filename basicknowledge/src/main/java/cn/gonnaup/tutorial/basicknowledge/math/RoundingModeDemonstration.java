package cn.gonnaup.tutorial.basicknowledge.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author gonnaup
 * @version created at 2022/10/24 上午10:52
 */
public class RoundingModeDemonstration {

    private static final Logger logger = LoggerFactory.getLogger(RoundingModeDemonstration.class);

    public static void roundingModeFeature() {
        BigDecimal bigDecimal = BigDecimal.valueOf(-1.4);
        logger.info("-1.4 UP => {}", bigDecimal.setScale(0, RoundingMode.UP).doubleValue());
        logger.info("-1.4 DOWN => {}", bigDecimal.setScale(0, RoundingMode.DOWN).doubleValue());
    }

    public static void main(String[] args) {
        roundingModeFeature();
    }

}
