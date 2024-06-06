import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient{
//    private const val BASE_URL = "http://192.168.174.1:3000/"
//    private const val BASE_URL = "http://192.168.27.88:3000/"
//    private const val BASE_URL = "http://192.168.1.79:3000/"
    private const val BASE_URL = "http://192.168.77.248:3000/"

    fun getRetroInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .connectTimeout(100, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(100, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
