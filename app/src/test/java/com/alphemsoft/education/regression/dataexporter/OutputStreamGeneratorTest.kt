package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.alphemsoft.education.regression.helper.mock
import com.alphemsoft.education.regression.helper.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.verification.VerificationMode
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MediaStore::class)
class OutputStreamGeneratorTest {

    private lateinit var fileName: String
    private lateinit var fileData : DataExportHelper.FileData.Csv
    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver

    private lateinit var uri: Uri

    @Before
    fun setup(){
        PowerMockito.mockStatic(MediaStore.Files::class.java)
        uri = mock()
        val mediaUri: Uri = mock()
        whenever(MediaStore.Files.getContentUri(Mockito.anyString())).thenReturn(mediaUri)
        fileName = "FILE_NAME"
        fileData = mock()
        contentResolver = mock()
        context = mock()
        whenever(context.contentResolver).thenReturn(contentResolver)

        whenever(contentResolver.openOutputStream(any())).thenReturn(mock())
    }

    @Test
    fun whenGenerating_getFileName(){

        OutPutStreamGenerator.generate(fileData, contentResolver)
        verify(fileData).fileName
    }

    @Test
    fun whenGenerating_getMimeType(){
        OutPutStreamGenerator.generate(fileData, contentResolver)
        verify(fileData).commonMimeType
    }

    @Test
    fun whenGenerating_returnNonNullValue(){
        whenever(contentResolver.insert(any(), any())).thenReturn(uri)
        Assert.assertNotNull(OutPutStreamGenerator.generate(fileData, contentResolver))
    }

    @Test
    fun whenGenerating_createsUri(){
        OutPutStreamGenerator.generate(fileData, contentResolver)
        verify(contentResolver).insert(any(), any())
    }
    @Test
    fun whenGenerating_callToOpenOutputStream(){
        whenever(contentResolver.insert(any(), any())).thenReturn(uri)
        OutPutStreamGenerator.generate(fileData, contentResolver)
        verify(contentResolver).openOutputStream(any())
    }

    @Test
    fun whenGenerating_verifyContentResolverDoNotOpenOutputStream(){
        whenever(contentResolver.insert(any(), any())).thenReturn(null)
        verify(contentResolver, times(0)).openOutputStream(any(), anyString())
    }

    @Test
    fun whenGenerating_verifyContentResolverOpensOutputStream(){
        whenever(contentResolver.insert(any(), any())).thenReturn(uri)
        OutPutStreamGenerator.generate(fileData, contentResolver)
        verify(contentResolver, times(1)).openOutputStream(any())
    }

    @Test
    fun whenGenerating_returnNullIfUriIsNull(){
        whenever(contentResolver.insert(any(), any())).thenReturn(null)
        Assert.assertNull(OutPutStreamGenerator.generate(fileData, contentResolver))
    }

}