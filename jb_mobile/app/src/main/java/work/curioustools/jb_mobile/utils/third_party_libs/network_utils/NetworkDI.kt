package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors.HeaderInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module @InstallIn(SingletonComponent::class)
class NetworkDI {

    @Singleton @Provides
    fun getGson(): Gson = GsonBuilder().serializeNulls().create()

    @Singleton @Provides
    fun getMoshi(): Moshi = Moshi.Builder().build()

    @Singleton @Provides
    fun getGsonConvertor(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton @Provides
    fun getMoshiConvertor(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton @Provides
    fun getScalerConvertor(): ScalarsConverterFactory = ScalarsConverterFactory.create()

    @Singleton @Provides
    fun getDefaultLogSeverity(): HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

    @Singleton @Provides
    fun getLogInterceptor(severity: HttpLoggingInterceptor.Level): HttpLoggingInterceptor = HttpLoggingInterceptor().run {
        level = severity
        this
    }

    @Singleton @Provides
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().run {
        connectTimeout(1, TimeUnit.MINUTES)
        writeTimeout(1, TimeUnit.MINUTES)
        readTimeout(1, TimeUnit.MINUTES)
        retryOnConnectionFailure(true)
        addInterceptor(loggingInterceptor)
        build()
    }

    @Singleton @Provides
    fun getTemporaryBaseUrl(): String = "https://www.google.com"

    @Singleton @Provides
    fun getRetrofitClient(
        baseUrl: String,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder().run {
        baseUrl(baseUrl)
        client(okHttpClient)
        addConverterFactory(scalarConvertor)
        addConverterFactory(moshiConverter)
        build()
    }


    @Singleton @Provides @NetworkWithHeaders
    fun getHeaderInterceptorWithHeaders(): HeaderInterceptor =
        HeaderInterceptor(hashMapOf(HeaderInterceptor.HEADER_DUMMY_API_AUTH_PAIR))


    @Singleton @Provides @NetworkWithHeaders
    fun getOkHttpClientWithHeaders(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient = OkHttpClient.Builder().run {
        connectTimeout(1, TimeUnit.MINUTES)
        writeTimeout(1, TimeUnit.MINUTES)
        readTimeout(1, TimeUnit.MINUTES)
        retryOnConnectionFailure(true)
        addInterceptor(loggingInterceptor)
        addInterceptor(headerInterceptor)
        build()
    }

    @Singleton @Provides @NetworkWithHeaders
    fun getRetrofitClientWithHeaders(
        baseUrl: String,
        okHttpClientWithDummyHeaders: OkHttpClient,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
    ): Retrofit = Retrofit.Builder().run {
        baseUrl(baseUrl)
        client(okHttpClientWithDummyHeaders)
        addConverterFactory(scalarConvertor)
        addConverterFactory(moshiConverter)
        build()
    }

    @Qualifier @Retention(AnnotationRetention.BINARY) @Keep
    annotation class NetworkWithHeaders

}




//@Module
//class APIDI{
//
//
//    @Singleton @Provides
//    fun getPostsApi(retrofitWithHeaders: Retrofit) = retrofitWithHeaders.run {
//        val url =  "https://dummyapi.io/data/api/"
//        newBuilder().baseUrl(url).build().create(PostsApi::class.java)
//    }
//
//
//    @Singleton @Provides
//    fun getAllUsersApi(retrofitWithHeaders: Retrofit) = retrofitWithHeaders.run {
//        val url =  "https://dummyapi.io/data/api/"
//        newBuilder().baseUrl(url).build().create(AllUsersApi::class.java)
//    }
//
//
//}