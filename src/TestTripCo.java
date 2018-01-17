import static org.junit.Assert.*;

import model.TestLeg;
import model.TestLocation;
import model.TestModel;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import presenter.TestPresenter;
import view.*;

public class TestTripCo {

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            TestModel.class,
            TestPresenter.class,
            ViewTest.class,
            TestLeg.class,
            TestLocation.class,
            PointTest.class,
            SegmentTest.class,
            WriteSvgFileTest.class,
            WriteXmlFileTest.class
    })

    public class TestAll {
    }

}
