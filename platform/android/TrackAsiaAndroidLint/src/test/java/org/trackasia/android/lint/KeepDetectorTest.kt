package org.trackasia.android.lint

/*
import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

    @Test
@RunWith(RobolectricTestRunner::class)
class KeepDetectorTest {

    fun correctClassName() {
        lint()
            .allowMissingSdk()
            .files(
                java(
                    """
        |package foo;
        |
        |import android.support.annotation.Keep;
        |
        |@Keep
        |class TestClass {
        |}
                     """.trimMargin()
                )
            )
            .issues(KeepDetector.ISSUE_NOT_KEPT)
            .run()
            .expectClean()
    }
} */
