package com.alphemsoft.education.regression.dataexporter

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class FileDataTest{

    @Test
    fun getSupportedFormats_hasAtLeastOneElement(){
        val supportedFormatList: List<String> = FileData.getSupportedFormats()
        Assert.assertEquals(FileData.Format.values().size, supportedFormatList.size)
    }

    
}
