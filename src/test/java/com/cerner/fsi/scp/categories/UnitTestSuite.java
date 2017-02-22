package com.cerner.fsi.scp.categories;

import com.cerner.fsi.scp.control.AuthenticatorTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by VR033549 on 10/6/2016.
 */
@RunWith(Categories.class)
@Categories.ExcludeCategory(IntegrationTests.class)
@Suite.SuiteClasses({ AuthenticatorTest.class})
public class UnitTestSuite
{
}
