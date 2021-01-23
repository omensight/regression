package com.alphemsoft.education.regression.dataexporter

import com.alphemsoft.education.regression.helper.whenever
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File
import kotlin.reflect.full.createInstance

@RunWith(PowerMockRunner::class)
class FileDataTest{

    @Test
    fun getSupportedFormats_hasAtLeastOneElement(){
        val supportedFormatList: List<String> = FileData.getSupportedFormats()
        Assert.assertEquals(FileData.Format.values().size, supportedFormatList.size)
    }

    
}
