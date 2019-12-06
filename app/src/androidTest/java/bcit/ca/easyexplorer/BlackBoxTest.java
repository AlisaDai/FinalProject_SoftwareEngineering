package bcit.ca.easyexplorer;

import android.content.Context;
import android.content.Intent;

import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class BlackBoxTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.android.testing.uiautomator.BasicSample";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String EXPLORER_PACKAGE = "bcit.ca.easyexplorer";
    private UiDevice device;
    @Before
    public void startMainActivityFromHomeScreen() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressHome();
        // Wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        Assert.assertThat(launcherPackage, IsNull.notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
        // Launch the app
        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //context.startActivity(intent);
        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    public void setUp() {
        // Launch a simple calculator app
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(EXPLORER_PACKAGE);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Clear out any previous instances
        context.startActivity(intent);
        device.wait(Until.hasObject(By.pkg(EXPLORER_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void tests() throws UiObjectNotFoundException {
        UiCollection files = new UiCollection(new UiSelector()
                .packageName(EXPLORER_PACKAGE).resourceId("fileListView"));
        int count = files.getChildCount(new UiSelector()
                .className("android.widget.LinearLayout"));
        Assert.assertEquals("14", count);
        // Find a specific video and simulate a user-click on it
        /*UiObject file = files.getChildByText(new UiSelector()
                .className("android.widget.LinearLayout"), "user");
        file.click();*/
    }
}
