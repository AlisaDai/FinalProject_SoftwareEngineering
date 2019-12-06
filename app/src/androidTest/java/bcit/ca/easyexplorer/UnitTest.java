package bcit.ca.easyexplorer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTest {

    @Test
    public void canChangeName() {
        assertEquals(true, FileName.ifNeedToChange("Object", "Picture"));
    }

    @Test
    public void cannotChangeName() {
        assertEquals(false, FileName.ifNeedToChange("picture", "Picture"));
    }

    @Test
    public void correctNewnameWithoutRepeatName() {
        assertEquals("sameName", FileName.correctRepeatName("sameName", new String[]{"picture", "users", "noname"}));
    }

    @Test
    public void correctNewnameWithTwoRepeatName() {
        assertEquals("sameName_2", FileName.correctRepeatName("sameName", new String[]{"picture", "sameName_1", "noname", "sameName"}));
    }

    @Test
    public void correctNewnameWithFourRepeatName() {
        assertEquals("sameName_4", FileName.correctRepeatName("sameName", new String[]{"sameName_3", "picture", "sameName_1", "noname", "sameName", "sameName_2"}));
    }

    @Test
    public void correctNameArrayWithoutRepeatName() {
        assertEquals(new String[]{"sameName", "sameName_1", "sameName_2"},
                FileName.newMultipleNames("sameName", 3, new String[]{"picture", "users", "noname"}));
    }

    @Test
    public void correctNameArrayWithOneRepeatName() {
        assertEquals(new String[]{"sameName_1", "sameName_2", "sameName_3"},
                FileName.newMultipleNames("sameName", 3, new String[]{"picture", "users", "noname", "sameName"}));
    }

    @Test
    public void correctNameArrayWithThreeRepeatName() {
        assertEquals(new String[]{"sameName_3", "sameName_4", "sameName_5"},
                FileName.newMultipleNames("sameName", 3, new String[]{"picture", "sameName_2", "users", "sameName_1", "noname", "sameName"}));
    }
}
