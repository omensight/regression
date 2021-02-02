package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class OutPutStreamGeneratorTest {

    private lateinit var outPutStreamGenerator: OutPutStreamGenerator
    private lateinit var fileName: String
    private lateinit var fileData : FileData.Csv
    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver

    private lateinit var uri: Uri
    private lateinit var uriForOldApi: Uri

    @Before
    fun setup(){

        outPutStreamGenerator = OutPutStreamGenerator()
        fileName = "FILE_NAME"
        context = InstrumentationRegistry.getInstrumentation().targetContext
        contentResolver = context.contentResolver
    }

    @Test
    fun whenGenerating_returnNonNullValue(){
        Assert.assertNotNull(outPutStreamGenerator.getUri("file", "csv", contentResolver))
    }

    @Test
    fun whenGenerating_createsUri(){
        outPutStreamGenerator.getUri("file", "csv", contentResolver)
    }

    @Test
    fun whenGenerating_verifyContentResolverDoNotOpenOutputStream(){
//        whenever(contentResolver.insert(any(), any())).thenReturn(null)
//        verify(contentResolver, times(0)).openOutputStream(any(), anyString())
    }

    @Test
    fun whenGenerating_returnNullIfUriIsNull(){
        Assert.assertNull(outPutStreamGenerator.getUri("", "csv", contentResolver))
    }

    @Test
    fun whenGettingUri_returnsNullIfNameIsEmpty(){
        assertThat(outPutStreamGenerator.getUri("", "csv", contentResolver)).isNull()
    }

    @Test
    fun whenGettingUri_returnUriForOldApi(){
        assertThat(outPutStreamGenerator.getUri("FILE", "CSV", contentResolver)).isNotNull()
    }


}