package bcit.ca.easyexplorer;
import androidx.test.rule.ActivityTestRule;
import androidx.test.espresso.Espresso;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchTest() {
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(3).perform(click());
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.searchLine)).perform(typeText("icu"), pressImeActionButton());//.check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.result)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(3).perform(click());
        onView(withId(R.id.fileListView)).check(matches(isDisplayed()));
    }

    @Test
    public void switchFolderTest(){
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(7).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(2).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(4).perform(click());
        onView(withText("SYSTEM")).perform(click());
        onView(withId(R.id.fileListView)).check(matches(isDisplayed()));
    }

    @Test
    public void switchWindows(){
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(7).perform(click());
        onView(withId(R.id.plus)).perform(click());
        onView(withId(R.id.plus)).perform(click());
        onView(withText("FOLDER2")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(3).perform(click());
        onView(withText("FOLDER3")).perform(click());
        onData(anything()).inAdapterView(withId(R.id.fileListView)).atPosition(1).perform(click());
        onView(withText("FOLDER1")).perform(click());
        onView(withText("FOLDER2")).perform(click());
        onView(withId(R.id.fileListView)).check(matches(isDisplayed()));
    }
}
