package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MimeTypeMap::class, Uri::class, MediaStore::class, MediaStore.Files::class)
class OutPutStreamGeneratorTest {

    private lateinit var outPutStreamGenerator: OutPutStreamGenerator
    private lateinit var fileName: String
    private lateinit var fileData : FileData.Csv
    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver

    private lateinit var uri: Uri

    @Before
    fun setup(){
        PowerMockito.mockStatic(MimeTypeMap::class.java)
        PowerMockito.mockStatic(Uri::class.java)
        PowerMockito.mockStatic(MediaStore::class.java)
        PowerMockito.mockStatic(MediaStore.Files::class.java)
        outPutStreamGenerator = OutPutStreamGenerator()
        val mimeTypeMap: MimeTypeMap = mock()
        whenever(mimeTypeMap.getMimeTypeFromExtension("csv")).thenReturn("text/*")
        whenever(mimeTypeMap.getMimeTypeFromExtension("xlsx")).thenReturn("application/vnd.ms-excel")
        whenever(MimeTypeMap.getSingleton()).thenReturn(mimeTypeMap)
        whenever(Uri.fromFile(any())).thenReturn(mock())
        uri = mock()
        val mediaUri: Uri = mock()
        whenever(MediaStore.Files.getContentUri(anyString())).thenReturn(mediaUri)
        fileName = "FILE_NAME"
        fileData = mock()
        whenever(fileData.fileName).thenReturn(fileName)
        contentResolver = mock()
        context = mock()
        whenever(context.contentResolver).thenReturn(contentResolver)

        whenever(contentResolver.openOutputStream(any())).thenReturn(mock())
    }

    @Test
    fun whenGenerating_returnNonNullValue(){
        whenever(contentResolver.insert(any(), any())).thenReturn(uri)
        Assert.assertNotNull(outPutStreamGenerator.getUri("file", "csv", contentResolver))
    }

    @Test
    fun whenGenerating_createsUri(){
        outPutStreamGenerator.getUri("file", "csv", contentResolver)
        verify(contentResolver).insert(any(), any())
    }

    @Test
    fun whenGenerating_verifyContentResolverDoNotOpenOutputStream(){
//        whenever(contentResolver.insert(any(), any())).thenReturn(null)
//        verify(contentResolver, times(0)).openOutputStream(any(), anyString())
    }

    @Test
    fun whenGenerating_returnNullIfUriIsNull(){
        whenever(contentResolver.insert(any(), any())).thenReturn(null)
        Assert.assertNull(outPutStreamGenerator.getUri("file", "csv", contentResolver))
    }

    @Test
    fun whenGettingUri_returnsNullIfNameIsEmpty(){
        assertThat(outPutStreamGenerator.getUri("", "csv", contentResolver)).isNull()
    }

}