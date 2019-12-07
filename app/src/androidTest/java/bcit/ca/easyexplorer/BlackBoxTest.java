package bcit.ca.easyexplorer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class BlackBoxTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.android.testing.uiautomator.BasicSample";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String EXPLORER_PACKAGE = "bcit.ca.easyexplorer";
    private UiDevice device;
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFolderCreate() {
        onView(withId(R.id.plus)).perform(click());
        onView(withId(R.id.plus)).perform(click());
        onView(withText("FOLDER2")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.createFolderField)).perform(typeText("TestFolder"));
        onView(withText("OK")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(11).check(matches(withText("TestFolder")));
    }

    @Test
    public void testFolderRenameAndDelete(){
        onView(withId(R.id.plus)).perform(click());
        onView(withId(R.id.plus)).perform(click());
        onView(withText("FOLDER2")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.createFolderField)).perform(typeText("TestFolder"));
        onView(withText("OK")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(11).check(matches(withText("TestFolder")));
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(11).perform(longClick());
        onView(withId(R.id.renameInputField)).perform(clearText(), typeText("renamedFolder"));
        onView(withText("OK")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(5).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(11).check(matches(withText("renamedFolder")));
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(11).perform(longClick());
        onView(withId(R.id.renameInputField)).perform(clearText());
        onView(withText("OK")).perform(click());
    }
}
