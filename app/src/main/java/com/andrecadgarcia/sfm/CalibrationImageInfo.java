package com.andrecadgarcia.sfm;

import boofcv.alg.geo.calibration.CalibrationObservation;
import boofcv.struct.image.GrayF32;

/**
 * Created by Andre Garcia on 01/10/16.
 */

public class CalibrationImageInfo {
    GrayF32 image;
    CalibrationObservation calibPoints = new CalibrationObservation();

    public CalibrationImageInfo(GrayF32 image, CalibrationObservation observations) {
        this.image = image.clone();
        this.calibPoints.setTo(observations);
    }

    public CalibrationObservation getCalibPoints() {
        return this.calibPoints;
    }
}