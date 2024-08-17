package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.model.city.CityItemEntity
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse

class CityRepositoryImplTest {
    private lateinit var cityRepository: CityRepositoryImpl
    private val cityService: CityService = mock(CityService::class.java)
    private val cityDao: CityDao = mock(CityDao::class.java)
    private val apiKey = "testApiKey"
    private val lat = 51.5074
    private val lon = -0.1278
    private val cityName = "London"
    private val country = "GB"
    private val cityItemEntity =
        CityItemEntity(
            country = cityName,
            name = country,
            state = "State",
            latitude = 0.0,
            longitude = 0.0,
        )

    private val cityItemModel =
        CityItemModel(
            name = cityName,
            country = country,
            latitude = lat,
            longitude = lon,
        )

    @Before
    fun setUp() {
        cityRepository = CityRepositoryImpl(cityService, cityDao, apiKey)
    }

    @Test
    fun `getCityByCoordinates emits success when service call is successful`() =
        runTest {
            val cityModel = CityModel() // mock your city model
            `when`(cityService.getCityByCoordinates(lat, lon, apiKey)).thenReturn(cityModel)
            val flow = cityRepository.getCityByCoordinates(lat, lon)
            val result = flow.toList()
            assertEquals(Response.Loading, result[0])
            assertEquals(Response.Success(cityModel), result[1])
        }

    @Test
    fun `getCityByCoordinates emits failure when service call throws an exception`() =
        runTest {
            `when`(cityService.getCityByCoordinates(lat, lon, apiKey))
                .thenThrow(
                    HttpException(
                        RetrofitResponse
                            .error<CityModel>(500, "Server error".toResponseBody(null)),
                    ),
                )
            val flow = cityRepository.getCityByCoordinates(lat, lon)
            val result = flow.toList()
            assertEquals(Response.Loading, result[0])
            assertTrue(result[1] is Response.Failure)
            assertTrue((result[1] as Response.Failure).e is CustomError.ApiError)
            assertEquals(
                ErrorCode.NETWORK_ERROR.message,
                (result[1] as Response.Failure).e?.message,
            )
        }

    @Test
    fun `getCitiesByName emits success when service call is successful`() =
        runTest {
            val cityModel = CityModel() // mock your city model
            `when`(cityService.getCitiesByName(cityName, apiKey)).thenReturn(cityModel)
            val flow = cityRepository.getCitiesByName(cityName)
            val result = flow.toList()
            assertEquals(Response.Loading, result[0])
            assertEquals(Response.Success(cityModel), result[1])
        }

    @Test
    fun `getAllSavedCities emits success when dao call is successful`() =
        runTest {
            val cityEntities =
                listOf(cityItemEntity)
            `when`(cityDao.getAllCities()).thenReturn(flowOf(cityEntities))
            val flow = cityRepository.getAllSavedCities()

            val result = flow.toList()
            assertEquals(Response.Loading, result[0])
            assertTrue(result[1] is Response.Success)
        }

    @Test
    fun `deleteCity calls deleteCity on dao with correct parameters`() =
        runTest {
            cityRepository.deleteCity(cityItemModel)
            verify(cityDao).deleteCity(cityName, country)
        }

    @Test
    fun `isCitySaved returns true when city is saved`() =
        runTest {
            `when`(cityDao.isCitySaved(cityName, country)).thenReturn(true)

            val result = cityRepository.isCitySaved(cityItemModel)
            assertTrue(result)
        }

    @Test
    fun `isCitySaved returns false when city is not saved`() =
        runTest {
            `when`(cityDao.isCitySaved(cityName, country)).thenReturn(false)
            val result = cityRepository.isCitySaved(cityItemModel)
            assertFalse(result)
        }
}
