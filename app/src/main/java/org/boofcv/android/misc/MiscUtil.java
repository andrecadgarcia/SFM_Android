package org.boofcv.android.misc;

import android.hardware.Camera;

import com.andrecadgarcia.sfm.CameraSpecs;
import com.andrecadgarcia.sfm.activity.MainActivity;

import boofcv.struct.calib.IntrinsicParameters;
import georegression.metric.UtilAngle;

/**
 * @author Peter Abeles
 */
public class MiscUtil {
	/**
	 * Either loads the current intrinsic parameters or makes one up from camera information
	 * if it doesn't exist
	 */
	public static IntrinsicParameters checkThenInventIntrinsic() {

		IntrinsicParameters intrinsic;

		// make sure the camera is calibrated first
		if( MainActivity.preference.intrinsic == null ) {
			CameraSpecs specs = MainActivity.specs.get(MainActivity.preference.cameraId);

			Camera.Size size = specs.sizePreview.get( MainActivity.preference.preview);

			intrinsic = new IntrinsicParameters();

			double hfov = UtilAngle.degreeToRadian(specs.horizontalViewAngle);
			double vfov = UtilAngle.degreeToRadian(specs.verticalViewAngle);

			intrinsic.width = size.width; intrinsic.height = size.height;
			intrinsic.cx = intrinsic.width/2;
			intrinsic.cy = intrinsic.height/2;
			intrinsic.fx = intrinsic.cx / Math.tan(hfov/2.0f);
			intrinsic.fy = intrinsic.cy / Math.tan(vfov/2.0f);
		} else {
			intrinsic = MainActivity.preference.intrinsic;
		}

		return intrinsic;
	}
}
